package com.dlohaiti.dlokiosk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.domain.SampleSite;
import com.google.inject.Inject;

import java.util.SortedSet;
import java.util.TreeSet;

public class SampleSiteRepository {
    private final static String[] columns = new String[]{KioskDatabase.SamplingSitesTable.NAME};
    private final String tag = getClass().getSimpleName();
    private final KioskDatabase db;

    @Inject
    public SampleSiteRepository(KioskDatabase db) {
        this.db = db;
    }

    public SortedSet<SampleSite> list() {
        SortedSet<SampleSite> sites = new TreeSet<SampleSite>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor c = rdb.query(KioskDatabase.SamplingSitesTable.TABLE_NAME, columns, null, null, null, null, null);
            c.moveToFirst();
            while(!c.isAfterLast()) {
                sites.add(new SampleSite(c.getString(0)));
                c.moveToNext();
            }
            rdb.setTransactionSuccessful();
            return sites;
        } catch (Exception e) {
            Log.e(tag, "Problem loading sample sites.", e);
            return new TreeSet<SampleSite>();
        } finally {
            rdb.endTransaction();
        }
    }

    //TODO: activity with list view of measurements
    //TODO: repository for said activity that gets the parameter info
    //TODO: measurements saved to DB
    //TODO: update client to read from DB
}
