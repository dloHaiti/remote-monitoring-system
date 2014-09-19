package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.db.KioskDatabase.SponsorsTable;
import com.dlohaiti.dlokiosk.domain.Sponsor;
import com.dlohaiti.dlokiosk.domain.Sponsors;
import com.google.inject.Inject;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class SponsorRepository {
    private final static String TAG = SponsorRepository.class.getSimpleName();
    private final static String[] COLUMNS = new String[]
            {
                    SponsorsTable.ID,
                    SponsorsTable.NAME,
                    SponsorsTable.CONTACT_NAME,
                    SponsorsTable.DESCRIPTION
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
                values.put(SponsorsTable.ID, sponsor.id());
                values.put(SponsorsTable.NAME, sponsor.name());
                values.put(SponsorsTable.CONTACT_NAME, sponsor.contactName());
                values.put(SponsorsTable.DESCRIPTION, sponsor.description());

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
                sponsors.add(new Sponsor(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
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
}
