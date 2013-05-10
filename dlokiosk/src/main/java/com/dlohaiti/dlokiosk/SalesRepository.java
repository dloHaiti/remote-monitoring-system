package com.dlohaiti.dlokiosk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.db.KioskDatabase;
import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.Sale;
import com.google.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class SalesRepository {
    private final KioskDatabase db;

    @Inject
    public SalesRepository(Context context) {
        this.db = new KioskDatabase(context);
    }

    public List<Sale> list() {
        String[] columns = {KioskDatabase.SalesTable.ID, KioskDatabase.SalesTable.SKU, KioskDatabase.SalesTable.QUANTITY, KioskDatabase.SalesTable.CREATED_AT};
        Cursor cursor = db.getReadableDatabase().query(KioskDatabase.SalesTable.TABLE_NAME, columns, null, null, null, null, null);
        List<Sale> sales = new ArrayList<Sale>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sales.add(new Sale(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3)));
            cursor.moveToNext();
        }
        return sales;
    }

    public void add(List<Product> products) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        writableDatabase.beginTransaction();
        String now = new DateTime(DateTimeZone.UTC).toString(ISODateTimeFormat.basicDateTime());
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
