package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dlohaiti.dlokiosk.domain.FlowMeterReading;
import com.dlohaiti.dlokiosk.domain.SalesChannel;
import com.dlohaiti.dlokiosk.domain.SamplingSite;
import com.google.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;
import static java.lang.String.format;

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
            if (c.moveToFirst()) {
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

    public ArrayList<FlowMeterReading> ListAllFlowMeterSites(){
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.rawQuery(format(
                        "SELECT %s FROM " +
                                "%s,%s,%s " +
                                " WHERE %s.%s = %s" +
                                " AND %s.%s=%s.id" +
                                " AND %s.id=%s.%s" +
                                " ORDER BY %s.%s",
                        getFieldNames(),
                        KioskDatabase.SamplingSitesTable.TABLE_NAME,
                        KioskDatabase.SamplingSitesParametersTable.TABLE_NAME,
                        KioskDatabase.ParametersTable.TABLE_NAME,

                        KioskDatabase.SamplingSitesParametersTable.TABLE_NAME,
                        KioskDatabase.SamplingSitesParametersTable.PARAMETER_ID,
                        parameterQuery(),
                        KioskDatabase.SamplingSitesParametersTable.TABLE_NAME,
                        KioskDatabase.SamplingSitesParametersTable.SITE_ID,
                        KioskDatabase.SamplingSitesTable.TABLE_NAME,
                        KioskDatabase.ParametersTable.TABLE_NAME,
                        KioskDatabase.SamplingSitesParametersTable.TABLE_NAME,
                        KioskDatabase.SamplingSitesParametersTable.PARAMETER_ID,
                        KioskDatabase.SamplingSitesTable.TABLE_NAME,
                        KioskDatabase.SamplingSitesTable.NAME),null);
        ArrayList<FlowMeterReading> flowMeterReadings=new ArrayList<FlowMeterReading>();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                flowMeterReadings.add(new FlowMeterReading(cursor.getLong(0),cursor.getLong(2), cursor.getString(1),cursor.getString(3), ""));
                cursor.moveToNext();
            }
            rdb.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Failed to Flow reading from database", e);
        } finally {
            cursor.close();
            rdb.endTransaction();
        }
        return flowMeterReadings;
    }

    private String getFieldNames() {
        return  KioskDatabase.SamplingSitesTable.TABLE_NAME +".*,"+KioskDatabase.SamplingSitesParametersTable.PARAMETER_ID +","+KioskDatabase.ParametersTable.TABLE_NAME+"."+KioskDatabase.ParametersTable.NAME+" as paramater_name";
    }

    private String parameterQuery() {
       return format("(SELECT id from %s where %s = 'true') ",KioskDatabase.ParametersTable.TABLE_NAME,KioskDatabase.ParametersTable.IS_USED_IN_TOTALIZER);
    }
//
//    select distinct(sampling_site.*) from sampling_site,PARAMETER_SAMPLING_SITES
//    where sampling_site.id=PARAMETER_SAMPLING_SITES.sampling_site_id
//    and PARAMETER_SAMPLING_SITES.parameter_id  != 19 ;

    public ArrayList<SamplingSite> listAllWaterQualityChannel() {
        ArrayList<SamplingSite> sites=new ArrayList<SamplingSite>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {  Cursor c = rdb.rawQuery(format(
                "SELECT distinct %s.* FROM " +
                        "%s,%s" +
                        " WHERE %s.%s = %s.%s" +
                        " AND %s.%s != %s" +
                        " GROUP BY %s.%s" +
                        " ORDER BY %s.%s",
                KioskDatabase.SamplingSitesTable.TABLE_NAME,
                KioskDatabase.SamplingSitesTable.TABLE_NAME,
                KioskDatabase.SamplingSitesParametersTable.TABLE_NAME,

                KioskDatabase.SamplingSitesTable.TABLE_NAME,
                KioskDatabase.SamplingSitesTable.ID,
                KioskDatabase.SamplingSitesParametersTable.TABLE_NAME,
                KioskDatabase.SamplingSitesParametersTable.SITE_ID,

                KioskDatabase.SamplingSitesParametersTable.TABLE_NAME,
                KioskDatabase.SamplingSitesParametersTable.PARAMETER_ID,
                parameterQuery(),
                KioskDatabase.SamplingSitesTable.TABLE_NAME,
                KioskDatabase.SamplingSitesTable.ID,
                KioskDatabase.SamplingSitesTable.TABLE_NAME,
                KioskDatabase.SamplingSitesTable.NAME),null);
//            Cursor c = rdb.query(KioskDatabase.SamplingSitesTable.TABLE_NAME, COLUMNS, null, null, null, null, null);
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
            return new ArrayList<SamplingSite>();
        } finally {
            rdb.endTransaction();
        }
    }
}
