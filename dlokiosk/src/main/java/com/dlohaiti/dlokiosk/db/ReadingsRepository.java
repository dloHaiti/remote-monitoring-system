package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.Clock;
import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.Reading;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReadingsRepository {
    private final static String TAG = ReadingsRepository.class.getSimpleName();
    private final static String[] READINGS_COLUMNS = new String[] {
            KioskDatabase.ReadingsTable.ID,
            KioskDatabase.ReadingsTable.SAMPLING_SITE_NAME,
            KioskDatabase.ReadingsTable.CREATED_DATE
    };
    private final static String[] MEASUREMENTS_COLUMNS = new String[] {
            KioskDatabase.MeasurementsTable.PARAMETER_NAME,
            KioskDatabase.MeasurementsTable.VALUE
    };
    private final KioskDatabase db;
    private final KioskDate kioskDate;
    private final Clock clock;

    @Inject
    public ReadingsRepository(KioskDatabase db, KioskDate kioskDate, Clock clock) {
        this.db = db;
        this.kioskDate = kioskDate;
        this.clock = clock;
    }

    public boolean save(Reading reading) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KioskDatabase.ReadingsTable.SAMPLING_SITE_NAME, reading.getSamplingSiteName());
        values.put(KioskDatabase.ReadingsTable.CREATED_DATE, kioskDate.getFormat().format(clock.now()));
        wdb.beginTransaction();
        try {
            long readingId = wdb.insert(KioskDatabase.ReadingsTable.TABLE_NAME, null, values);
            for(Measurement m : reading.getMeasurements()) {
                ContentValues cv = new ContentValues();
                cv.put(KioskDatabase.MeasurementsTable.PARAMETER_NAME, m.getParameterName());
                cv.put(KioskDatabase.MeasurementsTable.VALUE, m.getValue().toString());
                cv.put(KioskDatabase.MeasurementsTable.READING_ID, readingId);
                wdb.insert(KioskDatabase.MeasurementsTable.TABLE_NAME, null, cv);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save reading.", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public List<Reading> list() {
        List<Reading> readings = new ArrayList<Reading>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor rc = rdb.query(KioskDatabase.ReadingsTable.TABLE_NAME, READINGS_COLUMNS, null, null, null, null, null);
            Log.i(TAG, "Found readings: " + rc.getCount());
            if(rc.moveToFirst()) {
                Set<Measurement> measurements = new HashSet<Measurement>();
                while(!rc.isAfterLast()) {
                    long readingId = rc.getLong(0);
                    Cursor mc = rdb.query(KioskDatabase.MeasurementsTable.TABLE_NAME, MEASUREMENTS_COLUMNS, String.format("%s=?", KioskDatabase.MeasurementsTable.READING_ID), new String[] {String.valueOf(readingId)}, null, null, null);
                    if(mc.moveToFirst()) {
                        while(!mc.isAfterLast()) {
                            measurements.add(new Measurement(mc.getString(0), new BigDecimal(mc.getString(1))));
                            mc.moveToNext();
                        }
                    }
                    mc.close();
                    readings.add(new Reading(readingId, rc.getString(1), measurements, kioskDate.getFormat().parse(rc.getString(2))));
                    rc.moveToNext();
                }
            }
            rc.close();
            rdb.setTransactionSuccessful();
            return readings;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load readings.", e);
            return new ArrayList<Reading>();
        } finally {
            rdb.endTransaction();
        }
    }

    public void remove(Reading reading) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(KioskDatabase.MeasurementsTable.TABLE_NAME, String.format("%s=?", KioskDatabase.MeasurementsTable.READING_ID), new String[] { String.valueOf(reading.getId()) });
            wdb.delete(KioskDatabase.ReadingsTable.TABLE_NAME, String.format("%s=?", KioskDatabase.ReadingsTable.ID), new String[] { String.valueOf(reading.getId()) });
            wdb.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to remove reading for Sampling Site %s on %s.", reading.getSamplingSiteName(), kioskDate.getFormat().format(reading.getCreatedDate())), e);
            //FIXME: alert the receiver this failed somehow
        } finally {
            wdb.endTransaction();
        }
    }
}
