package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.domain.DeliveryAgent;
import com.google.inject.Inject;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class DeliveryAgentRepository {
    private final static String TAG = DeliveryAgentRepository.class.getSimpleName();
    private final KioskDatabase db;

    @Inject
    public DeliveryAgentRepository(KioskDatabase db) {
        this.db = db;
    }

    public boolean replaceAll(List<DeliveryAgent> agents) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(KioskDatabase.DeliveryAgentsTable.TABLE_NAME, null, null);
            for(DeliveryAgent agent : agents) {
                ContentValues values = new ContentValues();
                values.put(KioskDatabase.DeliveryAgentsTable.NAME, agent.getName());
                wdb.insert(KioskDatabase.DeliveryAgentsTable.TABLE_NAME, null, values);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public SortedSet<DeliveryAgent> findAll() {
        SortedSet<DeliveryAgent> agents = new TreeSet<DeliveryAgent>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.query(KioskDatabase.DeliveryAgentsTable.TABLE_NAME, new String[] {KioskDatabase.DeliveryAgentsTable.NAME}, null, null, null, null, null);
        try {
            if(cursor.moveToFirst()) {
                while(!cursor.isAfterLast()) {
                    agents.add(new DeliveryAgent(cursor.getString(0)));
                    cursor.moveToNext();
                }
            }
            rdb.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Failed to load delivery agents from database", e);
        } finally {
            cursor.close();
            rdb.endTransaction();
        }
        return agents;
    }
}
