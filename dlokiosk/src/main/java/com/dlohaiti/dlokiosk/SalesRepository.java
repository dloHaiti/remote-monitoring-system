package com.dlohaiti.dlokiosk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.db.KioskDatabase;
import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.Sale;
import com.google.inject.Inject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesRepository {
    private final KioskDatabase db;
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss z";
    private final DateFormat df = new SimpleDateFormat(DATE_FORMAT);

    @Inject
    public SalesRepository(Context context) {
        this.db = new KioskDatabase(context);
    }

    public List<Sale> list() {
        String[] columns = {KioskDatabase.SalesTable.ID, KioskDatabase.SalesTable.SKU, KioskDatabase.SalesTable.QUANTITY, KioskDatabase.SalesTable.CREATED_AT};
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        Cursor cursor = readableDatabase.query(KioskDatabase.SalesTable.TABLE_NAME, columns, null, null, null, null, null);
        List<Sale> sales = new ArrayList<Sale>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Date date = null;
            try {
                date = df.parse(cursor.getString(3));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sales.add(new Sale(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), date));
            cursor.moveToNext();
        }
        readableDatabase.close();
        return sales;
    }

    public void add(List<Product> products) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        writableDatabase.beginTransaction();
        String now = df.format(new Date());
        try {
            for (Product product : products) {
                ContentValues values = new ContentValues();
                values.put(KioskDatabase.SalesTable.QUANTITY, 1); //TODO: accurately reflect quantity
                values.put(KioskDatabase.SalesTable.SKU, product.getSku());
                values.put(KioskDatabase.SalesTable.CREATED_AT, now);
                writableDatabase.insert(KioskDatabase.SalesTable.TABLE_NAME, null, values);
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
        } catch (Exception e) {
            //TODO: alert this?
            throw new RuntimeException(e);
        } finally {
            writableDatabase.close();
        }
    }
}
