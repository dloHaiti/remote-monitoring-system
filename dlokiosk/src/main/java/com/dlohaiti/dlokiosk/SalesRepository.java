package com.dlohaiti.dlokiosk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.db.KioskDatabase;
import com.dlohaiti.dlokiosk.domain.Product;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class SalesRepository {
    private final KioskDatabase db;

    @Inject
    public SalesRepository(Context context) {
        this.db = new KioskDatabase(context);
    }

    public List<Product> list() {
        String[] columns = {KioskDatabase.SalesTable.ID, KioskDatabase.SalesTable.SKU};
        Cursor cursor = db.getReadableDatabase().query(KioskDatabase.SalesTable.TABLE_NAME, columns, null, null, null, null, null);
        List<Product> products = new ArrayList<Product>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            products.add(new Product(cursor.getLong(0), cursor.getString(1), -1));
            cursor.moveToNext();
        }
        return products;
    }

    public void add(List<Product> products) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            for (Product product : products) {
                ContentValues values = new ContentValues();
                values.put(KioskDatabase.SalesTable.QUANTITY, 1);
                values.put(KioskDatabase.SalesTable.SKU, product.getSku());
                writableDatabase.insert(KioskDatabase.SalesTable.TABLE_NAME, null, values);
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
        } catch (Exception e) {
            //TODO
            throw new RuntimeException(e);
        } finally {
            writableDatabase.close();
        }
    }
}
