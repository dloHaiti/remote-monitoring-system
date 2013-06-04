package com.dlohaiti.dlokiosk.db;

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
    private final static String[] columns = new String[]{
            KioskDatabase.SamplingSitesTable.ID,
            KioskDatabase.SamplingSitesTable.NAME
    };
    private final String tag = getClass().getSimpleName();
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
            Cursor c = rdb.query(KioskDatabase.SamplingSitesTable.TABLE_NAME, columns, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                sites.add(new SamplingSite(c.getInt(0), c.getString(1)));
                c.moveToNext();
            }
            c.close();
            rdb.setTransactionSuccessful();
            return sites;
        } catch (Exception e) {
            Log.e(tag, "Problem loading sampling sites.", e);
            return new TreeSet<SamplingSite>();
        } finally {
            rdb.endTransaction();
        }
    }

    public SamplingSite findById(int id) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor c = rdb.query(KioskDatabase.SamplingSitesTable.TABLE_NAME, columns, where(KioskDatabase.SamplingSitesTable.ID), matches(id), null, null, null);
            if (c.getCount() != 1) {
                throw new RecordNotFoundException();
            }
            c.moveToFirst();
            SamplingSite samplingSite = new SamplingSite(c.getInt(0), c.getString(1));
            c.close();
            rdb.setTransactionSuccessful();
            return samplingSite;
        } catch (Exception e) {
            Log.e(tag, String.format("Could not find Sampling Site with id %d.", id), e);
            return new SamplingSite(id, "");
        } finally {
            rdb.endTransaction();
        }
    }

    public SamplingSite findByName(String name) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor c = rdb.query(KioskDatabase.SamplingSitesTable.TABLE_NAME, columns, where(KioskDatabase.SamplingSitesTable.NAME), matches(name), null, null, null);
            if (c.getCount() != 1) {
                throw new RecordNotFoundException();
            }
            c.moveToFirst();
            SamplingSite samplingSite = new SamplingSite(c.getInt(0), c.getString(1));
            c.close();
            rdb.setTransactionSuccessful();
            return samplingSite;
        } catch (Exception e) {
            Log.e(tag, String.format("Could not find Sampling Site with name %s.", name), e);
            return new SamplingSite("");
        } finally {
            rdb.endTransaction();
        }
    }
}
