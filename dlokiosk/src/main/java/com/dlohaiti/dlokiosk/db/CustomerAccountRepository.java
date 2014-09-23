package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.dlohaiti.dlokiosk.domain.SalesChannel;
import com.dlohaiti.dlokiosk.domain.Sponsor;
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
            CustomerAccountsTable.GPS_COORDINATES,
            CustomerAccountsTable.KIOSK_ID,
            CustomerAccountsTable.DUE_AMOUNT,
            CustomerAccountsTable.IS_SYNCED
    };
    private final KioskDatabase db;
    private SalesChannelRepository salesChannelRepository;
    private SponsorRepository sponsorRepository;

    @Inject
    public CustomerAccountRepository(KioskDatabase db, SalesChannelRepository salesChannelRepository,SponsorRepository sponsorRepository) {
        this.db = db;
        this.salesChannelRepository = salesChannelRepository;
        this.sponsorRepository=sponsorRepository;
    }

    public boolean replaceAll(List<CustomerAccount> accounts) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(TABLE_NAME, null, null);
            for (CustomerAccount account : accounts) {
                ContentValues values = new ContentValues();
                values.put(CustomerAccountsTable.ID, account.getId());
                values.put(CustomerAccountsTable.NAME, account.getName());
                values.put(CustomerAccountsTable.CONTACT_NAME, account.getContactName());
                values.put(CustomerAccountsTable.CUSTOMER_TYPE, account.getCustomerTypeId());
                values.put(CustomerAccountsTable.ADDRESS, account.getAddress());
                values.put(CustomerAccountsTable.PHONE_NUMBER, account.getPhoneNumber());
                values.put(CustomerAccountsTable.GPS_COORDINATES, account.getGpsCoordinates());
                values.put(CustomerAccountsTable.KIOSK_ID, account.kioskId());
                values.put(CustomerAccountsTable.DUE_AMOUNT, account.getDueAmount());
                values.put(CustomerAccountsTable.IS_SYNCED, String.valueOf(true));
                wdb.insert(TABLE_NAME, null, values);
            }
            replaceAllSalesChannelCustomerAccountMap(wdb, accounts);
            replaceAllSponsorCustomerAccountMap(wdb, accounts);
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error when replacing all Customer Accounts. Exception: " + e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    private void replaceAllSponsorCustomerAccountMap(SQLiteDatabase wdb, List<CustomerAccount> accounts) {
        wdb.delete(KioskDatabase.SponsorCustomerAccountsTable.TABLE_NAME, null, null);
        for (CustomerAccount account : accounts) {
            for (long channelId : account.getSponsorIds()) {
                ContentValues values = new ContentValues();
                values.put(KioskDatabase.SponsorCustomerAccountsTable.CUSTOMER_ACCOUNT_ID, account.getId());
                values.put(KioskDatabase.SponsorCustomerAccountsTable.SPONSOR_ID, channelId);
                wdb.insert(KioskDatabase.SponsorCustomerAccountsTable.TABLE_NAME, null, values);
            }
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
                account.withSponsors(sponsorRepository.findByCustomerId(account.getId()));
                account.withChannels(salesChannelRepository.findByCustomerId(account.getId()));
                accounts.add(account);
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

    public CustomerAccount findById(String id) {
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
        return new CustomerAccount(cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getLong(7),
                cursor.getInt(8),
                Boolean.valueOf(cursor.getString(9))).setGpsCoordinates(cursor.getString(6));
    }

    public boolean save(CustomerAccount account) {
        SQLiteDatabase wdb = db.getWritableDatabase();

        wdb.beginTransaction();
        try {
            String accountId;
            ContentValues values = new ContentValues();
            if (account.getId() == null) {
//                values.put(CustomerAccountsTable.NAME, account.getName());
//                values.put(KioskDatabase.ReadingsTable.CREATED_DATE, kioskDate.getFormat().format(reading.getCreatedDate()));
//                values.put(KioskDatabase.ReadingsTable.IS_SYNCED, String.valueOf(reading.isSynced()));
//                readingId = wdb.insert(KioskDatabase.ReadingsTable.TABLE_NAME, null, values);
            } else {
                accountId = account.getId();
                values.put(CustomerAccountsTable.NAME, String.valueOf(account.getName()));
                values.put(CustomerAccountsTable.CONTACT_NAME, String.valueOf(account.getContactName()));
                values.put(CustomerAccountsTable.PHONE_NUMBER, String.valueOf(account.getPhoneNumber()));
                values.put(CustomerAccountsTable.GPS_COORDINATES, account.getGpsCoordinates());
                values.put(CustomerAccountsTable.ADDRESS, account.getAddress());
                values.put(CustomerAccountsTable.CUSTOMER_TYPE, String.valueOf(account.getCustomerTypeId()));
                values.put(CustomerAccountsTable.DUE_AMOUNT, String.valueOf(account.getDueAmount()));
                values.put(KioskDatabase.CustomerAccountsTable.IS_SYNCED, String.valueOf(false));
                wdb.update(KioskDatabase.CustomerAccountsTable.TABLE_NAME, values, "id " + "='" + accountId +"'", null);
                replaceSalesChannelCustomerAccountMap(account, wdb);
                replaceSponsorCustomerAccountMap(wdb, account);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save customer account to database.", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    private void replaceSalesChannelCustomerAccountMap(CustomerAccount account, SQLiteDatabase wdb) {
        String accountId = account.getId() ;
        wdb.delete(KioskDatabase.SalesChannelCustomerAccountsTable.TABLE_NAME,where(KioskDatabase.SalesChannelCustomerAccountsTable.CUSTOMER_ACCOUNT_ID),matches(accountId));
        for(SalesChannel sc:account.getChannels()){
            ContentValues cv = new ContentValues();
            cv.put(KioskDatabase.SalesChannelCustomerAccountsTable.CUSTOMER_ACCOUNT_ID,accountId);
            cv.put(KioskDatabase.SalesChannelCustomerAccountsTable.SALES_CHANNEL_ID,sc.getId());
            wdb.insert(KioskDatabase.SalesChannelCustomerAccountsTable.TABLE_NAME,null,cv);
        }
    }

    private void replaceSponsorCustomerAccountMap(SQLiteDatabase wdb, CustomerAccount account) {
        wdb.delete(KioskDatabase.SponsorCustomerAccountsTable.TABLE_NAME,where(KioskDatabase.SponsorCustomerAccountsTable.CUSTOMER_ACCOUNT_ID),matches(account.getId()));
        for(Sponsor s:account.sponsors()){
            ContentValues cv = new ContentValues();
            cv.put(KioskDatabase.SponsorCustomerAccountsTable.CUSTOMER_ACCOUNT_ID,account.getId());
            cv.put(KioskDatabase.SponsorCustomerAccountsTable.SPONSOR_ID,s.id());
            wdb.insert(KioskDatabase.SponsorCustomerAccountsTable.TABLE_NAME,null,cv);
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
                account.withSponsors(sponsorRepository.findByCustomerId(account.getId()));
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
            String accountId;
            ContentValues values = new ContentValues();
            accountId = account.getId();
            values.put(KioskDatabase.CustomerAccountsTable.IS_SYNCED, String.valueOf(true));
            wdb.update(KioskDatabase.CustomerAccountsTable.TABLE_NAME, values, "id " + "='" + accountId+"'", null);
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
