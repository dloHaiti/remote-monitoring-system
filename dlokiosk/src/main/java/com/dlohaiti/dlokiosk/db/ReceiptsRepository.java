package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.domain.OrderedProduct;
import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.Receipt;
import com.google.inject.Inject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiptsRepository {
    private final KioskDatabase db;
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss z";
    private final DateFormat df = new SimpleDateFormat(DATE_FORMAT);

    @Inject
    public ReceiptsRepository(Context context) {
        this.db = new KioskDatabase(context);
    }

    public List<Receipt> list() {
        String[] columns = {KioskDatabase.ReceiptsTable.ID, KioskDatabase.ReceiptsTable.KIOSK_ID, KioskDatabase.ReceiptsTable.CREATED_AT};
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        Cursor receiptsCursor = readableDatabase.query(KioskDatabase.ReceiptsTable.TABLE_NAME, columns, null, null, null, null, null);
        List<Receipt> receipts = new ArrayList<Receipt>();
        receiptsCursor.moveToFirst();

        while (!receiptsCursor.isAfterLast()) {
            Date date = null;
            try {
                date = df.parse(receiptsCursor.getString(2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String[] lineItemCols = {KioskDatabase.ReceiptLineItemsTable.ID, KioskDatabase.ReceiptLineItemsTable.SKU, KioskDatabase.ReceiptLineItemsTable.QUANTITY, KioskDatabase.ReceiptLineItemsTable.RECEIPT_ID};
            String selection = String.format("%s=?", KioskDatabase.ReceiptLineItemsTable.RECEIPT_ID);
            String receiptId = receiptsCursor.getString(0);
            String[] args = {receiptId};
            Cursor lineItemsCursor = readableDatabase.query(KioskDatabase.ReceiptLineItemsTable.TABLE_NAME, lineItemCols, selection, args, null, null, null);
            lineItemsCursor.moveToFirst();
            List<OrderedProduct> orderedProducts = new ArrayList<OrderedProduct>();
            while (!lineItemsCursor.isAfterLast()) {
                orderedProducts.add(new OrderedProduct(lineItemsCursor.getString(1), lineItemsCursor.getInt(2)));
                lineItemsCursor.moveToNext();
            }
            receipts.add(new Receipt(receiptsCursor.getLong(0), orderedProducts, date));
            receiptsCursor.moveToNext();
        }
        readableDatabase.close();
        return receipts;
    }

    public void add(List<Product> products) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        writableDatabase.beginTransaction();
        Receipt receipt = new Receipt(products);
        try {
            ContentValues receiptValues = new ContentValues();
            receiptValues.put(KioskDatabase.ReceiptsTable.KIOSK_ID, receipt.getKioskId());
            receiptValues.put(KioskDatabase.ReceiptsTable.CREATED_AT, df.format(receipt.getCreatedAt()));
            long receiptId = writableDatabase.insert(KioskDatabase.ReceiptsTable.TABLE_NAME, null, receiptValues);
            for (OrderedProduct orderedItem : receipt.getOrderedProducts()) {
                ContentValues lineItemValues = new ContentValues();
                lineItemValues.put(KioskDatabase.ReceiptLineItemsTable.RECEIPT_ID, receiptId);
                lineItemValues.put(KioskDatabase.ReceiptLineItemsTable.SKU, orderedItem.getSku());
                lineItemValues.put(KioskDatabase.ReceiptLineItemsTable.QUANTITY, orderedItem.getQuantity());
                writableDatabase.insert(KioskDatabase.ReceiptLineItemsTable.TABLE_NAME, null, lineItemValues);
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
        } catch (Exception e) {
            //TODO: alert this?
            throw new RuntimeException(e);
        } finally {
            writableDatabase.close();
        }
    }

    public void remove(Receipt receipt) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            String[] whereArgs = {receipt.getId().toString()};
            writableDatabase.delete(KioskDatabase.ReceiptsTable.TABLE_NAME, String.format("%s=?", KioskDatabase.ReceiptsTable.ID), whereArgs);
            writableDatabase.delete(KioskDatabase.ReceiptLineItemsTable.TABLE_NAME, String.format("%s=?", KioskDatabase.ReceiptLineItemsTable.RECEIPT_ID), whereArgs);
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            //TODO: alert? log?
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
    }
}
