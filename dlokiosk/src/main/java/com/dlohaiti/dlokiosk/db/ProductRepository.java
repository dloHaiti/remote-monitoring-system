package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import com.dlohaiti.dlokiosk.Base64ImageConverter;
import com.dlohaiti.dlokiosk.domain.Money;
import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.Products;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.KioskDatabase.ProductMrpsTable;
import static com.dlohaiti.dlokiosk.db.KioskDatabase.ProductsTable;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.matches;
import static com.dlohaiti.dlokiosk.db.KioskDatabaseUtils.where;

public class ProductRepository {
    private final static String TAG = ProductRepository.class.getSimpleName();
    private final KioskDatabase db;
    private final Base64ImageConverter imageConverter;
    private final static String[] columns = new String[]{
            ProductsTable.ID,
            ProductsTable.SKU,
            ProductsTable.ICON,
            ProductsTable.REQUIRES_QUANTITY,
            ProductsTable.MINIMUM_QUANTITY,
            ProductsTable.MAXIMUM_QUANTITY,
            ProductsTable.PRICE,
            ProductsTable.CURRENCY,
            ProductsTable.DESCRIPTION,
            ProductsTable.GALLONS,
            ProductsTable.CATEGORY_ID
    };

    @Inject
    public ProductRepository(KioskDatabase db, Base64ImageConverter imageConverter) {
        this.db = db;
        this.imageConverter = imageConverter;
    }

