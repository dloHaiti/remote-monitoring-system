package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.Delivery;
import com.dlohaiti.dlokiosk.domain.DeliveryFactory;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public boolean save(Delivery delivery) {
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
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public Collection<Delivery> list() {
        List<Delivery> deliveries = new ArrayList<Delivery>();
        String[] columns = new String[]{
                KioskDatabase.DeliveriesTable.ID,
                KioskDatabase.DeliveriesTable.QUANTITY,
                KioskDatabase.DeliveriesTable.DELIVERY_TYPE,
                KioskDatabase.DeliveriesTable.KIOSK_ID,
                KioskDatabase.DeliveriesTable.CREATED_AT
        };
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor cursor = rdb.query(KioskDatabase.DeliveriesTable.TABLE_NAME, columns, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Integer id = cursor.getInt(0);
                Integer quantity = cursor.getInt(1);
                String type = cursor.getString(2);
                String kioskId = cursor.getString(3);
                String createdAt = cursor.getString(4);
                deliveries.add(factory.makeDelivery(id, quantity, type, kioskId, createdAt));
                cursor.moveToNext();
            }
            rdb.setTransactionSuccessful();
            return deliveries;
        } finally {
            rdb.endTransaction();
        }
    }

    public boolean remove(Delivery delivery) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(KioskDatabase.DeliveriesTable.TABLE_NAME, "id=?", new String[]{ String.valueOf(delivery.getId()) });
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            wdb.endTransaction();
        }
    }
}
