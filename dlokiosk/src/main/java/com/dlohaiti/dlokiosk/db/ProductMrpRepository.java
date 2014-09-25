package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dlohaiti.dlokiosk.db.KioskDatabase.ProductMrpsTable;
import com.dlohaiti.dlokiosk.domain.Money;
import com.dlohaiti.dlokiosk.domain.ProductMrp;
import com.dlohaiti.dlokiosk.domain.ProductMrps;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.Currency;

public class ProductMrpRepository {
    private final static String TAG = ProductMrpRepository.class.getSimpleName();
    private final static String[] COLUMNS = new String[]
            {
                    ProductMrpsTable.PRODUCT_ID,
                    ProductMrpsTable.CHANNEL_ID,
                    ProductMrpsTable.PRICE,
                    ProductMrpsTable.CURRENCY
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
            wdb.delete(ProductMrpsTable.TABLE_NAME, null, null);
            for (ProductMrp mrp : productMrps) {
                ContentValues values = new ContentValues();
                values.put(ProductMrpsTable.PRODUCT_ID, mrp.productId());
                values.put(ProductMrpsTable.CHANNEL_ID, mrp.channelId());
                values.put(ProductMrpsTable.PRICE, mrp.price().amountAsString());
                values.put(ProductMrpsTable.CURRENCY, mrp.price().getCurrencyCode());

                wdb.insert(ProductMrpsTable.TABLE_NAME, null, values);
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
        Cursor cursor = rdb.query(ProductMrpsTable.TABLE_NAME, COLUMNS, null, null, null, null, null);
        return readAll(rdb, cursor);
    }

    private ProductMrps readAll(SQLiteDatabase rdb, Cursor cursor) {
        ProductMrps productMrps = new ProductMrps();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ProductMrp mrp = new ProductMrp(cursor.getLong(0), cursor.getLong(1),
                        new Money(new BigDecimal(cursor.getString(2)), cursor.getString(3)));
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
