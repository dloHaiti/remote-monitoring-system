package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.domain.SalesChannel;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.dlohaiti.dlokiosk.db.KioskDatabase.SalesChannelCustomerAccountsTable;
import static java.lang.String.format;

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
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.query(KioskDatabase.SalesChannelsTable.TABLE_NAME, COLUMNS, null, null, null, null, null);
        return readAll(rdb, cursor);
    }

    private SortedSet<SalesChannel> readAll(SQLiteDatabase rdb, Cursor cursor) {
        SortedSet<SalesChannel> channels = new TreeSet<SalesChannel>();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                channels.add(new SalesChannel(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
                cursor.moveToNext();
            }
            rdb.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Failed to Sales Channels from database", e);
        } finally {
            cursor.close();
            rdb.endTransaction();
        }
        return channels;
    }

    public ArrayList<SalesChannel> findByCustomerId(long customerId) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.rawQuery(format(
                        "SELECT %s FROM " +
                                "%s sc, " +
                                "%s map " +
                                "WHERE map.%s = ? and map.%s = sc.%s " +
                                "ORDER BY sc.%s",
                        StringUtils.join(COLUMNS, ","),
                        KioskDatabase.SalesChannelsTable.TABLE_NAME,
                        SalesChannelCustomerAccountsTable.TABLE_NAME,
                        SalesChannelCustomerAccountsTable.CUSTOMER_ACCOUNT_ID,
                        SalesChannelCustomerAccountsTable.SALES_CHANNEL_ID,
                        KioskDatabase.SalesChannelsTable.ID,
                        KioskDatabase.SalesChannelsTable.NAME),
                new String[]{String.valueOf(customerId)});

        return new ArrayList<SalesChannel>(readAll(rdb, cursor));
    }
}
