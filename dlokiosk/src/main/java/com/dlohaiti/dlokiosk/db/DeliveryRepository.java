package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.Delivery;
import com.dlohaiti.dlokiosk.domain.DeliveryFactory;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class DeliveryRepository {
    private final static String TAG = DeliveryRepository.class.getSimpleName();
    private final DeliveryFactory factory;
    private final KioskDatabase db;
    private final KioskDate kioskDate;

    @Inject
    public DeliveryRepository(KioskDatabase db, DeliveryFactory factory, KioskDate kioskDate) {
        this.factory = factory;
        this.db = db;
        this.kioskDate = kioskDate;
    }

    public boolean save(Delivery delivery) {
        ContentValues values = new ContentValues();
        values.put(KioskDatabase.DeliveriesTable.QUANTITY, delivery.getQuantity());
        values.put(KioskDatabase.DeliveriesTable.DELIVERY_TYPE, delivery.getType().name());
        values.put(KioskDatabase.DeliveriesTable.CREATED_DATE, kioskDate.getFormat().format(delivery.getCreatedDate()));
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            writableDatabase.insert(KioskDatabase.DeliveriesTable.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save delivery to database.", e);
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
                KioskDatabase.DeliveriesTable.CREATED_DATE
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
                String createdDate = cursor.getString(3);
                deliveries.add(factory.makeDelivery(id, quantity, type, createdDate));
                cursor.moveToNext();
            }
            cursor.close();
            rdb.setTransactionSuccessful();
            return deliveries;
        } catch(Exception e) {
            Log.e(TAG, "Failed to load all deliveries from database.", e);
            return new ArrayList<Delivery>();
        } finally {
            rdb.endTransaction();
        }
    }

    public boolean remove(Delivery delivery) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(KioskDatabase.DeliveriesTable.TABLE_NAME, where(KioskDatabase.DeliveriesTable.ID), matches(delivery.getId()));
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to delete delivery from the database", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }
}
