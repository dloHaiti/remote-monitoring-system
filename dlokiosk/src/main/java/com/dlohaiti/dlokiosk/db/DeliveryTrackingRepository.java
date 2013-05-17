package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.Delivery;
import com.dlohaiti.dlokiosk.domain.DeliveryFactory;
import com.google.inject.Inject;

public class DeliveryTrackingRepository {
    private final DeliveryFactory factory;
    private final KioskDatabase db;
    private final KioskDate kioskDate;

    @Inject
    public DeliveryTrackingRepository(KioskDatabase db, DeliveryFactory factory, KioskDate kioskDate) {
        this.factory = factory;
        this.db = db;
        this.kioskDate = kioskDate;
    }

    public void save(Delivery delivery) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KioskDatabase.DeliveriesTable.QUANTITY, delivery.getQuantity());
        values.put(KioskDatabase.DeliveriesTable.DELIVERY_TYPE, delivery.getType().name());
        values.put(KioskDatabase.DeliveriesTable.KIOSK_ID, delivery.getKioskId());
        values.put(KioskDatabase.DeliveriesTable.CREATED_AT, kioskDate.getFormat().format(delivery.getCreatedAt()));
        writableDatabase.beginTransaction();
        try {
            writableDatabase.insert(KioskDatabase.DeliveriesTable.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }
}
