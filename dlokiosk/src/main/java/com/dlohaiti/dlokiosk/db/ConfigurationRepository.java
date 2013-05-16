package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.domain.Kiosk;
import com.google.inject.Inject;

public class ConfigurationRepository {
    private final KioskDatabase db;

    @Inject
    public ConfigurationRepository(KioskDatabase db) {
        this.db = db;
    }

    public void save(String kioskId, String kioskPassword) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KioskDatabase.ConfigurationTable.KIOSK_ID, kioskId);
        values.put(KioskDatabase.ConfigurationTable.KIOSK_PASSWORD, kioskPassword);
        writableDatabase.beginTransaction();
        try {
            writableDatabase.insert(KioskDatabase.ConfigurationTable.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            //TODO: log? alert?
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public Kiosk getKiosk() {
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        String[] columns = {KioskDatabase.ConfigurationTable.KIOSK_ID, KioskDatabase.ConfigurationTable.KIOSK_PASSWORD};
        Kiosk kiosk = new Kiosk("", "");
        readableDatabase.beginTransaction();
        try {
            Cursor cursor = readableDatabase.query(KioskDatabase.ConfigurationTable.TABLE_NAME, columns, null, null, null, null, null);
            //TODO: if more than one result, show an error?
            cursor.moveToFirst();
            kiosk = new Kiosk(cursor.getString(0), cursor.getString(1));
            readableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            //TODO: log? alert?
        } finally {
            readableDatabase.endTransaction();
        }
        return kiosk;
    }
}
