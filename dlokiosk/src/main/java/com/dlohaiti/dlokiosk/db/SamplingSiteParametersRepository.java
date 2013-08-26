package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.ParameterSamplingSites;
import com.dlohaiti.dlokiosk.domain.Parameter;
import com.dlohaiti.dlokiosk.domain.SamplingSite;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class SamplingSiteParametersRepository {
    private final static String TAG = SamplingSiteParametersRepository.class.getSimpleName();
    private final KioskDatabase db;
    private final SamplingSiteRepository samplingSiteRepository;
    private final String[] COLUMNS = new String[] {
            KioskDatabase.ParametersTable.NAME,
            KioskDatabase.ParametersTable.UNIT_OF_MEASURE,
            KioskDatabase.ParametersTable.MINIMUM,
            KioskDatabase.ParametersTable.MAXIMUM,
            KioskDatabase.ParametersTable.IS_OK_NOT_OK,
            KioskDatabase.ParametersTable.PRIORITY
    };

    @Inject
    public SamplingSiteParametersRepository(KioskDatabase db, SamplingSiteRepository samplingSiteRepository) {
        this.db = db;
        this.samplingSiteRepository = samplingSiteRepository;
    }

    public SortedSet<Parameter> findBySamplingSite(SamplingSite samplingSite) {
        SortedSet<Parameter> parameters = new TreeSet<Parameter>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            int samplingSiteId = fetchSamplingSiteId(samplingSite, rdb);
            String[] parameterIds = fetchParameterIds(rdb, samplingSiteId);
            Cursor query = rdb.query(KioskDatabase.ParametersTable.TABLE_NAME, COLUMNS, whereIn(KioskDatabase.ParametersTable.ID, parameterIds.length), parameterIds, null, null, null);
            if(query.getCount() < 1) {
                throw new RuntimeException(String.format("No parameters found with ids: %s", StringUtils.join(parameterIds, ",")));
            }
            query.moveToFirst();
            while(!query.isAfterLast()) {
                String name = query.getString(0);
                String unitOfMeasure = query.getString(1);
                String minimum = query.getString(2);
                String maximum = query.getString(3);
                boolean isOkNotOk = Boolean.parseBoolean(query.getString(4));
                Integer priority = query.getInt(5);
                parameters.add(new Parameter(name, unitOfMeasure, minimum, maximum, isOkNotOk, priority));
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

    public boolean replaceAll(List<ParameterSamplingSites> samplingSiteParameters) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(KioskDatabase.SamplingSitesParametersTable.TABLE_NAME, null, null);
            wdb.delete(KioskDatabase.ParametersTable.TABLE_NAME, null, null);
            wdb.delete(KioskDatabase.SamplingSitesTable.TABLE_NAME, null, null);
            for(ParameterSamplingSites pss : samplingSiteParameters) {
                ContentValues parameterValues = new ContentValues();
                Parameter parameter = pss.getParameter();
                String parameterName = parameter.getName();
                parameterValues.put(KioskDatabase.ParametersTable.NAME, parameterName);
                parameterValues.put(KioskDatabase.ParametersTable.UNIT_OF_MEASURE, parameter.getUnitOfMeasure());
                if(parameter.getMinimum() != null) {
                    parameterValues.put(KioskDatabase.ParametersTable.MINIMUM, String.valueOf(parameter.getMinimum()));
                }
                if(parameter.getMaximum() != null) {
                    parameterValues.put(KioskDatabase.ParametersTable.MAXIMUM, String.valueOf(parameter.getMaximum()));
                }
                parameterValues.put(KioskDatabase.ParametersTable.IS_OK_NOT_OK, String.valueOf(parameter.isOkNotOk()));
                parameterValues.put(KioskDatabase.ParametersTable.PRIORITY, parameter.getPriority());
                long parameterId = wdb.insert(KioskDatabase.ParametersTable.TABLE_NAME, null, parameterValues);
                if(parameterId == -1) {
                    Log.e(TAG, String.format("Error inserting Parameter[%s]", parameterName));
                    break;
                }

                for(SamplingSite site : pss.getSamplingSites()) {
                    SamplingSite existingSite = samplingSiteRepository.findOrCreateByName(site.getName());
                    ContentValues parameterSites = new ContentValues();
                    parameterSites.put(KioskDatabase.SamplingSitesParametersTable.PARAMETER_ID, parameterId);
                    parameterSites.put(KioskDatabase.SamplingSitesParametersTable.SITE_ID, existingSite.getId());
                    long rowId = wdb.insert(KioskDatabase.SamplingSitesParametersTable.TABLE_NAME, null, parameterSites);
                    if(rowId == -1) {
                        Log.e(TAG, String.format("Error inserting SamplingSite[%s]-Parameter[%s] relationship", site.getName(), parameterName));
                    }
                }
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to replace parameters, sampling sites, and relationships", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }
}
