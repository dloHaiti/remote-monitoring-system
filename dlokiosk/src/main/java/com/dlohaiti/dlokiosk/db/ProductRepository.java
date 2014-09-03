package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import com.dlohaiti.dlokiosk.Base64ImageConverter;
import com.dlohaiti.dlokiosk.domain.Money;
import com.dlohaiti.dlokiosk.domain.Product;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class ProductRepository {
    private final static String TAG = ProductRepository.class.getSimpleName();
    private final KioskDatabase db;
    private final Base64ImageConverter imageConverter;
    private final static String[] columns = new String[]{
            KioskDatabase.ProductsTable.ID,
            KioskDatabase.ProductsTable.SKU,
            KioskDatabase.ProductsTable.ICON,
            KioskDatabase.ProductsTable.REQUIRES_QUANTITY,
            KioskDatabase.ProductsTable.MINIMUM_QUANTITY,
            KioskDatabase.ProductsTable.MAXIMUM_QUANTITY,
            KioskDatabase.ProductsTable.PRICE,
            KioskDatabase.ProductsTable.CURRENCY,
            KioskDatabase.ProductsTable.DESCRIPTION,
            KioskDatabase.ProductsTable.GALLONS,
            KioskDatabase.ProductsTable.CATEGORY_ID
    };

    @Inject
    public ProductRepository(KioskDatabase db, Base64ImageConverter imageConverter) {
        this.db = db;
        this.imageConverter = imageConverter;
    }

    public List<Product> list() {
        List<Product> products = new ArrayList<Product>();
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        readableDatabase.beginTransaction();
        Cursor cursor = readableDatabase.query(KioskDatabase.ProductsTable.TABLE_NAME, columns, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                products.add(buildProduct(cursor));
                cursor.moveToNext();
            }
            readableDatabase.setTransactionSuccessful();
            return products;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load all products from the database.", e);
            return new ArrayList<Product>();
        } finally {
            cursor.close();
            readableDatabase.endTransaction();
        }
    }

    private Product buildProduct(Cursor cursor) {
        String sku = cursor.getString(1);
        boolean requiresQuantity = Boolean.parseBoolean(cursor.getString(3));
        Integer minimum = cursor.getInt(4);
        Integer maximum = cursor.getInt(5);
        Money price = new Money(new BigDecimal(cursor.getDouble(6)));
        String description = cursor.getString(8);
        Integer gallons = cursor.getInt(9);
        long category = cursor.getInt(10);
        Bitmap resource = imageConverter.fromBase64EncodedString(cursor.getString(2));
        long id = cursor.getLong(0);
        return new Product(id, sku, resource, requiresQuantity, 1, minimum, maximum, price, description, gallons,
                category);
    }

    public Product findById(Long id) {
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        readableDatabase.beginTransaction();
        Cursor cursor = readableDatabase.query(KioskDatabase.ProductsTable.TABLE_NAME, columns, where(KioskDatabase.ProductsTable.ID), matches(id), null, null, null);
        try {
            if (cursor.getCount() != 1) {
                return new Product(null, null, null, false, null, null, null, null, null, null, null);
            }
            cursor.moveToFirst();
            Product product = buildProduct(cursor);
            readableDatabase.setTransactionSuccessful();
            return product;
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to find product with id %d in the database.", id), e);
            return new Product(null, null, null, false, null, null, null, null, null, null, null);
        } finally {
            cursor.close();
            readableDatabase.endTransaction();
        }
    }

    public boolean replaceAll(List<Product> products) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(KioskDatabase.ProductsTable.TABLE_NAME, null, null);
            for (Product p : products) {
                ContentValues values = new ContentValues();
                values.put(KioskDatabase.ProductsTable.SKU, p.getSku());
                values.put(KioskDatabase.ProductsTable.PRICE, p.getPrice().getAmount().toString());
                values.put(KioskDatabase.ProductsTable.DESCRIPTION, p.getDescription());
                values.put(KioskDatabase.ProductsTable.GALLONS, p.getGallons());
                values.put(KioskDatabase.ProductsTable.ICON, imageConverter.toBase64EncodedString(p.getImageResource()));
                values.put(KioskDatabase.ProductsTable.MINIMUM_QUANTITY, p.getMinimumQuantity());
                values.put(KioskDatabase.ProductsTable.MAXIMUM_QUANTITY, p.getMaximumQuantity());
                values.put(KioskDatabase.ProductsTable.REQUIRES_QUANTITY, String.valueOf(p.requiresQuantity()));
                values.put(KioskDatabase.ProductsTable.CURRENCY, p.getPrice().getCurrencyCode());
                values.put(KioskDatabase.ProductsTable.CATEGORY_ID, p.getCategoryId());
                wdb.insert(KioskDatabase.ProductsTable.TABLE_NAME, null, values);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to replace all products.", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }
}
