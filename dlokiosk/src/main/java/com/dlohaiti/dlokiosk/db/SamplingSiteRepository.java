package com.dlohaiti.dlokiosk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.domain.SamplingSite;
import com.google.inject.Inject;

import java.util.SortedSet;
import java.util.TreeSet;

public class SamplingSiteRepository {
    private final static String[] columns = new String[]{KioskDatabase.SamplingSitesTable.NAME};
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
            while(!c.isAfterLast()) {
                sites.add(new SamplingSite(c.getString(0)));
                c.moveToNext();
            }
            rdb.setTransactionSuccessful();
            return sites;
        } catch (Exception e) {
            Log.e(tag, "Problem loading sample sites.", e);
            return new TreeSet<SamplingSite>();
        } finally {
            rdb.endTransaction();
        }
    }

    //TODO: update client to read from DB
}
