package com.dlohaiti.dlokiosk.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.domain.Promotion;
import com.dlohaiti.dlokiosk.domain.PromotionApplicationType;
import com.dlohaiti.dlokiosk.domain.PromotionType;
import com.google.inject.Inject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PromotionRepository {
    private final Context context;
    private final KioskDatabase db;
    private final KioskDate kioskDate;
    private final static String[] columns = {
            KioskDatabase.PromotionsTable.ID,
            KioskDatabase.PromotionsTable.APPLIES_TO,
            KioskDatabase.PromotionsTable.SKU,
            KioskDatabase.PromotionsTable.AMOUNT,
            KioskDatabase.PromotionsTable.TYPE,
            KioskDatabase.PromotionsTable.START_DATE,
            KioskDatabase.PromotionsTable.END_DATE,
            KioskDatabase.PromotionsTable.ICON
    };

    @Inject
    public PromotionRepository(Context context, KioskDatabase db, KioskDate kioskDate) {
        this.context = context;
        this.db = db;
        this.kioskDate = kioskDate;
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
            rdb.setTransactionSuccessful();
            return promos;
        } catch (Exception e) {
            //TODO: log? alert?
            return new ArrayList<Promotion>();
        } finally {
            rdb.endTransaction();
        }
    }

    public Promotion findById(long id) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        try {
            String whereId = String.format("%s=?", KioskDatabase.PromotionsTable.ID);
            Cursor c = rdb.query(KioskDatabase.PromotionsTable.TABLE_NAME, columns, whereId, new String[]{ String.valueOf(id) }, null, null, null);
            c.moveToFirst();
            //TODO: more than one result? throw exception?
            Promotion promotion = buildPromotion(c);
            rdb.setTransactionSuccessful();
            return promotion;
        } catch (Exception e) {
            //TODO: log? alert?
            return new Promotion(null, null, null, null, null, null, null, null);
        } finally {
            rdb.endTransaction();
        }
    }

    private Promotion buildPromotion(Cursor c) throws ParseException {
        long id = c.getLong(0);
        PromotionApplicationType appliesTo = PromotionApplicationType.valueOf(c.getString(1));
        String sku = c.getString(2);
        Date startDate = kioskDate.getFormat().parse(c.getString(5));
        Date endDate = kioskDate.getFormat().parse(c.getString(6));
        String amount = c.getString(3);
        PromotionType type = PromotionType.valueOf(c.getString(4));
        Bitmap resource; //TODO: how can we make this show the unknown image when it doesn't decode properly?
//                try {
//                    byte[] imageData = Base64.decode(c.getString(7));
//                    resource = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//                } catch (IOException e) {
//                    Log.e(getClass().getSimpleName(), String.format("image for promo(%s) could not be decoded", sku));
        resource = BitmapFactory.decodeResource(context.getResources(), R.drawable.unknown);
//                }
        return new Promotion(id, appliesTo, sku, startDate, endDate, amount, type, resource);
    }
}
