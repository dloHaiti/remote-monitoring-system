package com.dlohaiti.dlokiosknew.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosknew.KioskDate;
import com.dlohaiti.dlokiosknew.domain.LineItem;
import com.dlohaiti.dlokiosknew.domain.Money;
import com.dlohaiti.dlokiosknew.domain.Receipt;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dlohaiti.dlokiosknew.db.KioskDatabase.ReceiptLineItemsTable;
import static com.dlohaiti.dlokiosknew.db.KioskDatabase.ReceiptsTable;
import static com.dlohaiti.dlokiosknew.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosknew.db.KioskDatabaseUtils.where;

public class ReceiptsRepository {
    private final static String TAG = ReceiptsRepository.class.getSimpleName();
    private final KioskDatabase db;
    private final KioskDate kioskDate;
    private ConfigurationRepository configurationRepository;
    private final static String[] lineItemCols = new String[]{
            ReceiptLineItemsTable.ID,
            ReceiptLineItemsTable.SKU,
            ReceiptLineItemsTable.QUANTITY,
            ReceiptLineItemsTable.RECEIPT_ID,
            ReceiptLineItemsTable.PRICE,
            ReceiptLineItemsTable.TYPE
    };

    @Inject
    public ReceiptsRepository(KioskDatabase db, KioskDate kioskDate, ConfigurationRepository configurationRepository) {
        this.db = db;
        this.kioskDate = kioskDate;
        this.configurationRepository = configurationRepository;
    }

    public List<Receipt> list() {
        List<Receipt> receipts = new ArrayList<Receipt>();
        String[] columns = {
                ReceiptsTable.ID,
                ReceiptsTable.CREATED_AT,
                ReceiptsTable.TOTAL_GALLONS,
                ReceiptsTable.TOTAL,
                ReceiptsTable.SALES_CHANNEL_ID,
                ReceiptsTable.CUSTOMER_ACCOUNT_ID,
                ReceiptsTable.PAYMENT_MODE,
                ReceiptsTable.IS_SPONSOR_SELECTED,
                ReceiptsTable.SPONSOR_ID,
                ReceiptsTable.SPONSOR_AMOUNT,
                ReceiptsTable.CUSTOMER_AMOUNT,
                ReceiptsTable.PAYMENT_TYPE,
                ReceiptsTable.DELIVERY_TIME
        };

        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        readableDatabase.beginTransaction();
        try {
            Cursor cursor = readableDatabase.query(ReceiptsTable.TABLE_NAME, columns, null, null, null, null, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Date date = null;
                try {
                    date = kioskDate.getFormat().parse(getStringValueForColumn(cursor, ReceiptsTable.CREATED_AT));
                } catch (ParseException e) {
                    Log.e(TAG, String.format("Invalid date format for receipt with id %d", cursor.getLong(0)), e);
                }
                long id = getLongValueForColumn(cursor, ReceiptsTable.ID);
                List<LineItem> lineItems = listLineItems(readableDatabase, id);
                Double totalGallons = getDoubleValueForColumn(cursor, ReceiptsTable.TOTAL_GALLONS);
                Money total = new Money(new BigDecimal(getStringValueForColumn(cursor, ReceiptsTable.TOTAL)));
                Long salesChannelId = getLongValueForColumn(cursor, ReceiptsTable.SALES_CHANNEL_ID);
                String customerAccountId = getStringValueForColumn(cursor, ReceiptsTable.CUSTOMER_ACCOUNT_ID);
                String paymentMode = getStringValueForColumn(cursor, ReceiptsTable.PAYMENT_MODE);
                Boolean isSponsorSelected = getBooleanValueForColumn(cursor, ReceiptsTable.IS_SPONSOR_SELECTED);
                String sponsorId = getStringValueForColumn(cursor, ReceiptsTable.SPONSOR_ID);
                Money sponsorAmount = new Money(getStringValueForColumn(cursor, ReceiptsTable.SPONSOR_AMOUNT),
                        currency());
                Money customerAmount = new Money(getStringValueForColumn(cursor, ReceiptsTable.CUSTOMER_AMOUNT),
                        currency());
                String paymentType = getStringValueForColumn(cursor, ReceiptsTable.PAYMENT_TYPE);
                String deliveryTime = getStringValueForColumn(cursor, ReceiptsTable.DELIVERY_TIME);

                receipts.add(makeReceipt(date, id, lineItems, totalGallons, total, salesChannelId, customerAccountId, paymentMode, isSponsorSelected, sponsorId, sponsorAmount, customerAmount, paymentType, deliveryTime));
                cursor.moveToNext();
            }
            cursor.close();
            readableDatabase.setTransactionSuccessful();
            return receipts;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load all receipts from the database.", e);
            return new ArrayList<Receipt>();
        } finally {
            readableDatabase.endTransaction();
        }
    }

    private Receipt makeReceipt(Date date, long id, List<LineItem> lineItems, Double totalGallons, Money total, Long salesChannelId, String customerAccountId, String paymentMode, Boolean isSponsorSelected, String sponsorId, Money sponsorAmount, Money customerAmount, String paymentType, String deliveryTime) {
        return new Receipt(id, lineItems, date, totalGallons, total, salesChannelId, customerAccountId, paymentMode,
                isSponsorSelected, sponsorId, sponsorAmount, customerAmount, paymentType, deliveryTime);
    }

