package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.Clock;
import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.Reading;
import com.google.inject.Inject;

public class ReadingsRepository {
    private final static String TAG = ReadingsRepository.class.getSimpleName();
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
        values.put(KioskDatabase.ReadingsTable.SAMPLING_SITE_NAME, reading.getSampleSiteName());
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
}
