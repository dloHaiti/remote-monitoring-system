package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.inject.Inject;

public class ConfigurationRepository {
    private final KioskDatabase db;

    @Inject
    public ConfigurationRepository(KioskDatabase db) {
        this.db = db;
    }

    public void saveKioskId(String kioskId) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KioskDatabase.ConfigurationTable.KIOSK_ID, kioskId);
        try {
            writableDatabase.insert(KioskDatabase.ConfigurationTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            //TODO: log? alert?
        } finally {
            writableDatabase.close();
        }
    }

    public String getKioskId() {
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        String[] columns = {KioskDatabase.ConfigurationTable.KIOSK_ID};
        String kioskId = "";
        try {
            Cursor cursor = readableDatabase.query(KioskDatabase.ConfigurationTable.TABLE_NAME, columns, null, null, null, null, null);
            //TODO: if more than one result, show an error?
            cursor.moveToFirst();
            kioskId = cursor.getString(0);
        } catch (Exception e) {
            //TODO: log? alert?
        } finally {
            readableDatabase.close();
        }
        return kioskId;
    }
}
