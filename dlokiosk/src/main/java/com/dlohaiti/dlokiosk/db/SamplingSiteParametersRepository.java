package com.dlohaiti.dlokiosk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.domain.Parameter;
import com.dlohaiti.dlokiosk.domain.SampleSite;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.SortedSet;
import java.util.TreeSet;

public class SamplingSiteParametersRepository {
    private final String tag = getClass().getSimpleName();
    private final KioskDatabase db;

    @Inject
    public SamplingSiteParametersRepository(KioskDatabase db) {
        this.db = db;
    }

    public SortedSet<Parameter> findBySampleSite(SampleSite sampleSite) {
        SortedSet<Parameter> parameters = new TreeSet<Parameter>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            int sampleSiteId = fetchSamplingSiteId(sampleSite, rdb);
            String[] parameterIds = fetchParameterIds(rdb, sampleSiteId);
            Cursor query = rdb.query(KioskDatabase.ParametersTable.TABLE_NAME, new String[]{KioskDatabase.ParametersTable.NAME, KioskDatabase.ParametersTable.UNIT_OF_MEASURE, KioskDatabase.ParametersTable.MINIMUM, KioskDatabase.ParametersTable.MAXIMUM}, whereIn(KioskDatabase.ParametersTable.ID, parameterIds.length), parameterIds, null, null, null);
            if(query.getCount() < 1) {
                throw new RuntimeException(String.format("No parameters found with ids: %s", StringUtils.join(parameterIds, ",")));
            }
            query.moveToFirst();
            while(!query.isAfterLast()) {
                parameters.add(new Parameter(query.getString(0), query.getString(1), query.getString(2), query.getString(3)));
                query.moveToNext();
            }
            query.close();
            rdb.setTransactionSuccessful();
            return parameters;
        } catch (Exception e) {
            Log.e(tag, String.format("Problem loading parameters for sample site %s", sampleSite.getName()), e);
            return new TreeSet<Parameter>();
        } finally {
            rdb.endTransaction();
        }
    }

    private String[] fetchParameterIds(SQLiteDatabase rdb, int sampleSiteId) {
        Cursor sp = rdb.query(KioskDatabase.SamplingSitesParametersTable.TABLE_NAME, new String[] {KioskDatabase.SamplingSitesParametersTable.PARAMETER_ID}, where(KioskDatabase.SamplingSitesParametersTable.SITE_ID), matches(sampleSiteId), null, null, null);
        if(sp.getCount() < 1) {
            throw new RuntimeException(String.format("Expected at least 1 parameter for Sampling Site; got %d", sp.getCount()));
        }
        sp.moveToFirst();
        //TODO
        String[] parameterIds = new String[sp.getCount()];
        int i = 0;
        while(!sp.isAfterLast()) {
            parameterIds[i] = sp.getString(0);
            sp.moveToNext();
            i++;
        }
        sp.close();
        return parameterIds;
    }

    private int fetchSamplingSiteId(SampleSite sampleSite, SQLiteDatabase rdb) {
        Cursor c = rdb.query(KioskDatabase.SamplingSitesTable.TABLE_NAME, new String[] {KioskDatabase.SamplingSitesTable.ID}, where(KioskDatabase.SamplingSitesTable.NAME), matches(sampleSite.getName()), null, null, null);
        if(c.getCount() != 1) {
            throw new RuntimeException(String.format("Expected 1 result; got %d.", c.getCount()));
        }
        c.moveToFirst();
        int sampleSiteId = c.getInt(0);
        c.close();
        return sampleSiteId;
    }

    private static String[] matches(int match) {
        return matches(String.valueOf(match));
    }

    private static String[] matches(String match) {
        return new String[] {match};
    }

    private static String where(String columnName) {
        return String.format("%s=?", columnName);
    }

    private static String whereIn(String columnName, int numberOfItems) {
        String replacements = StringUtils.repeat("?", ",", numberOfItems);
        return String.format("%s IN (%s)", columnName, replacements);
    }
}
