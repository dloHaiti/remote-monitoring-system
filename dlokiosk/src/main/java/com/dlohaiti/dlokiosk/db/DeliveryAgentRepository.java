package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.domain.DeliveryAgent;
import com.google.inject.Inject;

import java.util.List;

public class DeliveryAgentRepository {
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
}
