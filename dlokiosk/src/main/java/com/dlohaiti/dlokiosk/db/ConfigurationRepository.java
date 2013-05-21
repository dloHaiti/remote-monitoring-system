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
        ContentValues id = new ContentValues();
        id.put(KioskDatabase.ConfigurationTable.KEY, "kioskId");
        id.put(KioskDatabase.ConfigurationTable.VALUE, kioskId);
        ContentValues pw = new ContentValues();
        pw.put(KioskDatabase.ConfigurationTable.KEY, "kioskPassword");
        pw.put(KioskDatabase.ConfigurationTable.VALUE, kioskPassword);
        writableDatabase.beginTransaction();
        try {
            writableDatabase.insert(KioskDatabase.ConfigurationTable.TABLE_NAME, null, id);
            writableDatabase.insert(KioskDatabase.ConfigurationTable.TABLE_NAME, null, pw);
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            //TODO: log? alert?
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public Kiosk getKiosk() {
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        String[] columns = {KioskDatabase.ConfigurationTable.KEY, KioskDatabase.ConfigurationTable.VALUE};
        String kioskId = "";
        String kioskPassword = "";
        readableDatabase.beginTransaction();
        try {
            Cursor cursor = readableDatabase.query(KioskDatabase.ConfigurationTable.TABLE_NAME, columns, String.format("%s=? OR %s=?", KioskDatabase.ConfigurationTable.KEY, KioskDatabase.ConfigurationTable.KEY), new String[]{"kioskId", "kioskPassword"}, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String key = cursor.getString(0);
                String value = cursor.getString(1);
                if ("kioskId".equals(key)) {
                    kioskId = value;
                } else if ("kioskPassword".equals(key)) {
                    kioskPassword = value;
                }
                cursor.moveToNext();
            }
            readableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            //TODO: log? alert?
        } finally {
            readableDatabase.endTransaction();
        }
        return new Kiosk(kioskId, kioskPassword);
    }
}
