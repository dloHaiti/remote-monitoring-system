package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.PaymentHistory;
import com.google.inject.Inject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class PaymentHistoryRepository {
    private final static String TAG = PaymentHistoryRepository.class.getSimpleName();
    private final KioskDatabase db;
    private final KioskDate kioskDate;
    private ConfigurationRepository configurationRepository;

    private final static String[] DB_COLUMNS = new String[]{
            KioskDatabase.PaymentHistoryTable.ID,
            KioskDatabase.PaymentHistoryTable.CUSTOMER_ACCOUNT_ID,
            KioskDatabase.PaymentHistoryTable.AMOUNT,
            KioskDatabase.PaymentHistoryTable.PAYMENT_DATE,
            KioskDatabase.PaymentHistoryTable.RECEIPT_ID,
    };

    @Inject
    public PaymentHistoryRepository(KioskDatabase db, KioskDate kioskDate, ConfigurationRepository configurationRepository) {
        this.db = db;
        this.kioskDate = kioskDate;
        this.configurationRepository = configurationRepository;
    }

    public boolean save(PaymentHistory paymentHistory) {
        SQLiteDatabase wdb = db.getWritableDatabase();

        wdb.beginTransaction();
        try {
            long readingId;
            ContentValues values = new ContentValues();
            values.put(KioskDatabase.PaymentHistoryTable.CUSTOMER_ACCOUNT_ID, paymentHistory.getCustomerId());
            values.put(KioskDatabase.PaymentHistoryTable.PAYMENT_DATE, kioskDate.getFormat().format(paymentHistory.getPaymentDate()));
            values.put(KioskDatabase.PaymentHistoryTable.AMOUNT, String.valueOf(paymentHistory.getAmount()));
            values.put(KioskDatabase.PaymentHistoryTable.RECEIPT_ID, paymentHistory.getReceiptID());
            wdb.insert(KioskDatabase.PaymentHistoryTable.TABLE_NAME, null, values);
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save payment history to database.", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public boolean remove(PaymentHistory paymentHistory) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            writableDatabase.delete(KioskDatabase.PaymentHistoryTable.TABLE_NAME, where(KioskDatabase.PaymentHistoryTable.ID), matches(paymentHistory.getId()));
            writableDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to remove receipt with id %d from the database.", paymentHistory.getId()), e);
            return false;
        } finally {
            writableDatabase.endTransaction();
        }
    }
    public List<PaymentHistory> list() {
        List<PaymentHistory> paymentHistories = new ArrayList<PaymentHistory>();
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor cursor = rdb.query(KioskDatabase.PaymentHistoryTable.TABLE_NAME, DB_COLUMNS, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Date date = null;
                try {
                    date = kioskDate.getFormat().parse(cursor.getString(3));
                } catch (ParseException e) {
                    Log.e(TAG, String.format("Invalid date format for receipt with id %d", cursor.getLong(0)), e);
                }
                paymentHistories.add(new PaymentHistory(cursor.getLong(0),cursor.getString(1),Double.valueOf(cursor.getString(2)),date,cursor.getLong(4)));
               cursor.moveToNext();
            }
            cursor.close();
            rdb.setTransactionSuccessful();
            return paymentHistories;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load readings from database.", e);
            return new ArrayList<PaymentHistory>();
        } finally {
            rdb.endTransaction();
        }
    }

    public boolean isNotEmpty() {
        return list().size() > 0;
    }
}
