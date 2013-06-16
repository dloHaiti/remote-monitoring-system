package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import com.dlohaiti.dlokiosk.Base64ImageConverter;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.Promotion;
import com.dlohaiti.dlokiosk.domain.PromotionApplicationType;
import com.dlohaiti.dlokiosk.domain.PromotionType;
import com.google.inject.Inject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class PromotionRepository {
    private final static String TAG = PromotionRepository.class.getSimpleName();
    private final Context context;
    private final KioskDatabase db;
    private final KioskDate kioskDate;
    private final Base64ImageConverter imageConverter;
    private final static String[] columns = {
            KioskDatabase.PromotionsTable.ID,
            KioskDatabase.PromotionsTable.APPLIES_TO,
            KioskDatabase.PromotionsTable.PRODUCT_SKU,
            KioskDatabase.PromotionsTable.AMOUNT,
            KioskDatabase.PromotionsTable.TYPE,
            KioskDatabase.PromotionsTable.START_DATE,
            KioskDatabase.PromotionsTable.END_DATE,
            KioskDatabase.PromotionsTable.ICON,
            KioskDatabase.PromotionsTable.SKU
    };

    @Inject
    public PromotionRepository(Context context, KioskDatabase db, KioskDate kioskDate, Base64ImageConverter imageConverter) {
        this.context = context;
        this.db = db;
        this.kioskDate = kioskDate;
        this.imageConverter = imageConverter;
    }

    public List<Promotion> list() {
        List<Promotion> promos = new ArrayList<Promotion>();

        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor c = rdb.query(KioskDatabase.PromotionsTable.TABLE_NAME, columns, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                promos.add(buildPromotion(c));
                c.moveToNext();
            }
            c.close();
            rdb.setTransactionSuccessful();
            return promos;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load promotions from the database.", e);
            return new ArrayList<Promotion>();
        } finally {
            rdb.endTransaction();
        }
    }

    public Promotion findById(long id) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            Cursor c = rdb.query(KioskDatabase.PromotionsTable.TABLE_NAME, columns, where(KioskDatabase.PromotionsTable.ID), matches(id), null, null, null);
            c.moveToFirst();
            //TODO: more than one result? throw exception?
            Promotion promotion = buildPromotion(c);
            c.close();
            rdb.setTransactionSuccessful();
            return promotion;
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to find promotion with id %d in the database.", id), e);
            return new Promotion(null, null, null, null, null, null, null, null, null);
        } finally {
            rdb.endTransaction();
        }
    }

    private Promotion buildPromotion(Cursor c) throws ParseException {
        long id = c.getLong(0);
        PromotionApplicationType appliesTo = PromotionApplicationType.valueOf(c.getString(1));
        String productSku = c.getString(2);
        Date startDate = kioskDate.getFormat().parse(c.getString(5));
        Date endDate = kioskDate.getFormat().parse(c.getString(6));
        String amount = c.getString(3);
        PromotionType type = PromotionType.valueOf(c.getString(4));
        String sku = c.getString(8);
        Bitmap resource = imageConverter.fromBase64EncodedString(c.getString(7));
        return new Promotion(id, sku, appliesTo, productSku, startDate, endDate, amount, type, resource);
    }

    public boolean replaceAll(List<Promotion> promotions) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(KioskDatabase.PromotionsTable.TABLE_NAME, null, null);
            for(Promotion p : promotions) {
                ContentValues values = new ContentValues();
                values.put(KioskDatabase.PromotionsTable.SKU, p.getSku());
                values.put(KioskDatabase.PromotionsTable.AMOUNT, p.getRawAmount().toString());
                values.put(KioskDatabase.PromotionsTable.APPLIES_TO, p.getAppliesTo().name());
                values.put(KioskDatabase.PromotionsTable.PRODUCT_SKU, p.getProductSku());
                values.put(KioskDatabase.PromotionsTable.START_DATE, kioskDate.getFormat().format(p.getStartDate()));
                values.put(KioskDatabase.PromotionsTable.END_DATE, kioskDate.getFormat().format(p.getEndDate()));
                values.put(KioskDatabase.PromotionsTable.TYPE, p.getType().name());
                values.put(KioskDatabase.PromotionsTable.ICON, imageConverter.toBase64EncodedString(p.getImageResource()));
                wdb.insert(KioskDatabase.PromotionsTable.TABLE_NAME, null, values);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to replace all promotions", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }
}
