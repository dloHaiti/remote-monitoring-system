package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.DeliveryType;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.Delivery;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class DeliveryRepository {
    private final static String TAG = DeliveryRepository.class.getSimpleName();
    private final KioskDatabase db;
    private final KioskDate kioskDate;
    private final static String[] COLUMNS = new String[]{
            KioskDatabase.DeliveriesTable.ID,
            KioskDatabase.DeliveriesTable.QUANTITY,
            KioskDatabase.DeliveriesTable.DELIVERY_TYPE,
            KioskDatabase.DeliveriesTable.CREATED_DATE,
            KioskDatabase.DeliveriesTable.AGENT_NAME
    };

    @Inject
    public DeliveryRepository(KioskDatabase db, KioskDate kioskDate) {
        this.db = db;
        this.kioskDate = kioskDate;
    }

    public boolean save(Delivery delivery) {
        ContentValues values = new ContentValues();
        values.put(KioskDatabase.DeliveriesTable.QUANTITY, delivery.getQuantity());
        values.put(KioskDatabase.DeliveriesTable.DELIVERY_TYPE, delivery.getType().name());
        values.put(KioskDatabase.DeliveriesTable.CREATED_DATE, kioskDate.getFormat().format(delivery.getCreatedDate()));
        values.put(KioskDatabase.DeliveriesTable.AGENT_NAME, delivery.getAgentName());
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

    public List<Delivery> list() {
        List<Delivery> deliveries = new ArrayList<Delivery>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.query(KioskDatabase.DeliveriesTable.TABLE_NAME, COLUMNS, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Integer id = cursor.getInt(0);
                Integer quantity = cursor.getInt(1);
                DeliveryType type = DeliveryType.valueOf(cursor.getString(2));
                Date createdDate = kioskDate.getFormat().parse(cursor.getString(3));
                String agentName = cursor.getString(4);
                deliveries.add(new Delivery(id, quantity, type, createdDate, agentName));
                cursor.moveToNext();
            }
            rdb.setTransactionSuccessful();
            return deliveries;
        } catch(Exception e) {
            Log.e(TAG, "Failed to load all deliveries from database.", e);
            return new ArrayList<Delivery>();
        } finally {
            cursor.close();
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

    public boolean isNotEmpty() {
        return DatabaseUtils.queryNumEntries(db.getReadableDatabase(), KioskDatabase.DeliveriesTable.TABLE_NAME) > 0;
    }
}
