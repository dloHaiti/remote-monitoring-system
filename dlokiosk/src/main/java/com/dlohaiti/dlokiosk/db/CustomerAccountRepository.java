package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.dlohaiti.dlokiosk.db.KioskDatabase.CustomerAccountsTable;
import static com.dlohaiti.dlokiosk.db.KioskDatabase.CustomerAccountsTable.TABLE_NAME;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class CustomerAccountRepository {
    private final static String TAG = CustomerAccountRepository.class.getSimpleName();
    private final static String[] COLUMNS = new String[]{
            CustomerAccountsTable.ID,
            CustomerAccountsTable.NAME,
            CustomerAccountsTable.CONTACT_NAME,
            CustomerAccountsTable.CUSTOMER_TYPE,
            CustomerAccountsTable.ADDRESS,
            CustomerAccountsTable.PHONE_NUMBER,
            CustomerAccountsTable.KIOSK_ID,
            CustomerAccountsTable.DUE_AMOUNT,
            CustomerAccountsTable.IS_SYNCED
    };
    private final KioskDatabase db;
    private SalesChannelRepository salesChannelRepository;

    @Inject
    public CustomerAccountRepository(KioskDatabase db, SalesChannelRepository salesChannelRepository) {
        this.db = db;
        this.salesChannelRepository = salesChannelRepository;
    }

    public boolean replaceAll(List<CustomerAccount> accounts) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(TABLE_NAME, null, null);
            for (CustomerAccount account : accounts) {
                ContentValues values = new ContentValues();
                values.put(CustomerAccountsTable.NAME, account.getName());
                values.put(CustomerAccountsTable.CONTACT_NAME, account.getContactName());
                values.put(CustomerAccountsTable.CUSTOMER_TYPE, account.getCustomerType());
                values.put(CustomerAccountsTable.ADDRESS, account.getAddress());
                values.put(CustomerAccountsTable.PHONE_NUMBER, account.getPhoneNumber());
                values.put(CustomerAccountsTable.KIOSK_ID, account.kioskId());
                values.put(CustomerAccountsTable.DUE_AMOUNT, account.getDueAmount());
                values.put(CustomerAccountsTable.IS_SYNCED, String.valueOf(true));
                wdb.insert(TABLE_NAME, null, values);
            }
            replaceAllSalesChannelCustomerAccountMap(wdb, accounts);
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error when replacing all Customer Accounts. Exception: " + e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    private void replaceAllSalesChannelCustomerAccountMap(SQLiteDatabase wdb, List<CustomerAccount> accounts) {
        wdb.delete(KioskDatabase.SalesChannelCustomerAccountsTable.TABLE_NAME, null, null);
        for (CustomerAccount account : accounts) {
            for (long channelId : account.channelIds()) {
                ContentValues values = new ContentValues();
                values.put(KioskDatabase.SalesChannelCustomerAccountsTable.CUSTOMER_ACCOUNT_ID, account.getId());
                values.put(KioskDatabase.SalesChannelCustomerAccountsTable.SALES_CHANNEL_ID, channelId);
                wdb.insert(KioskDatabase.SalesChannelCustomerAccountsTable.TABLE_NAME, null, values);
            }
        }
    }

    public SortedSet<CustomerAccount> findAll() {
        SortedSet<CustomerAccount> accounts = new TreeSet<CustomerAccount>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CustomerAccount account = buildCustomerAccount(cursor);
                accounts.add(account);
                account.withChannels(salesChannelRepository.findByCustomerId(account.getId()));
                cursor.moveToNext();
            }
            rdb.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Failed to load delivery agents from database", e);
        } finally {
            cursor.close();
            rdb.endTransaction();
        }
        return accounts;
    }

    public CustomerAccount findById(Long id) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor cursor = rdb.query(TABLE_NAME, COLUMNS, where(CustomerAccountsTable.ID), matches(id), null, null, null);
            if (cursor.getCount() != 1) {
                throw new RecordNotFoundException();
            }
            cursor.moveToFirst();
            CustomerAccount account = buildCustomerAccount(cursor);
            cursor.close();
            rdb.setTransactionSuccessful();
            return account;
        } catch (Exception e) {
            Log.e(TAG, String.format("Could not find Customer Account with id %s.", id), e);
            return null;
        } finally {
            rdb.endTransaction();
        }
    }

    private CustomerAccount buildCustomerAccount(Cursor cursor) {
        return new CustomerAccount(cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getLong(6),
                cursor.getInt(7),
                Boolean.valueOf(cursor.getString(8)));
    }

    public boolean save(CustomerAccount account) {
        SQLiteDatabase wdb = db.getWritableDatabase();

        wdb.beginTransaction();
        try {
            long accountId;
            ContentValues values = new ContentValues();
            if (account.getId() == null) {
//                values.put(CustomerAccountsTable.NAME, account.getName());
//                values.put(KioskDatabase.ReadingsTable.CREATED_DATE, kioskDate.getFormat().format(reading.getCreatedDate()));
//                values.put(KioskDatabase.ReadingsTable.IS_SYNCED, String.valueOf(reading.isSynced()));
//                readingId = wdb.insert(KioskDatabase.ReadingsTable.TABLE_NAME, null, values);
            } else {
                accountId = account.getId();
                values.put(CustomerAccountsTable.DUE_AMOUNT, String.valueOf(account.getDueAmount()));
                values.put(KioskDatabase.CustomerAccountsTable.IS_SYNCED, String.valueOf(false));
                wdb.update(KioskDatabase.CustomerAccountsTable.TABLE_NAME, values, "id " + "=" + accountId, null);
            }
//            for (Measurement m : reading.getMeasurements()) {
//                ContentValues cv = new ContentValues();
//                cv.put(KioskDatabase.MeasurementsTable.PARAMETER_NAME, m.getParameterName());
//                cv.put(KioskDatabase.MeasurementsTable.VALUE, m.getValue().toString());
//                cv.put(KioskDatabase.MeasurementsTable.READING_ID, readingId);
//                if (m.getId() == null) {
//                    wdb.insert(KioskDatabase.MeasurementsTable.TABLE_NAME, null, cv);
//                } else {
//                    wdb.update(KioskDatabase.MeasurementsTable.TABLE_NAME, cv, "getId " + "=" + m.getId(), null);
//                }
//            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save customer account to database.", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public List<CustomerAccount> getNonSyncAccounts() {
        List<CustomerAccount> accounts = new ArrayList<CustomerAccount>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.query(TABLE_NAME, COLUMNS, where(CustomerAccountsTable.IS_SYNCED), matches(String.valueOf(false)), null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CustomerAccount account = buildCustomerAccount(cursor);
                accounts.add(account);
                account.withChannels(salesChannelRepository.findByCustomerId(account.getId()));
                cursor.moveToNext();
            }
            rdb.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Failed to load customer account from database", e);
        } finally {
            cursor.close();
            rdb.endTransaction();
        }
        return accounts;
    }

    public boolean synced(CustomerAccount account) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            long accountId;
            ContentValues values = new ContentValues();
            accountId = account.getId();
            values.put(KioskDatabase.CustomerAccountsTable.IS_SYNCED, String.valueOf(true));
            wdb.update(KioskDatabase.CustomerAccountsTable.TABLE_NAME, values, "id " + "=" + accountId, null);
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save customer account to database.", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public boolean isNotEmpty() {
       return this.getNonSyncAccounts().size() > 0;
    }
}
