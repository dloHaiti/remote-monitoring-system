package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.domain.Kiosk;
import com.google.inject.Inject;

public class ConfigurationRepository {
    private final KioskDatabase db;
    private final String[] columns = new String[]{KioskDatabase.ConfigurationTable.KEY, KioskDatabase.ConfigurationTable.VALUE};

    @Inject
    public ConfigurationRepository(KioskDatabase db) {
        this.db = db;
    }

    public void save(String kioskId, String kioskPassword) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        ContentValues id = new ContentValues();
        id.put(KioskDatabase.ConfigurationTable.KEY, ConfigurationKey.KIOSK_ID.name());
        id.put(KioskDatabase.ConfigurationTable.VALUE, kioskId);
        ContentValues pw = new ContentValues();
        pw.put(KioskDatabase.ConfigurationTable.KEY, ConfigurationKey.KIOSK_PASSWORD.name());
        pw.put(KioskDatabase.ConfigurationTable.VALUE, kioskPassword);
        writableDatabase.beginTransaction();
        try {
            writableDatabase.update(KioskDatabase.ConfigurationTable.TABLE_NAME, id, String.format("%s=?", KioskDatabase.ConfigurationTable.KEY), new String[]{ConfigurationKey.KIOSK_ID.name()});
            writableDatabase.update(KioskDatabase.ConfigurationTable.TABLE_NAME, pw, String.format("%s=?", KioskDatabase.ConfigurationTable.KEY), new String[]{ConfigurationKey.KIOSK_PASSWORD.name()});
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            //TODO: log? alert?
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public Kiosk getKiosk() {
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        String kioskId = "";
        String kioskPassword = "";
        readableDatabase.beginTransaction();
        try {
            String selection = String.format("%s=? OR %s=?", KioskDatabase.ConfigurationTable.KEY, KioskDatabase.ConfigurationTable.KEY);
            String[] selectionArgs = {ConfigurationKey.KIOSK_ID.name(), ConfigurationKey.KIOSK_PASSWORD.name()};
            Cursor cursor = readableDatabase.query(KioskDatabase.ConfigurationTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String key = cursor.getString(0);
                String value = cursor.getString(1);
                if (ConfigurationKey.KIOSK_ID.name().equals(key)) {
                    kioskId = value;
                } else if (ConfigurationKey.KIOSK_PASSWORD.name().equals(key)) {
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

    public String get(ConfigurationKey key) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor cursor = rdb.query(KioskDatabase.ConfigurationTable.TABLE_NAME, columns, String.format("%s=?", KioskDatabase.ConfigurationTable.KEY), new String[]{key.name()}, null, null, null);
            cursor.moveToFirst();
            //TODO: more than one result
            rdb.setTransactionSuccessful();
            return cursor.getString(1);
        } finally {
            rdb.endTransaction();
        }
    }

    public Integer getInt(ConfigurationKey key) {
        try {
            return Integer.valueOf(get(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
