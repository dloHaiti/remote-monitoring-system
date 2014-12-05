package com.dlohaiti.dlokiosknew.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dlohaiti.dlokiosknew.domain.CustomerType;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class CustomerTypeRepository {
    private final static String TAG = CustomerTypeRepository.class.getSimpleName();
    private final KioskDatabase db;
    private final String[] columns = new String[]{
            KioskDatabase.CustomerTypeTable.ID,
            KioskDatabase.CustomerTypeTable.NAME};

    @Inject
    public CustomerTypeRepository(KioskDatabase db) {
        this.db = db;
    }

    public boolean replaceAll(List<CustomerType> customerTypes) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(KioskDatabase.CustomerTypeTable.TABLE_NAME, null, null);
            for (CustomerType type : customerTypes) {
                ContentValues values = new ContentValues();
                values.put(KioskDatabase.CustomerTypeTable.ID, type.getId());
                values.put(KioskDatabase.CustomerTypeTable.NAME, type.getName());
                wdb.insert(KioskDatabase.CustomerTypeTable.TABLE_NAME, null, values);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error when replacing all Customer type. Exception: " + e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public List<CustomerType> findAll() {
        List<CustomerType> customerTypes = new ArrayList<CustomerType>();
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        readableDatabase.beginTransaction();
        Cursor cursor = readableDatabase.query(KioskDatabase.CustomerTypeTable.TABLE_NAME,
                columns, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                customerTypes.add(buildCustomerType(cursor));
                cursor.moveToNext();
            }
            readableDatabase.setTransactionSuccessful();
            return customerTypes;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load all customer types from the database.", e);
            return new ArrayList<CustomerType>();
        } finally {
            cursor.close();
            readableDatabase.endTransaction();
        }
    }

    private CustomerType buildCustomerType(Cursor cursor) {
        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        return new CustomerType(id, name);
    }
}
