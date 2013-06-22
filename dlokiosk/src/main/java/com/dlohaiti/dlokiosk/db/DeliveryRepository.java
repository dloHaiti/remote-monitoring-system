package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.DeliveryType;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.Delivery;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.database.DatabaseUtils.queryNumEntries;
import static com.dlohaiti.dlokiosk.db.KioskDatabase.DeliveriesTable.*;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class DeliveryRepository {
    private final static String TAG = DeliveryRepository.class.getSimpleName();
    private final KioskDatabase db;
    private final KioskDate kioskDate;
    private final static String[] COLUMNS = new String[]{
            KioskDatabase.DeliveriesTable.ID,
            QUANTITY,
            DELIVERY_TYPE,
            CREATED_DATE,
            AGENT_NAME
    };

    @Inject
    public DeliveryRepository(KioskDatabase db, KioskDate kioskDate) {
        this.db = db;
        this.kioskDate = kioskDate;
    }

    public boolean save(Delivery delivery) {
        ContentValues values = new ContentValues();
        values.put(QUANTITY, delivery.getQuantity());
        values.put(DELIVERY_TYPE, delivery.getType().name());
        values.put(CREATED_DATE, kioskDate.getFormat().format(delivery.getCreatedDate()));
        values.put(AGENT_NAME, delivery.getAgentName());

        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.insert(TABLE_NAME, null, values);
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save delivery to database.", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public List<Delivery> list() {
        List<Delivery> deliveries = new ArrayList<Delivery>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
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
        } catch (Exception e) {
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
            wdb.delete(TABLE_NAME, where(ID), matches(delivery.getId()));
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
        return queryNumEntries(db.getReadableDatabase(), TABLE_NAME) > 0;
    }
}
