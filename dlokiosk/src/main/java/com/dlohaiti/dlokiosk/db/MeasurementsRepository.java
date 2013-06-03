package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.Clock;
import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.SamplingSite;
import com.google.inject.Inject;

public class MeasurementsRepository {
    private final static String TAG = MeasurementsRepository.class.getSimpleName();
    private final KioskDatabase db;
    private final KioskDate kioskDate;
    private final Clock clock;

    @Inject
    public MeasurementsRepository(KioskDatabase db, KioskDate kioskDate, Clock clock) {
        this.db = db;
        this.kioskDate = kioskDate;
        this.clock = clock;
    }

    public boolean save(Iterable<Measurement> measurements, SamplingSite samplingSite) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KioskDatabase.MeasurementsTable.SAMPLING_SITE_NAME, samplingSite.getName());
        values.put(KioskDatabase.MeasurementsTable.CREATED_DATE, kioskDate.getFormat().format(clock.now()));
        values.put(KioskDatabase.MeasurementsTable.KIOSK_ID, "HARDCODED K1");
        wdb.beginTransaction();
        try {
            long measurementId = wdb.insert(KioskDatabase.MeasurementsTable.TABLE_NAME, null, values);
            for(Measurement m : measurements) {
                ContentValues cv = new ContentValues();
                cv.put(KioskDatabase.MeasurementLineItemsTable.PARAMETER_NAME, m.getParameterName());
                cv.put(KioskDatabase.MeasurementLineItemsTable.VALUE, m.getValue().toString());
                cv.put(KioskDatabase.MeasurementLineItemsTable.MEASUREMENT_ID, measurementId);
                wdb.insert(KioskDatabase.MeasurementLineItemsTable.TABLE_NAME, null, cv);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save measurements.", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }
}
