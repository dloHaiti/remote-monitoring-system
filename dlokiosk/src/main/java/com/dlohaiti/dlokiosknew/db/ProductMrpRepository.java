package com.dlohaiti.dlokiosknew.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosknew.domain.Money;
import com.dlohaiti.dlokiosknew.domain.ProductMrp;
import com.dlohaiti.dlokiosknew.domain.ProductMrps;
import com.google.inject.Inject;

import java.math.BigDecimal;

import static com.dlohaiti.dlokiosknew.db.KioskDatabase.ProductMrpsTable.*;

public class ProductMrpRepository {
    private final static String TAG = ProductMrpRepository.class.getSimpleName();
    private final static String[] COLUMNS = new String[]
            {
                    PRODUCT_ID,
                    CHANNEL_ID,
                    PRICE,
                    CURRENCY
            };
    private final KioskDatabase db;

    @Inject
    public ProductMrpRepository(KioskDatabase db) {
        this.db = db;
    }

    public boolean replaceAll(ProductMrps productMrps) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(TABLE_NAME, null, null);
            for (ProductMrp mrp : productMrps) {
                ContentValues values = new ContentValues();
                values.put(PRODUCT_ID, mrp.productId());
                values.put(CHANNEL_ID, mrp.channelId());
                values.put(PRICE, mrp.price().amountAsString());
                values.put(CURRENCY, mrp.price().getCurrencyCode());

                wdb.insert(TABLE_NAME, null, values);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to replace all product MRPs. " + e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public ProductMrps findAll() {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        Cursor cursor = rdb.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
        return readAll(rdb, cursor);
    }

    private ProductMrps readAll(SQLiteDatabase rdb, Cursor cursor) {
        ProductMrps productMrps = new ProductMrps();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ProductMrp mrp =
                        new ProductMrp(
                                cursor.getLong(cursor.getColumnIndex(PRODUCT_ID)),
                                cursor.getLong(cursor.getColumnIndex(CHANNEL_ID)),
                                new Money(new BigDecimal(
                                        cursor.getString(cursor.getColumnIndex(PRICE))),
                                        cursor.getString(cursor.getColumnIndex(CURRENCY))));
                productMrps.add(mrp);
                cursor.moveToNext();
            }
            rdb.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Failed to load Product MRPs from database", e);
        } finally {
            cursor.close();
            rdb.endTransaction();
        }
        return productMrps;
    }
}
