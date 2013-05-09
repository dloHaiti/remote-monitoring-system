package com.dlohaiti.dlokiosk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.inject.Inject;

public class KioskDatabase extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "kiosk.db";
    private final static int DATABASE_VERSION = 1;

    @Inject
    public KioskDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        String createSales = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s INTEGER," +
                        "%s TEXT" +
                        ")",
                SalesTable.TABLE_NAME,
                SalesTable.ID,
                SalesTable.QUANTITY,
                SalesTable.SKU
        );
        db.execSQL(createSales);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }

    public static class SalesTable {
        public static String TABLE_NAME = "SALES";
        public static String ID = "ID";
        public static String QUANTITY = "QUANTITY";
        public static String SKU = "SKU";
    }
}
