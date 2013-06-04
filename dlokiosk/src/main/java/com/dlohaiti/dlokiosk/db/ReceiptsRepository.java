package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.*;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class ReceiptsRepository {
    private final KioskDatabase db;
    private final ReceiptFactory receiptFactory;
    private final KioskDate kioskDate;
    private final static String[] lineItemCols = new String[]{
            KioskDatabase.ReceiptLineItemsTable.ID,
            KioskDatabase.ReceiptLineItemsTable.SKU,
            KioskDatabase.ReceiptLineItemsTable.QUANTITY,
            KioskDatabase.ReceiptLineItemsTable.RECEIPT_ID,
            KioskDatabase.ReceiptLineItemsTable.PRICE,
            KioskDatabase.ReceiptLineItemsTable.TYPE
    };

    @Inject
    public ReceiptsRepository(KioskDatabase db, ReceiptFactory receiptFactory, KioskDate kioskDate) {
        this.db = db;
        this.receiptFactory = receiptFactory;
        this.kioskDate = kioskDate;
    }

    public List<Receipt> list() {
        List<Receipt> receipts = new ArrayList<Receipt>();
        String[] columns = {
                KioskDatabase.ReceiptsTable.ID,
                KioskDatabase.ReceiptsTable.KIOSK_ID,
                KioskDatabase.ReceiptsTable.CREATED_AT,
                KioskDatabase.ReceiptsTable.TOTAL_GALLONS,
                KioskDatabase.ReceiptsTable.TOTAL
        };

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
                Cursor lineItemsCursor = readableDatabase.query(KioskDatabase.ReceiptLineItemsTable.TABLE_NAME, lineItemCols, where(KioskDatabase.ReceiptLineItemsTable.RECEIPT_ID), matches(receiptId), null, null, null);
                lineItemsCursor.moveToFirst();
                List<LineItem> lineItems = new ArrayList<LineItem>();
                while (!lineItemsCursor.isAfterLast()) {
                    String sku = lineItemsCursor.getString(1);
                    int quantity = lineItemsCursor.getInt(2);
                    String price = lineItemsCursor.getString(4);
                    ReceiptLineItemType type = ReceiptLineItemType.valueOf(lineItemsCursor.getString(5));
                    Money money = new Money(new BigDecimal(price));
                    lineItems.add(new LineItem(sku, quantity, money, type));
                    lineItemsCursor.moveToNext();
                }
                long id = receiptsCursor.getLong(0);
                String kioskId = receiptsCursor.getString(1);
                receipts.add(receiptFactory.makeReceipt(id, lineItems, kioskId, date, receiptsCursor.getInt(3), new Money(new BigDecimal(receiptsCursor.getString(4)))));
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
            writableDatabase.delete(KioskDatabase.ReceiptsTable.TABLE_NAME, where(KioskDatabase.ReceiptsTable.ID), matches(receipt.getId()));
            writableDatabase.delete(KioskDatabase.ReceiptLineItemsTable.TABLE_NAME, where(KioskDatabase.ReceiptLineItemsTable.RECEIPT_ID), matches(receipt.getId()));
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            //TODO: alert? log?
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public void add(List<Product> products, List<Promotion> promotions, Integer totalGallons, Money total) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        writableDatabase.beginTransaction();
        Receipt receipt = receiptFactory.makeReceipt(products, promotions, total);
        ContentValues receiptValues = new ContentValues();
        receiptValues.put(KioskDatabase.ReceiptsTable.KIOSK_ID, receipt.getKioskId());
        receiptValues.put(KioskDatabase.ReceiptsTable.CREATED_AT, kioskDate.getFormat().format(receipt.getCreatedDate()));
        receiptValues.put(KioskDatabase.ReceiptsTable.TOTAL_GALLONS, totalGallons);
        receiptValues.put(KioskDatabase.ReceiptsTable.TOTAL, total.getAmount().toString());
        try {
            long receiptId = writableDatabase.insert(KioskDatabase.ReceiptsTable.TABLE_NAME, null, receiptValues);
            for (LineItem orderedItem : receipt.getLineItems()) {
                ContentValues productLineItemValue = buildContentValues(receiptId, orderedItem);
                writableDatabase.insert(KioskDatabase.ReceiptLineItemsTable.TABLE_NAME, null, productLineItemValue);
            }
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            //TODO: alert this?
            throw new RuntimeException(e);
        } finally {
            writableDatabase.endTransaction();
        }
    }

    private ContentValues buildContentValues(long receiptId, LineItem orderedItem) {
        ContentValues productLineItemValue = new ContentValues();
        productLineItemValue.put(KioskDatabase.ReceiptLineItemsTable.TYPE, orderedItem.getType().name());
        productLineItemValue.put(KioskDatabase.ReceiptLineItemsTable.RECEIPT_ID, receiptId);
        productLineItemValue.put(KioskDatabase.ReceiptLineItemsTable.SKU, orderedItem.getSku());
        productLineItemValue.put(KioskDatabase.ReceiptLineItemsTable.QUANTITY, orderedItem.getQuantity());
        productLineItemValue.put(KioskDatabase.ReceiptLineItemsTable.PRICE, orderedItem.getPrice().getAmount().toString());
        return productLineItemValue;
    }
}
