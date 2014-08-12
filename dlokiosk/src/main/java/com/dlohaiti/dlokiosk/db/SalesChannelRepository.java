package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.domain.SalesChannel;
import com.google.inject.Inject;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class SalesChannelRepository {
    private final static String TAG = SalesChannelRepository.class.getSimpleName();
    private final static String[] COLUMNS = new String[]
            {
                    KioskDatabase.SalesChannelsTable.ID,
                    KioskDatabase.SalesChannelsTable.NAME,
                    KioskDatabase.SalesChannelsTable.DESCRIPTION
            };
    private final KioskDatabase db;

    @Inject
    public SalesChannelRepository(KioskDatabase db) {
        this.db = db;
    }

    public boolean replaceAll(List<SalesChannel> channels) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(KioskDatabase.SalesChannelsTable.TABLE_NAME, null, null);
            for (SalesChannel channel : channels) {
                ContentValues values = new ContentValues();
                values.put(KioskDatabase.SalesChannelsTable.ID, channel.id());
                values.put(KioskDatabase.SalesChannelsTable.NAME, channel.name());
                values.put(KioskDatabase.SalesChannelsTable.DESCRIPTION, channel.description());

                wdb.insert(KioskDatabase.SalesChannelsTable.TABLE_NAME, null, values);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public SortedSet<SalesChannel> findAll() {
        SortedSet<SalesChannel> channels = new TreeSet<SalesChannel>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.query(KioskDatabase.SalesChannelsTable.TABLE_NAME, COLUMNS, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                channels.add(new SalesChannel(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
                cursor.moveToNext();
            }
            rdb.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Failed to load delivery agents from database", e);
        } finally {
            cursor.close();
            rdb.endTransaction();
        }
        return channels;
    }
}
