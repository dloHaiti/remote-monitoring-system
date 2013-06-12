package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.domain.SamplingSite;
import com.google.inject.Inject;

import java.util.SortedSet;
import java.util.TreeSet;

import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class SamplingSiteRepository {
    private final static String TAG = SamplingSiteRepository.class.getSimpleName();
    private final static String[] COLUMNS = new String[]{
            KioskDatabase.SamplingSitesTable.ID,
            KioskDatabase.SamplingSitesTable.NAME
    };
    private final KioskDatabase db;

    @Inject
    public SamplingSiteRepository(KioskDatabase db) {
        this.db = db;
    }

    public SortedSet<SamplingSite> list() {
        SortedSet<SamplingSite> sites = new TreeSet<SamplingSite>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor c = rdb.query(KioskDatabase.SamplingSitesTable.TABLE_NAME, COLUMNS, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                sites.add(new SamplingSite(c.getLong(0), c.getString(1)));
                c.moveToNext();
            }
            c.close();
            rdb.setTransactionSuccessful();
            return sites;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load sampling sites from database.", e);
            return new TreeSet<SamplingSite>();
        } finally {
            rdb.endTransaction();
        }
    }

    public SamplingSite findByName(String name) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor c = rdb.query(KioskDatabase.SamplingSitesTable.TABLE_NAME, COLUMNS, where(KioskDatabase.SamplingSitesTable.NAME), matches(name), null, null, null);
            if (c.getCount() != 1) {
                throw new RecordNotFoundException();
            }
            c.moveToFirst();
            SamplingSite samplingSite = new SamplingSite(c.getLong(0), c.getString(1));
            c.close();
            rdb.setTransactionSuccessful();
            return samplingSite;
        } catch (Exception e) {
            Log.e(TAG, String.format("Could not find Sampling Site with name %s.", name), e);
            return new SamplingSite(null);
        } finally {
            rdb.endTransaction();
        }
    }

    public SamplingSite findOrCreateByName(String name) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        Cursor c = wdb.query(KioskDatabase.SamplingSitesTable.TABLE_NAME, COLUMNS, where(KioskDatabase.SamplingSitesTable.NAME), matches(name), null, null, null);
        try {
            if(c.moveToFirst()) {
                wdb.setTransactionSuccessful();
                return new SamplingSite(c.getLong(0), c.getString(1));
            }
            ContentValues values = new ContentValues();
            values.put(KioskDatabase.SamplingSitesTable.NAME, name);
            long samplingSiteId = wdb.insert(KioskDatabase.SamplingSitesTable.TABLE_NAME, null, values);
            wdb.setTransactionSuccessful();
            return new SamplingSite(samplingSiteId, name);
        } catch (Exception e) {
            Log.e(TAG, "Failed to find or create sampling site with name " + name, e);
            return new SamplingSite(null);
        } finally {
            c.close();
            wdb.endTransaction();
        }
    }
}
