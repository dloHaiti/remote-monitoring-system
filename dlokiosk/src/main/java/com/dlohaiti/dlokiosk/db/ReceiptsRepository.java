package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.*;
import com.google.inject.Inject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiptsRepository {
    private final KioskDatabase db;
    private final ReceiptFactory receiptFactory;
    private final KioskDate kioskDate;

    @Inject
    public ReceiptsRepository(KioskDatabase db, ReceiptFactory receiptFactory, KioskDate kioskDate) {
        this.db = db;
        this.receiptFactory = receiptFactory;
        this.kioskDate = kioskDate;
    }

    public List<Receipt> list() {
        List<Receipt> receipts = new ArrayList<Receipt>();
        String[] columns = {KioskDatabase.ReceiptsTable.ID, KioskDatabase.ReceiptsTable.KIOSK_ID, KioskDatabase.ReceiptsTable.CREATED_AT};
        String[] lineItemCols = {KioskDatabase.ReceiptLineItemsTable.ID, KioskDatabase.ReceiptLineItemsTable.SKU, KioskDatabase.ReceiptLineItemsTable.QUANTITY, KioskDatabase.ReceiptLineItemsTable.RECEIPT_ID};
        String selection = String.format("%s=?", KioskDatabase.ReceiptLineItemsTable.RECEIPT_ID);

        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        readableDatabase.beginTransaction();
        try {
            Cursor receiptsCursor = readableDatabase.query(KioskDatabase.ReceiptsTable.TABLE_NAME, columns, null, null, null, null, null);
            receiptsCursor.moveToFirst();

            while (!receiptsCursor.isAfterLast()) {
                Date date = null;
                try {
                    date = kioskDate.getFormat().parse(receiptsCursor.getString(2));
                } catch (ParseException e) {
                    e.printStackTrace(); //TODO: alert? log?
                }
                String receiptId = receiptsCursor.getString(0);
                String[] args = {receiptId};
                Cursor lineItemsCursor = readableDatabase.query(KioskDatabase.ReceiptLineItemsTable.TABLE_NAME, lineItemCols, selection, args, null, null, null);
                lineItemsCursor.moveToFirst();
                List<OrderedProduct> orderedProducts = new ArrayList<OrderedProduct>();
                while (!lineItemsCursor.isAfterLast()) {
                    orderedProducts.add(new OrderedProduct(lineItemsCursor.getString(1), lineItemsCursor.getInt(2)));
                    lineItemsCursor.moveToNext();
                }
                receipts.add(new Receipt(receiptsCursor.getLong(0), orderedProducts, new ArrayList<Promotion>(), receiptsCursor.getString(1), date));
                receiptsCursor.moveToNext();
            }
            readableDatabase.setTransactionSuccessful();
            return receipts;
        } finally {
            readableDatabase.endTransaction();
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
        }
    }

    public void add(List<Product> products, List<Promotion> promotions) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        writableDatabase.beginTransaction();
        Receipt receipt = receiptFactory.makeReceipt(products, promotions);
        ContentValues receiptValues = new ContentValues();
        receiptValues.put(KioskDatabase.ReceiptsTable.KIOSK_ID, receipt.getKioskId());
        receiptValues.put(KioskDatabase.ReceiptsTable.CREATED_AT, kioskDate.getFormat().format(receipt.getCreatedDate()));
        try {
            long receiptId = writableDatabase.insert(KioskDatabase.ReceiptsTable.TABLE_NAME, null, receiptValues);
            for (OrderedProduct orderedItem : receipt.getOrderedProducts()) {
                ContentValues productLineItemValue = buildContentValues(receiptId, orderedItem, ReceiptLineItemType.PRODUCT);
                writableDatabase.insert(KioskDatabase.ReceiptLineItemsTable.TABLE_NAME, null, productLineItemValue);
            }
            for (Promotion promotion : promotions) {
                ContentValues promotionLineItemValue = buildContentValues(receiptId, promotion, ReceiptLineItemType.PROMOTION);
                writableDatabase.insert(KioskDatabase.ReceiptLineItemsTable.TABLE_NAME, null, promotionLineItemValue);
            }
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            //TODO: alert this?
            throw new RuntimeException(e);
        } finally {
            writableDatabase.endTransaction();
        }
    }

    private ContentValues buildContentValues(long receiptId, Orderable orderedItem, ReceiptLineItemType type) {
        ContentValues productLineItemValue = new ContentValues();
        productLineItemValue.put(KioskDatabase.ReceiptLineItemsTable.TYPE, type.name());
        productLineItemValue.put(KioskDatabase.ReceiptLineItemsTable.RECEIPT_ID, receiptId);
        productLineItemValue.put(KioskDatabase.ReceiptLineItemsTable.SKU, orderedItem.getSku());
        productLineItemValue.put(KioskDatabase.ReceiptLineItemsTable.QUANTITY, orderedItem.getQuantity());
        return productLineItemValue;
    }
}