    private String currency() {
        return configurationRepository.get(ConfigurationKey.CURRENCY);
    }

    private String getStringValueForColumn(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    private Long getLongValueForColumn(Cursor cursor, String column) {
        return cursor.getLong(cursor.getColumnIndex(column));
    }

    private Double getDoubleValueForColumn(Cursor cursor, String column) {
        return cursor.getDouble(cursor.getColumnIndex(column));
    }

    private int getIntegerValueForColumn(Cursor cursor, String column) {
        return cursor.getInt(cursor.getColumnIndex(column));
    }

    private Boolean getBooleanValueForColumn(Cursor cursor, String column) {
        return Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(column)));
    }

    private List<LineItem> listLineItems(SQLiteDatabase readableDatabase, long receiptId) {
        Cursor cursor = readableDatabase
                .query(
                        ReceiptLineItemsTable.TABLE_NAME, lineItemCols,
                        where(ReceiptLineItemsTable.RECEIPT_ID),
                        matches(receiptId), null, null, null);
        cursor.moveToFirst();
        List<LineItem> lineItems = new ArrayList<LineItem>();
        while (!cursor.isAfterLast()) {
            String sku = cursor.getString(1);
            int quantity = cursor.getInt(2);
            String price = cursor.getString(4);
            ReceiptLineItemType type = ReceiptLineItemType.valueOf(cursor.getString(5));
            Money money = new Money(new BigDecimal(price));
            lineItems.add(new LineItem(sku, quantity, money, type));
            cursor.moveToNext();
        }
        cursor.close();
        return lineItems;
    }

    public boolean remove(Receipt receipt) {
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            writableDatabase.delete(ReceiptsTable.TABLE_NAME, where(ReceiptsTable.ID), matches(receipt.getId()));
            writableDatabase.delete(ReceiptLineItemsTable.TABLE_NAME, where(ReceiptLineItemsTable.RECEIPT_ID), matches(receipt.getId()));
            writableDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to remove receipt with id %d from the database.", receipt.getId()), e);
            return false;
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public Long add(Receipt receipt) {
        ContentValues receiptValues = buildContentValuesForReceipt(receipt);
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            long receiptId = wdb.insert(ReceiptsTable.TABLE_NAME, null, receiptValues);
            for (LineItem orderedItem : receipt.getLineItems()) {
                ContentValues productLineItemValue = buildContentValuesForLineItems(receiptId, orderedItem);
                wdb.insert(ReceiptLineItemsTable.TABLE_NAME, null, productLineItemValue);
            }
            wdb.setTransactionSuccessful();
            return new Long(receiptId);
        } catch (Exception e) {
            Log.e(TAG, "Failed to save receipt to database.", e);
            return null;
        } finally {
            wdb.endTransaction();
        }
    }

    private ContentValues buildContentValuesForReceipt(Receipt receipt) {
        ContentValues receiptValues = new ContentValues();
        receiptValues.put(ReceiptsTable.CREATED_AT, kioskDate.getFormat().format(receipt.getCreatedDate()));
        receiptValues.put(ReceiptsTable.TOTAL_GALLONS, receipt.getTotalGallons());
        receiptValues.put(ReceiptsTable.TOTAL, receipt.getTotal().amountAsString());
        receiptValues.put(ReceiptsTable.SALES_CHANNEL_ID, receipt.getSalesChannelId());
        receiptValues.put(ReceiptsTable.CUSTOMER_ACCOUNT_ID, receipt.getCustomerAccountId());
        receiptValues.put(ReceiptsTable.PAYMENT_MODE, receipt.getPaymentMode());
        receiptValues.put(ReceiptsTable.IS_SPONSOR_SELECTED, String.valueOf(receipt.getIsSponsorSelected()));
        receiptValues.put(ReceiptsTable.SPONSOR_ID, receipt.getSponsorId());
        receiptValues.put(ReceiptsTable.SPONSOR_AMOUNT, receipt.getSponsorAmount().amountAsString());
        receiptValues.put(ReceiptsTable.CUSTOMER_AMOUNT, receipt.getCustomerAmount().amountAsString());
        receiptValues.put(ReceiptsTable.PAYMENT_TYPE, receipt.getPaymentType());
        receiptValues.put(ReceiptsTable.DELIVERY_TIME, receipt.getDeliveryTime());
        return receiptValues;
    }

    private ContentValues buildContentValuesForLineItems(long receiptId, LineItem orderedItem) {
        ContentValues productLineItemValue = new ContentValues();
        productLineItemValue.put(ReceiptLineItemsTable.TYPE, orderedItem.getType().name());
        productLineItemValue.put(ReceiptLineItemsTable.RECEIPT_ID, receiptId);
        productLineItemValue.put(ReceiptLineItemsTable.SKU, orderedItem.getSku());
        productLineItemValue.put(ReceiptLineItemsTable.QUANTITY, orderedItem.getQuantity());
        productLineItemValue.put(ReceiptLineItemsTable.PRICE, orderedItem.getPrice().amountAsString());
        return productLineItemValue;
    }

    public boolean isNotEmpty() {
        return list().size() > 0;
    }
}
