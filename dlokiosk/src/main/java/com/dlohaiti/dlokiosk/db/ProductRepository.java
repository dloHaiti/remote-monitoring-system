package com.dlohaiti.dlokiosk.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.domain.Product;
import com.google.inject.Inject;
import org.springframework.util.support.Base64;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private final KioskDatabase db;
    private final Context context;

    @Inject
    public ProductRepository(Context context, KioskDatabase db) {
        this.context = context;
        this.db = db;
    }

    public List<Product> list() {
        List<Product> products = new ArrayList<Product>();
        String[] columns = {
                KioskDatabase.ProductsTable.ID,
                KioskDatabase.ProductsTable.SKU,
                KioskDatabase.ProductsTable.ICON,
                KioskDatabase.ProductsTable.REQUIRES_QUANTITY,
                KioskDatabase.ProductsTable.MINIMUM_QUANTITY,
                KioskDatabase.ProductsTable.MAXIMUM_QUANTITY
        };
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        readableDatabase.beginTransaction();
        try {
            Cursor cursor = readableDatabase.query(KioskDatabase.ProductsTable.TABLE_NAME, columns, null, null, null, null, null);
            cursor.moveToFirst();
            //TODO: guard against/fail gracefully when running out of memory
            while (!cursor.isAfterLast()) {
                products.add(buildProduct(cursor));
                cursor.moveToNext();
            }
            readableDatabase.setTransactionSuccessful();
            return products;
        } finally {
            readableDatabase.endTransaction();
        }
    }

    private Product buildProduct(Cursor cursor) {
        String sku = cursor.getString(1);
        boolean requiresQuantity = cursor.getInt(3) == 1;
        Integer minimum = cursor.getInt(4);
        Integer maximum = cursor.getInt(5);
        Bitmap resource; //TODO: how can we make this show the unknown image when it doesn't decode properly?
        try {
            byte[] imageData = Base64.decode(cursor.getString(2));
            resource = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), String.format("image for product(%s) could not be decoded", sku));
            resource = BitmapFactory.decodeResource(context.getResources(), R.drawable.unknown);
        }
        //FIXME: default quantity of 1
        return new Product(cursor.getLong(0), sku, resource, requiresQuantity, 1, minimum, maximum);
    }

    public Product findById(Long id) {
        String[] columns = {
                KioskDatabase.ProductsTable.ID,
                KioskDatabase.ProductsTable.SKU,
                KioskDatabase.ProductsTable.ICON,
                KioskDatabase.ProductsTable.REQUIRES_QUANTITY,
                KioskDatabase.ProductsTable.MINIMUM_QUANTITY,
                KioskDatabase.ProductsTable.MAXIMUM_QUANTITY
        };
        String selection = String.format("%s=?", KioskDatabase.ProductsTable.ID);
        String[] args = {String.valueOf(id)};
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        readableDatabase.beginTransaction();
        try {
            Cursor cursor = readableDatabase.query(KioskDatabase.ProductsTable.TABLE_NAME, columns, selection, args, null, null, null);
            if (cursor.getCount() != 1) {
                //TODO: make this graceful
                return new Product(null, null, null, false, null, null, null);
            }
            cursor.moveToFirst();
            Product product = buildProduct(cursor);
            readableDatabase.setTransactionSuccessful();
            return product;
        } finally {
            readableDatabase.endTransaction();
        }
    }
}
