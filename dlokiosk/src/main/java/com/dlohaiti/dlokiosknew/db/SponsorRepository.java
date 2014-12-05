package com.dlohaiti.dlokiosknew.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.dlohaiti.dlokiosknew.db.KioskDatabase.SponsorsTable;
import com.dlohaiti.dlokiosknew.domain.CustomerAccount;
import com.dlohaiti.dlokiosknew.domain.Sponsor;
import com.dlohaiti.dlokiosknew.domain.Sponsors;
import com.google.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import static com.dlohaiti.dlokiosknew.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosknew.db.KioskDatabaseUtils.where;
import static com.dlohaiti.dlokiosknew.db.KioskDatabaseUtils.whereWithLike;
import static java.lang.String.format;

public class SponsorRepository {
    private final static String TAG = SponsorRepository.class.getSimpleName();
    private final static String[] COLUMNS = new String[]
            {
                    SponsorsTable.ID,
                    SponsorsTable.NAME,
                    SponsorsTable.CONTACT_NAME,
                    SponsorsTable.PHONE_NUMBER,
                    SponsorsTable.IS_SYNCED
            };
    private final KioskDatabase db;

    @Inject
    public SponsorRepository(KioskDatabase db) {
        this.db = db;

    }

    public boolean replaceAll(List<Sponsor> sponsors) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(SponsorsTable.TABLE_NAME, null, null);
            for (Sponsor sponsor : sponsors) {
                ContentValues values = new ContentValues();
                values.put(SponsorsTable.ID, sponsor.getId());
                values.put(SponsorsTable.NAME, sponsor.getName());
                values.put(SponsorsTable.CONTACT_NAME, sponsor.getContactName());
                values.put(SponsorsTable.PHONE_NUMBER, sponsor.getPhoneNumber());
                values.put(SponsorsTable.IS_SYNCED, String.valueOf(true));
                wdb.insert(SponsorsTable.TABLE_NAME, null, values);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to replace all sponsors. " + e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public Sponsors findAll() {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.query(SponsorsTable.TABLE_NAME, COLUMNS, null, null, null, null, null);
        return readAll(rdb, cursor);
    }

    private Sponsors readAll(SQLiteDatabase rdb, Cursor cursor) {
        SortedSet<Sponsor> sponsors = new TreeSet<Sponsor>();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Sponsor sponsor = new Sponsor(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), Boolean.valueOf(cursor.getString(4)));
                sponsors.add(sponsor);
                cursor.moveToNext();
            }
            rdb.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Failed to load Sponsors from database", e);
        } finally {
            cursor.close();
            rdb.endTransaction();
        }
        return new Sponsors(sponsors);
    }

    public Sponsors findByCustomerId(String customerId) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.rawQuery(format(
                        "SELECT %s FROM " +
                                "%s s, " +
                                "%s map " +
                                "WHERE map.%s = ? and map.%s = s.%s " +
                                "ORDER BY s.%s",
                        StringUtils.join(COLUMNS, ","),
                        SponsorsTable.TABLE_NAME,
                        KioskDatabase.SponsorCustomerAccountsTable.TABLE_NAME,
                        KioskDatabase.SponsorCustomerAccountsTable.CUSTOMER_ACCOUNT_ID,
                        KioskDatabase.SponsorCustomerAccountsTable.SPONSOR_ID,
                        KioskDatabase.SponsorsTable.ID,
                        KioskDatabase.SponsorsTable.NAME),
                new String[]{String.valueOf(customerId)});
        return readAll(rdb, cursor);
    }

    public Sponsor findById(String id) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor cursor = rdb.query(SponsorsTable.TABLE_NAME, COLUMNS, where(SponsorsTable.ID), matches(id), null, null, null);
            if (cursor.getCount() != 1) {
                throw new RecordNotFoundException();
            }
            cursor.moveToFirst();
            Sponsor sponsor = new Sponsor(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), Boolean.valueOf(cursor.getString(4)));
            cursor.close();
            rdb.setTransactionSuccessful();
            return sponsor;
        } catch (Exception e) {
            Log.e(TAG, String.format("Could not find sponsor with id %s.", id), e);
            return null;
        } finally {
            rdb.endTransaction();
        }
    }

    public boolean save(Sponsor sponsor) {
        SQLiteDatabase wdb = db.getWritableDatabase();

        wdb.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(SponsorsTable.NAME, String.valueOf(sponsor.getName()));
            values.put(SponsorsTable.CONTACT_NAME, String.valueOf(sponsor.getContactName()));
            values.put(SponsorsTable.PHONE_NUMBER, String.valueOf(sponsor.getPhoneNumber()));
            values.put(KioskDatabase.SponsorsTable.IS_SYNCED, String.valueOf(false));

            if (TextUtils.isEmpty(sponsor.getId())) {
                String generatedId = UUID.randomUUID().toString();
                values.put(SponsorsTable.ID, generatedId);
                sponsor.setId(generatedId);
                wdb.insert(KioskDatabase.SponsorsTable.TABLE_NAME, null, values);
            } else {
                String sponsorId = sponsor.getId();
                wdb.update(KioskDatabase.SponsorsTable.TABLE_NAME, values, "id " + "='" + sponsorId + "'", null);
            }
            replaceSponsorCustomerAccountMap(wdb, sponsor);
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save sponsors to database.", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public boolean synced(Sponsor sponsor) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            String sponsorId = sponsor.getId();
            values.put(SponsorsTable.IS_SYNCED, String.valueOf(true));
            wdb.update(SponsorsTable.TABLE_NAME, values, "id " + "='" + sponsorId + "'", null);
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save sponsor to database.", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    private void replaceSponsorCustomerAccountMap(SQLiteDatabase wdb, Sponsor sponsor) {
        wdb.delete(KioskDatabase.SponsorCustomerAccountsTable.TABLE_NAME, where(KioskDatabase.SponsorCustomerAccountsTable.SPONSOR_ID), matches(sponsor.getId()));
        for (CustomerAccount account : sponsor.customerAccounts()) {
            ContentValues cv = new ContentValues();
            cv.put(KioskDatabase.SponsorCustomerAccountsTable.CUSTOMER_ACCOUNT_ID, account.getId());
            cv.put(KioskDatabase.SponsorCustomerAccountsTable.SPONSOR_ID, sponsor.getId());
            wdb.insert(KioskDatabase.SponsorCustomerAccountsTable.TABLE_NAME, null, cv);
        }
    }

    public boolean isNotEmpty() {
        return this.getNonSyncSponsors().size() > 0;
    }

    public List<Sponsor> getNonSyncSponsors() {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.query(KioskDatabase.SponsorsTable.TABLE_NAME, COLUMNS, where(KioskDatabase.SponsorsTable.IS_SYNCED), matches(String.valueOf(false)), null, null, null);
        return readAll(rdb, cursor);
    }

    public Sponsor findByName(String name) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor cursor = rdb.query(SponsorsTable.TABLE_NAME, COLUMNS, whereWithLike(SponsorsTable.NAME), matches(name), null, null, null);
            if (cursor.getCount() != 1) {
                throw new RecordNotFoundException();
            }
            cursor.moveToFirst();
            Sponsor sponsor = new Sponsor(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), Boolean.valueOf(cursor.getString(4)));
            cursor.close();
            rdb.setTransactionSuccessful();
            return sponsor;
        } catch (Exception e) {
            return null;
        } finally {
            rdb.endTransaction();
        }
    }
}
