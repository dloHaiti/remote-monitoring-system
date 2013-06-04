package com.dlohaiti.dlokiosk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.domain.Parameter;
import com.dlohaiti.dlokiosk.domain.SamplingSite;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.SortedSet;
import java.util.TreeSet;

import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class SamplingSiteParametersRepository {
    private final static String TAG = SamplingSiteParametersRepository.class.getSimpleName();
    private final KioskDatabase db;

    @Inject
    public SamplingSiteParametersRepository(KioskDatabase db) {
        this.db = db;
    }

    public SortedSet<Parameter> findBySamplingSite(SamplingSite samplingSite) {
        SortedSet<Parameter> parameters = new TreeSet<Parameter>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            int samplingSiteId = fetchSamplingSiteId(samplingSite, rdb);
            String[] parameterIds = fetchParameterIds(rdb, samplingSiteId);
            Cursor query = rdb.query(KioskDatabase.ParametersTable.TABLE_NAME, new String[]{KioskDatabase.ParametersTable.NAME, KioskDatabase.ParametersTable.UNIT_OF_MEASURE, KioskDatabase.ParametersTable.MINIMUM, KioskDatabase.ParametersTable.MAXIMUM, KioskDatabase.ParametersTable.IS_OK_NOT_OK}, whereIn(KioskDatabase.ParametersTable.ID, parameterIds.length), parameterIds, null, null, null);
            if(query.getCount() < 1) {
                throw new RuntimeException(String.format("No parameters found with ids: %s", StringUtils.join(parameterIds, ",")));
            }
            query.moveToFirst();
            while(!query.isAfterLast()) {
                parameters.add(new Parameter(query.getString(0), query.getString(1), query.getString(2), query.getString(3), Boolean.parseBoolean(query.getString(4))));
                query.moveToNext();
            }
            query.close();
            rdb.setTransactionSuccessful();
            return parameters;
        } catch (Exception e) {
            Log.e(TAG, String.format("Problem loading parameters for sampling site %s", samplingSite.getName()), e);
            return new TreeSet<Parameter>();
        } finally {
            rdb.endTransaction();
        }
    }

    private String[] fetchParameterIds(SQLiteDatabase rdb, int samplingSiteId) {
        Cursor sp = rdb.query(KioskDatabase.SamplingSitesParametersTable.TABLE_NAME, new String[] {KioskDatabase.SamplingSitesParametersTable.PARAMETER_ID}, where(KioskDatabase.SamplingSitesParametersTable.SITE_ID), matches(samplingSiteId), null, null, null);
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

    private int fetchSamplingSiteId(SamplingSite samplingSite, SQLiteDatabase rdb) {
        Cursor c = rdb.query(KioskDatabase.SamplingSitesTable.TABLE_NAME, new String[] {KioskDatabase.SamplingSitesTable.ID}, where(KioskDatabase.SamplingSitesTable.NAME), matches(samplingSite.getName()), null, null, null);
        if(c.getCount() != 1) {
            throw new RuntimeException(String.format("Expected 1 result; got %d.", c.getCount()));
        }
        c.moveToFirst();
        int samplingSiteId = c.getInt(0);
        c.close();
        return samplingSiteId;
    }

    private static String whereIn(String columnName, int numberOfItems) {
        String replacements = StringUtils.repeat("?", ",", numberOfItems);
        return String.format("%s IN (%s)", columnName, replacements);
    }
}
