package com.dlohaiti.dlokiosknew.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import com.dlohaiti.dlokiosknew.Base64ImageConverter;
import com.dlohaiti.dlokiosknew.domain.ProductCategory;
import com.dlohaiti.dlokiosknew.domain.SalesChannel;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class ProductCategoryRepository {
    private final static String TAG = ProductCategoryRepository.class.getSimpleName();
    private final KioskDatabase db;
    private final Base64ImageConverter imageConverter;
    private final static String[] columns = new String[]{
            KioskDatabase.ProductsCategoryTable.ID,
            KioskDatabase.ProductsCategoryTable.NAME,
            KioskDatabase.ProductsCategoryTable.ICON
    };

    @Inject
    public ProductCategoryRepository(KioskDatabase db, Base64ImageConverter imageConverter) {
        this.db = db;
        this.imageConverter = imageConverter;
    }

    public List<ProductCategory> findAll() {
        List<ProductCategory> productCategories = new ArrayList<ProductCategory>();
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        readableDatabase.beginTransaction();
        Cursor cursor = readableDatabase.query(KioskDatabase.ProductsCategoryTable.TABLE_NAME,
                columns, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                productCategories.add(buildProductCategory(cursor));
                cursor.moveToNext();
            }
            readableDatabase.setTransactionSuccessful();
            return productCategories;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load all product categories from the database.", e);
            return new ArrayList<ProductCategory>();
        } finally {
            cursor.close();
            readableDatabase.endTransaction();
        }
    }

    private ProductCategory buildProductCategory(Cursor cursor) {
        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        Bitmap icon = imageConverter.fromBase64EncodedString(cursor.getString(2));
        return new ProductCategory(id, name, icon);
    }

    public boolean replaceAll(List<ProductCategory> productCategories) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(KioskDatabase.ProductsCategoryTable.TABLE_NAME, null, null);
            for (ProductCategory p : productCategories) {
                ContentValues values = new ContentValues();
                values.put(KioskDatabase.ProductsCategoryTable.ID, p.id());
                values.put(KioskDatabase.ProductsCategoryTable.NAME, p.name());
                values.put(KioskDatabase.ProductsCategoryTable.ICON, imageConverter.toBase64EncodedString(p.icon()));
                wdb.insert(KioskDatabase.ProductsCategoryTable.TABLE_NAME, null, values);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to replace all product categories.", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public List<ProductCategory> findWithSalesChannel(SalesChannel salesChannel) {
        List<ProductCategory> productCategories = new ArrayList<ProductCategory>();
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        readableDatabase.beginTransaction();
        Cursor cursor = readableDatabase.rawQuery(format(
                "SELECT distinct(%s), %s, %s " +
                        "from %s, %s, %s " +
                        "where %s=%s.%s " +
                        "and %s.%s=? " +
                        "and %s.%s=%s.%s",
                getNameWithTable(KioskDatabase.ProductsCategoryTable.ID),
                getNameWithTable(KioskDatabase.ProductsCategoryTable.NAME),
                getNameWithTable(KioskDatabase.ProductsCategoryTable.ICON),
                KioskDatabase.ProductsCategoryTable.TABLE_NAME,
                KioskDatabase.ProductsTable.TABLE_NAME,
                KioskDatabase.ProductMrpsTable.TABLE_NAME,
                getNameWithTable(KioskDatabase.ProductsCategoryTable.ID),
                KioskDatabase.ProductsTable.TABLE_NAME,
                KioskDatabase.ProductsTable.CATEGORY_ID,
                KioskDatabase.ProductMrpsTable.TABLE_NAME,
                KioskDatabase.ProductMrpsTable.CHANNEL_ID,
                KioskDatabase.ProductMrpsTable.TABLE_NAME,
                KioskDatabase.ProductMrpsTable.PRODUCT_ID,
                KioskDatabase.ProductsTable.TABLE_NAME,
                KioskDatabase.ProductsTable.ID)
                ,new String[]{String.valueOf(salesChannel.getId())});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                productCategories.add(buildProductCategory(cursor));
                cursor.moveToNext();
            }
            readableDatabase.setTransactionSuccessful();
            return productCategories;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load all product categories from the database.", e);
            return new ArrayList<ProductCategory>();
        } finally {
            cursor.close();
            readableDatabase.endTransaction();
        }
    }

    private String getNameWithTable(String name) {
        return KioskDatabase.ProductsCategoryTable.TABLE_NAME + "." +name;
    }

}
