package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.google.inject.Inject;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.dlohaiti.dlokiosk.db.KioskDatabase.CustomerAccountsTable.TABLE_NAME;


public class CustomerAccountRepository {
    private final static String TAG = CustomerAccountRepository.class.getSimpleName();
    private final static String[] COLUMNS = new String[]{
            KioskDatabase.CustomerAccountsTable.ID,
            KioskDatabase.CustomerAccountsTable.NAME,
            KioskDatabase.CustomerAccountsTable.ADDRESS,
            KioskDatabase.CustomerAccountsTable.PHONE_NUMBER,
            KioskDatabase.CustomerAccountsTable.KIOSK_ID,
    };
    private final KioskDatabase db;

    @Inject
    public CustomerAccountRepository(KioskDatabase db) {
        this.db = db;
    }

    public boolean replaceAll(List<CustomerAccount> accounts) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(TABLE_NAME, null, null);
            for (CustomerAccount account : accounts) {
                ContentValues values = new ContentValues();
                values.put(KioskDatabase.CustomerAccountsTable.NAME, account.name());
                values.put(KioskDatabase.CustomerAccountsTable.ADDRESS, account.address());
                values.put(KioskDatabase.CustomerAccountsTable.PHONE_NUMBER, account.phoneNumber());
                values.put(KioskDatabase.CustomerAccountsTable.KIOSK_ID, account.kioskId());

                wdb.insert(TABLE_NAME, null, values);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public SortedSet<CustomerAccount> findAll() {
        SortedSet<CustomerAccount> agents = new TreeSet<CustomerAccount>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                agents.add(new CustomerAccount(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getLong(4)
                ));
                cursor.moveToNext();
            }
            rdb.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Failed to load delivery agents from database", e);
        } finally {
            cursor.close();
            rdb.endTransaction();
        }
        return agents;
    }
}