    public List<Product> list() {
        List<Product> products = new ArrayList<Product>();
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        readableDatabase.beginTransaction();
        Cursor cursor = readableDatabase.query(ProductsTable.TABLE_NAME,
                columns, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                products.add(buildProduct(cursor));
                cursor.moveToNext();
            }
            readableDatabase.setTransactionSuccessful();
            return products;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load all products from the database.", e);
            return new ArrayList<Product>();
        } finally {
            cursor.close();
            readableDatabase.endTransaction();
        }
    }

    private Product buildProduct(Cursor cursor) {
        String sku = cursor.getString(1);
        boolean requiresQuantity = Boolean.parseBoolean(cursor.getString(3));
        Integer minimum = cursor.getInt(4);
        Integer maximum = cursor.getInt(5);
        Money price = new Money(new BigDecimal(cursor.getDouble(6)));
        String description = cursor.getString(8);
        Double gallons = cursor.getDouble(9);
        long category = cursor.getInt(10);
        Bitmap resource = imageConverter.fromBase64EncodedString(cursor.getString(2));
        long id = cursor.getLong(0);
        return new Product(id, sku, resource, requiresQuantity, null, minimum, maximum, price, description, gallons,
                category);
    }

    public Product findById(Long id) {
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        readableDatabase.beginTransaction();
        Cursor cursor = readableDatabase.query(ProductsTable.TABLE_NAME, columns, where(ProductsTable.ID), matches(id), null, null, null);
        try {
            if (cursor.getCount() != 1) {
                return new Product(null, null, null, false, null, null, null, null, null, null, null);
            }
            cursor.moveToFirst();
            Product product = buildProduct(cursor);
            readableDatabase.setTransactionSuccessful();
            return product;
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to find product with id %d in the database.", id), e);
            return new Product(null, null, null, false, null, null, null, null, null, null, null);
        } finally {
            cursor.close();
            readableDatabase.endTransaction();
        }
    }

    public boolean replaceAll(List<Product> products) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            wdb.delete(ProductsTable.TABLE_NAME, null, null);
            for (Product p : products) {
                ContentValues values = new ContentValues();
                values.put(ProductsTable.ID, p.getId());
                values.put(ProductsTable.SKU, p.getSku());
                values.put(ProductsTable.PRICE, p.getPrice().getAmount().toString());
                values.put(ProductsTable.DESCRIPTION, p.getDescription());
                values.put(ProductsTable.GALLONS, p.getGallons());
                values.put(ProductsTable.ICON, imageConverter.toBase64EncodedString(p.getImageResource()));
                values.put(ProductsTable.MINIMUM_QUANTITY, p.getMinimumQuantity());
                values.put(ProductsTable.MAXIMUM_QUANTITY, p.getMaximumQuantity());
                values.put(ProductsTable.REQUIRES_QUANTITY, String.valueOf(p.requiresQuantity()));
                values.put(ProductsTable.CURRENCY, p.getPrice().getCurrencyCode());
                values.put(ProductsTable.CATEGORY_ID, p.getCategoryId());
                wdb.insert(ProductsTable.TABLE_NAME, null, values);
            }
            wdb.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to replace all products.", e);
            return false;
        } finally {
            wdb.endTransaction();
        }
    }

    public Products findProductsWithPriceFor(long salesChannelId) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        rdb.beginTransaction();
        /*SELECT PRODUCTS.ID as PRODUCTSID, PRODUCTS.SKU as PRODUCTSSKU, PRODUCTS.REQUIRES_QUANTITY as PRODUCTSREQUIRES_QUANTITY,
        PRODUCTS.MINIMUM_QUANTITY as PRODUCTSMINIMUM_QUANTITY,  PRODUCTS.MAXIMUM_QUANTITY as PRODUCTSMAXIMUM_QUANTITY,
        PRODUCTS.PRICE as PRODUCTSPRICE, PRODUCTS.CURRENCY as PRODUCTSCURRENCY, PRODUCTS.DESCRIPTION as PRODUCTSDESCRIPTION,
        PRODUCTS.GALLONS as PRODUCTSGALLONS, PRODUCTS.CATEGORY_ID as PRODUCTSCATEGORY_ID, PRODUCT_MRPS.PRICE AS PRODUCT_MRPSPRICE
        FROM PRODUCTS, PRODUCT_MRPS  WHERE PRODUCT_MRPS.PRODUCT_ID= PRODUCTS.ID AND PRODUCT_MRPS.CHANNEL_ID=109 ORDER BY PRODUCTS.CATEGORY_ID*/
        Cursor cursor = rdb.rawQuery(
                "SELECT " +
                        tableColumnsForQuery(ProductsTable.TABLE_NAME, columns) + ", " +
                        ProductMrpsTable.TABLE_NAME + "." + ProductMrpsTable.PRICE +
                        " AS " +
                        ProductMrpsTable.TABLE_NAME + ProductMrpsTable.PRICE +
                        " FROM " + ProductsTable.TABLE_NAME + ", " + ProductMrpsTable.TABLE_NAME +
                        " WHERE "  + ProductMrpsTable.TABLE_NAME + "." + ProductMrpsTable.PRODUCT_ID + " = " + ProductsTable.TABLE_NAME + "." + ProductsTable.ID +
                        " AND " + ProductMrpsTable.TABLE_NAME + "." + ProductMrpsTable.CHANNEL_ID + " = ? " +
                        " ORDER BY " + ProductsTable.TABLE_NAME + "." + ProductsTable.CATEGORY_ID,
                new String[]{String.valueOf(salesChannelId)});
        return readProducts(rdb, cursor);
    }

    private String tableColumnsForQuery(String tableName, String[] tableColumns) {
        return StringUtils.join(prepend(tableColumns, tableName), ", ");
    }

    private String[] prepend(String[] input, String tableName) {
        int length = input.length;
        String[] output = new String[length];
        for (int index = 0; index < length; index++) {
            output[index] = tableName + "." + input[index] + " as " + tableName + input[index];
        }
        return output;
    }

    private Products readProducts(SQLiteDatabase rdb, Cursor cursor) {
        Products products = new Products();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                long id = cursor.getLong(getColumnIndexByAlias(cursor, ProductsTable.TABLE_NAME, ProductsTable.ID));
                String sku = cursor.getString(getColumnIndexByAlias(cursor, ProductsTable.TABLE_NAME, ProductsTable.SKU));
                Bitmap resource = imageConverter.fromBase64EncodedString(cursor.getString(getColumnIndexByAlias(cursor, ProductsTable.TABLE_NAME, ProductsTable.ICON)));
                boolean requiresQuantity = Boolean.parseBoolean(cursor.getString(getColumnIndexByAlias(cursor, ProductsTable.TABLE_NAME, ProductsTable.REQUIRES_QUANTITY)));
                Integer minimum = cursor.getInt(getColumnIndexByAlias(cursor, ProductsTable.TABLE_NAME, ProductsTable.MINIMUM_QUANTITY));
                Integer maximum = cursor.getInt(getColumnIndexByAlias(cursor, ProductsTable.TABLE_NAME, ProductsTable.MAXIMUM_QUANTITY));
                Money price = cursor.getDouble(getColumnIndexByAlias(cursor, ProductMrpsTable.TABLE_NAME, ProductMrpsTable.PRICE)) != 0
                        ? new Money(new BigDecimal(cursor.getDouble(getColumnIndexByAlias(cursor, ProductMrpsTable.TABLE_NAME, ProductMrpsTable.PRICE))))
                        : new Money(new BigDecimal(cursor.getDouble(getColumnIndexByAlias(cursor, ProductsTable.TABLE_NAME, ProductsTable.PRICE))));
                String description = cursor.getString(getColumnIndexByAlias(cursor, ProductsTable.TABLE_NAME, ProductsTable.DESCRIPTION));
                Double gallons = cursor.getDouble(getColumnIndexByAlias(cursor, ProductsTable.TABLE_NAME, ProductsTable.GALLONS));
                long category = cursor.getInt(getColumnIndexByAlias(cursor, ProductsTable.TABLE_NAME, ProductsTable.CATEGORY_ID));
                products.add(new Product(id, sku, resource, requiresQuantity, null, minimum, maximum, price,
                        description, gallons, category));
                cursor.moveToNext();
            }
            rdb.setTransactionSuccessful();
            return products;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load products.", e);
        } finally {
            cursor.close();
            rdb.endTransaction();
        }
        return products;
    }

    private int getColumnIndexByAlias(Cursor cursor, String table, String column) {
        return cursor.getColumnIndex(table + column);
    }
}
