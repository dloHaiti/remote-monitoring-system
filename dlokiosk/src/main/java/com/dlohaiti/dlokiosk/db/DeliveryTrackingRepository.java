package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.domain.Delivery;
import com.dlohaiti.dlokiosk.domain.DeliveryFactory;
import com.google.inject.Inject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DeliveryTrackingRepository {
    private final DeliveryFactory factory;
    private final KioskDatabase db;
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss z";
    private final DateFormat df = new SimpleDateFormat(DATE_FORMAT);

    @Inject
    public DeliveryTrackingRepository(KioskDatabase db, DeliveryFactory factory) {
        this.factory = factory;
        this.db = db;
    }

    public void save(Delivery delivery) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KioskDatabase.DeliveriesTable.QUANTITY, delivery.getQuantity());
        values.put(KioskDatabase.DeliveriesTable.DELIVERY_TYPE, delivery.getType().name());
        values.put(KioskDatabase.DeliveriesTable.KIOSK_ID, delivery.getKioskId());
        values.put(KioskDatabase.DeliveriesTable.CREATED_AT, df.format(delivery.getCreatedAt()));
        writableDatabase.beginTransaction();
        try {
            writableDatabase.insert(KioskDatabase.DeliveriesTable.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }
}
