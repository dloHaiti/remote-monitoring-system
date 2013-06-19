package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.inject.Inject;

import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class ConfigurationRepository {
    private final static String TAG = ConfigurationRepository.class.getSimpleName();
    private final KioskDatabase db;
    private final String[] columns = new String[]{KioskDatabase.ConfigurationTable.KEY, KioskDatabase.ConfigurationTable.VALUE};

    @Inject
    public ConfigurationRepository(KioskDatabase db) {
        this.db = db;
    }

    public String get(ConfigurationKey key) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
            Cursor cursor = rdb.query(KioskDatabase.ConfigurationTable.TABLE_NAME, columns, where(KioskDatabase.ConfigurationTable.KEY), matches(key.name()), null, null, null);
        try {
            String value = "";
            if(cursor.moveToFirst()) {
                value = cursor.getString(1);
                rdb.setTransactionSuccessful();
            }
            return value;
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to get value for %s from database.", key.name()), e);
            return "";
        } finally {
            cursor.close();
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

    public boolean save(ConfigurationKey key, Integer value) {
        return save(key, String.valueOf(value));
    }

    public boolean save(ConfigurationKey key, String value) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(KioskDatabase.ConfigurationTable.KEY, key.name());
        val.put(KioskDatabase.ConfigurationTable.VALUE, value);
        writableDatabase.beginTransaction();
        try {
            //Depends on things being inserted beforehand
            writableDatabase.update(KioskDatabase.ConfigurationTable.TABLE_NAME, val, where(KioskDatabase.ConfigurationTable.KEY), matches(key.name()));
            writableDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to save value for configuration key %s to database.", key.name()), e);
            return false;
        } finally {
            writableDatabase.endTransaction();
        }
    }
}
