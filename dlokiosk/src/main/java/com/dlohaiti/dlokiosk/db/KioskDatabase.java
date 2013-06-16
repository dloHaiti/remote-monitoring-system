package com.dlohaiti.dlokiosk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class KioskDatabase extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "kiosk.db";
    private final static int DATABASE_VERSION = 1;

    @Inject
    public KioskDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        String createReceipts = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s INTEGER," +
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                ReceiptsTable.TABLE_NAME,
                ReceiptsTable.ID,
                ReceiptsTable.TOTAL_GALLONS,
                ReceiptsTable.TOTAL,
                ReceiptsTable.CREATED_AT
        );
        String createReceiptLineItems = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s INTEGER," +
                        "%s TEXT," +
                        "%s INTEGER," +
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                ReceiptLineItemsTable.TABLE_NAME,
                ReceiptLineItemsTable.ID,
                ReceiptLineItemsTable.RECEIPT_ID,
                ReceiptLineItemsTable.SKU,
                ReceiptLineItemsTable.QUANTITY,
                ReceiptLineItemsTable.TYPE,
                ReceiptLineItemsTable.PRICE
        );
        String createProducts = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s REAL," +
                        "%s TEXT," +
                        "%s INTEGER," +
                        "%s INTEGER," +
                        "%s INTEGER," +
                        "%s INTEGER" +
                        ")",
                ProductsTable.TABLE_NAME,
                ProductsTable.ID,
                ProductsTable.SKU,
                ProductsTable.DESCRIPTION,
                ProductsTable.ICON,
                ProductsTable.PRICE,
                ProductsTable.CURRENCY,
                ProductsTable.REQUIRES_QUANTITY,
                ProductsTable.MINIMUM_QUANTITY,
                ProductsTable.MAXIMUM_QUANTITY,
                ProductsTable.GALLONS
        );
        String createConfiguration = String.format(
                "CREATE TABLE %s(" +
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                ConfigurationTable.TABLE_NAME,
                ConfigurationTable.KEY,
                ConfigurationTable.VALUE
        );
        String createDeliveries = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s TEXT," +
                        "%s INTEGER," +
                        "%s TEXT" +
                        ")",
                DeliveriesTable.TABLE_NAME,
                DeliveriesTable.ID,
                DeliveriesTable.DELIVERY_TYPE,
                DeliveriesTable.QUANTITY,
                DeliveriesTable.CREATED_AT
        );
        String createPromotions = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                PromotionsTable.TABLE_NAME,
                PromotionsTable.ID,
                PromotionsTable.APPLIES_TO,
                PromotionsTable.PRODUCT_SKU,
                PromotionsTable.START_DATE,
                PromotionsTable.END_DATE,
                PromotionsTable.AMOUNT,
                PromotionsTable.TYPE,
                PromotionsTable.SKU,
                PromotionsTable.ICON
        );
        String createSamplingSites = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s TEXT" +
                        ")",
                SamplingSitesTable.TABLE_NAME,
                SamplingSitesTable.ID,
                SamplingSitesTable.NAME
        );
        String createParameters = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                ParametersTable.TABLE_NAME,
                ParametersTable.ID,
                ParametersTable.NAME,
                ParametersTable.UNIT_OF_MEASURE,
                ParametersTable.MINIMUM,
                ParametersTable.MAXIMUM,
                ParametersTable.IS_OK_NOT_OK
        );
        String createSamplingSiteParameters = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s INTEGER," +
                        "%s INTEGER" +
                        ")",
                SamplingSitesParametersTable.TABLE_NAME,
                SamplingSitesParametersTable.ID,
                SamplingSitesParametersTable.PARAMETER_ID,
                SamplingSitesParametersTable.SITE_ID
        );
        String createMeasurements = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s INTEGER," +
                        "%s TEXT" +
                ")",
                ReadingsTable.TABLE_NAME,
                ReadingsTable.ID,
                ReadingsTable.SAMPLING_SITE_NAME,
                ReadingsTable.CREATED_DATE
        );
        String createMeasurementLineItems = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s INTEGER," +
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                MeasurementsTable.TABLE_NAME,
                MeasurementsTable.ID,
                MeasurementsTable.READING_ID,
                MeasurementsTable.PARAMETER_NAME,
                MeasurementsTable.VALUE
        );

        String insertConfig = String.format(
                "INSERT INTO %s(%s, %s) VALUES (?, ?)",
                ConfigurationTable.TABLE_NAME,
                ConfigurationTable.KEY,
                ConfigurationTable.VALUE
        );

        String insertSamplingSite = String.format(
                "INSERT INTO %s(%s, %s) VALUES(?, ?)",
                SamplingSitesTable.TABLE_NAME,
                SamplingSitesTable.ID,
                SamplingSitesTable.NAME
        );

        String insertParameter = String.format(
                "INSERT INTO %s(%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
                ParametersTable.TABLE_NAME,
                ParametersTable.ID,
                ParametersTable.NAME,
                ParametersTable.UNIT_OF_MEASURE,
                ParametersTable.MINIMUM,
                ParametersTable.MAXIMUM,
                ParametersTable.IS_OK_NOT_OK
        );

        String insertSamplingSiteParameter = String.format(
                "INSERT INTO %s(%s, %s) VALUES (?, ?)",
                SamplingSitesParametersTable.TABLE_NAME,
                SamplingSitesParametersTable.SITE_ID,
                SamplingSitesParametersTable.PARAMETER_ID
        );

        db.execSQL(createMeasurements);
        db.execSQL(createMeasurementLineItems);
        db.execSQL(createSamplingSites);
        int BOREHOLE_EARLY = 1;
        int BOREHOLE_LATE = 2;
        int FILL_STATION_EARLY = 3;
        int FILL_STATION_LATE = 4;
        int KIOSK_COUNTER_EARLY = 5;
        int KIOSK_COUNTER_LATE = 6;
        int BULK_FILL_EARLY = 7;
        int BULK_FILL_LATE = 8;
        int CLEANING_STATION_EARLY = 9;
        int CLEANING_STATION_LATE = 10;
        int WTU = 11;
        db.execSQL(insertSamplingSite, new Object[]{BOREHOLE_EARLY, "Borehole - Early Day"});
        db.execSQL(insertSamplingSite, new Object[]{BOREHOLE_LATE, "Borehole - Late Day"});
        db.execSQL(insertSamplingSite, new Object[]{FILL_STATION_EARLY, "Fill Station - Early Day"});
        db.execSQL(insertSamplingSite, new Object[]{FILL_STATION_LATE, "Fill Station - Late Day"});
        db.execSQL(insertSamplingSite, new Object[]{KIOSK_COUNTER_EARLY, "Kiosk Counter - Early Day"});
        db.execSQL(insertSamplingSite, new Object[]{KIOSK_COUNTER_LATE, "Kiosk Counter - Late Day"});
        db.execSQL(insertSamplingSite, new Object[]{BULK_FILL_EARLY, "Bulk Fill - Early Day"});
        db.execSQL(insertSamplingSite, new Object[]{BULK_FILL_LATE, "Bulk Fill - Late Day"});
        db.execSQL(insertSamplingSite, new Object[]{CLEANING_STATION_EARLY, "Cleaning Station - Early Day"});
        db.execSQL(insertSamplingSite, new Object[]{CLEANING_STATION_LATE, "Cleaning Station - Late Day"});
        db.execSQL(insertSamplingSite, new Object[]{WTU, "WTU"});

        db.execSQL(createParameters);
        int TEMPERATURE_ID = 1;
        int PH_ID = 2;
        int TURBIDITY_ID = 3;
        int ALKALINITY_ID = 4;
        int HARDNESS_ID = 5;
        int TDS_FEED_RO_ROUF_ID = 6;
        int FREE_CL_RESIDUAL_ID = 7;
        int TOTAL_CL_RESIDUAL_ID = 8;
        int TDS_FEED = 9;
        int TDS_RO = 10;
        int TDS_ROUF = 11;
        int PRESSURE_PREFILTER = 12;
        int PRESSURE_POSTFILTER = 13;
        int PRESSURE_PRERO = 14;
        int PRESSURE_POSTRO = 15;
        int FEED_FLOW_RATE = 16;
        int PRODUCT_FLOW_RATE = 17;
        int RO_PRODUCT_FLOW_RATE = 18;
        int GALLONS_DISTRIBUTED = 19;
        int COLOR = 20;
        int ODOR = 21;
        int TASTE = 22;
        db.execSQL(insertParameter, new Object[]{TEMPERATURE_ID, "Temperature", "°C", "10", "30", "false"});
        db.execSQL(insertParameter, new Object[]{PH_ID, "pH", "", "5", "9", "false"});
        db.execSQL(insertParameter, new Object[]{TURBIDITY_ID, "Turbidity", "NTU", "0", "10", "false"});
        db.execSQL(insertParameter, new Object[]{ALKALINITY_ID, "Alkalinity", "mg/L CaCO3 (ppm)", "100", "500", "false"});
        db.execSQL(insertParameter, new Object[]{HARDNESS_ID, "Hardness", "mg/L CaCO3 (ppm)", "100", "500", "false"});
        db.execSQL(insertParameter, new Object[]{TDS_FEED_RO_ROUF_ID, "TDS (Feed, RO, RO/UF)", "mg/L (ppm)", "0", "800", "false"});
        db.execSQL(insertParameter, new Object[]{FREE_CL_RESIDUAL_ID, "Free chlorine residual", "mg/L Cl2 (ppm)", "0", "1", "false"});
        db.execSQL(insertParameter, new Object[]{TOTAL_CL_RESIDUAL_ID, "Total chlorine residual", "mg/L Cl2 (ppm)", "0", "1", "false"});
        db.execSQL(insertParameter, new Object[]{TDS_FEED, "TDS Feed", "mg/L (ppm)", "0", "800", "false"});
        db.execSQL(insertParameter, new Object[]{TDS_RO, "TDS RO", "mg/L (ppm)", "0", "800", "false"});
        db.execSQL(insertParameter, new Object[]{TDS_ROUF, "TDS RO/UF", "mg/L (ppm)", "0", "800", "false"});
        db.execSQL(insertParameter, new Object[]{PRESSURE_PREFILTER, "Pressure Pre-Filter", "PSI", "0", "100", "false"});
        db.execSQL(insertParameter, new Object[]{PRESSURE_POSTFILTER, "Pressure Post-Filter", "PSI", "0", "100", "false"});
        db.execSQL(insertParameter, new Object[]{PRESSURE_PRERO, "Pressure Pre-RO", "PSI", "0", "300", "false"});
        db.execSQL(insertParameter, new Object[]{PRESSURE_POSTRO, "Pressure Post-RO", "PSI", "0", "300", "false"});
        db.execSQL(insertParameter, new Object[]{FEED_FLOW_RATE, "Feed Flow Rate", "gpm", "0", "", "false"});
        db.execSQL(insertParameter, new Object[]{PRODUCT_FLOW_RATE, "Product Flow Rate", "gpm", "0", "", "false"});
        db.execSQL(insertParameter, new Object[]{RO_PRODUCT_FLOW_RATE, "R/O Product Flow Rate", "Gallons", "0", "", "false"});
        db.execSQL(insertParameter, new Object[]{GALLONS_DISTRIBUTED, "Gallons distributed", "Gallons", "0", "", "false"});
        db.execSQL(insertParameter, new Object[]{COLOR, "Color", "", "0", "1", "true"});
        db.execSQL(insertParameter, new Object[]{ODOR, "Odor", "", "0", "1", "true"});
        db.execSQL(insertParameter, new Object[]{TASTE, "Taste", "", "0", "1", "true"});

        db.execSQL(createSamplingSiteParameters);
        db.execSQL(insertSamplingSiteParameter, new Object[]{BOREHOLE_EARLY, TEMPERATURE_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{BOREHOLE_EARLY, PH_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{BOREHOLE_EARLY, TURBIDITY_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{BOREHOLE_EARLY, ALKALINITY_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{BOREHOLE_EARLY, HARDNESS_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{BOREHOLE_EARLY, TDS_FEED_RO_ROUF_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{BOREHOLE_EARLY, GALLONS_DISTRIBUTED});
        db.execSQL(insertSamplingSiteParameter, new Object[]{BOREHOLE_LATE, GALLONS_DISTRIBUTED});

        db.execSQL(insertSamplingSiteParameter, new Object[]{FILL_STATION_EARLY, PH_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{FILL_STATION_EARLY, TURBIDITY_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{FILL_STATION_EARLY, FREE_CL_RESIDUAL_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{FILL_STATION_EARLY, TOTAL_CL_RESIDUAL_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{FILL_STATION_EARLY, ALKALINITY_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{FILL_STATION_EARLY, HARDNESS_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{FILL_STATION_EARLY, TDS_FEED_RO_ROUF_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{FILL_STATION_EARLY, GALLONS_DISTRIBUTED});
        db.execSQL(insertSamplingSiteParameter, new Object[]{FILL_STATION_EARLY, COLOR});
        db.execSQL(insertSamplingSiteParameter, new Object[]{FILL_STATION_EARLY, ODOR});
        db.execSQL(insertSamplingSiteParameter, new Object[]{FILL_STATION_EARLY, TASTE});
        db.execSQL(insertSamplingSiteParameter, new Object[]{FILL_STATION_LATE, GALLONS_DISTRIBUTED});

        db.execSQL(insertSamplingSiteParameter, new Object[]{KIOSK_COUNTER_EARLY, PH_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{KIOSK_COUNTER_EARLY, TURBIDITY_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{KIOSK_COUNTER_EARLY, FREE_CL_RESIDUAL_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{KIOSK_COUNTER_EARLY, TOTAL_CL_RESIDUAL_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{KIOSK_COUNTER_EARLY, ALKALINITY_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{KIOSK_COUNTER_EARLY, HARDNESS_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{KIOSK_COUNTER_EARLY, TDS_FEED_RO_ROUF_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{KIOSK_COUNTER_EARLY, GALLONS_DISTRIBUTED});
        db.execSQL(insertSamplingSiteParameter, new Object[]{KIOSK_COUNTER_EARLY, COLOR});
        db.execSQL(insertSamplingSiteParameter, new Object[]{KIOSK_COUNTER_EARLY, ODOR});
        db.execSQL(insertSamplingSiteParameter, new Object[]{KIOSK_COUNTER_EARLY, TASTE});
        db.execSQL(insertSamplingSiteParameter, new Object[]{KIOSK_COUNTER_LATE, GALLONS_DISTRIBUTED});

        db.execSQL(insertSamplingSiteParameter, new Object[]{BULK_FILL_EARLY, GALLONS_DISTRIBUTED});
        db.execSQL(insertSamplingSiteParameter, new Object[]{BULK_FILL_LATE, GALLONS_DISTRIBUTED});

        db.execSQL(insertSamplingSiteParameter, new Object[]{CLEANING_STATION_EARLY, GALLONS_DISTRIBUTED});
        db.execSQL(insertSamplingSiteParameter, new Object[]{CLEANING_STATION_LATE, GALLONS_DISTRIBUTED});

        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, TEMPERATURE_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, PH_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, FREE_CL_RESIDUAL_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, TOTAL_CL_RESIDUAL_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, ALKALINITY_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, HARDNESS_ID});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, TDS_FEED});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, TDS_RO});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, TDS_ROUF});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, PRESSURE_PREFILTER});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, PRESSURE_POSTFILTER});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, PRESSURE_PRERO});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, PRESSURE_POSTRO});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, FEED_FLOW_RATE});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, PRODUCT_FLOW_RATE});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, RO_PRODUCT_FLOW_RATE});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, COLOR});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, TASTE});
        db.execSQL(insertSamplingSiteParameter, new Object[]{WTU, ODOR});

        db.execSQL(createReceipts);
        db.execSQL(createReceiptLineItems);
        db.execSQL(createProducts);
        db.execSQL(createConfiguration);
        db.execSQL(createDeliveries);
        db.execSQL(createPromotions);
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.KIOSK_ID.name(), "kiosk01"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.KIOSK_PASSWORD.name(), "pw"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.DELIVERY_TRACKING_MIN.name(), "0"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.DELIVERY_TRACKING_MAX.name(), "24"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.DELIVERY_TRACKING_DEFAULT.name(), "24"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.REPORTS_HOME_URL.name(), "http://10.0.2.2:8080/dloserver/report"});
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }

    public static class ReceiptLineItemsTable {
        public static final String TABLE_NAME = "RECEIPT_LINE_ITEMS";
        public static final String ID = "ID";
        public static final String RECEIPT_ID = "RECEIPT_ID";
        public static final String QUANTITY = "QUANTITY";
        public static final String SKU = "SKU";
        public static final String TYPE = "TYPE";
        public static final String PRICE = "PRICE";
    }

    public static class ProductsTable {
        public static final String TABLE_NAME = "PRODUCTS";
        public static final String ID = "ID";
        public static final String SKU = "SKU";
        public static final String ICON = "ICON";
        public static final String PRICE = "PRICE";
        public static final String CURRENCY = "CURRENCY";
        public static final String REQUIRES_QUANTITY = "REQUIRES_QUANTITY";
        public static final String MINIMUM_QUANTITY = "MINIMUM_QUANTITY";
        public static final String MAXIMUM_QUANTITY = "MAXIMUM_QUANTITY";
        public static final String DESCRIPTION = "DESCRIPTION";
        public static final String GALLONS = "GALLONS";
    }

    public static class ReceiptsTable {
        public static final String TABLE_NAME = "RECEIPTS";
        public static final String ID = "ID";
        public static final String CREATED_AT = "CREATED_AT";
        public static final String TOTAL_GALLONS = "TOTAL_GALLONS";
        public static final String TOTAL = "TOTAL";
    }

    public static class ConfigurationTable {
        public static final String TABLE_NAME = "CONFIGURATION";
        public static final String KEY = "KEY";
        public static final String VALUE = "VALUE";
    }

    public static class DeliveriesTable {
        public static final String TABLE_NAME = "DELIVERIES";
        public static final String ID = "ID";
        public static final String QUANTITY = "QUANTITY";
        public static final String DELIVERY_TYPE = "DELIVERY_TYPE";
        public static final String CREATED_AT = "CREATED_AT";
    }

    public static class PromotionsTable {
        public static final String TABLE_NAME = "PROMOTIONS";
        public static final String ID = "ID";
        public static final String APPLIES_TO = "APPLIES_TO";
        public static final String PRODUCT_SKU = "PRODUCT_SKU";
        public static final String START_DATE = "START_DATE";
        public static final String END_DATE = "END_DATE";
        public static final String AMOUNT = "AMOUNT";
        public static final String TYPE = "TYPE";
        public static final String ICON = "ICON";
        public static final String SKU = "SKU";
    }

    public static class SamplingSitesTable {
        public static final String TABLE_NAME = "SAMPLING_SITES";
        public static final String ID = "ID";
        public static final String NAME = "NAME";
    }

    public static class ParametersTable {
        public static final String TABLE_NAME = "PARAMETERS";
        public static final String ID = "ID";
        public static final String NAME = "NAME";
        public static final String UNIT_OF_MEASURE = "UNIT_OF_MEASURE";
        public static final String MINIMUM = "MINIMUM";
        public static final String MAXIMUM = "MAXIMUM";
        public static final String IS_OK_NOT_OK = "IS_OK_NOT_OK";
    }

    public static class SamplingSitesParametersTable {
        public static final String TABLE_NAME = "SAMPLING_SITES_PARAMETERS";
        public static final String ID = "ID";
        public static final String SITE_ID = "SITE_ID";
        public static final String PARAMETER_ID = "PARAMETER_ID";
    }

    public static class ReadingsTable {
        public static final String TABLE_NAME = "READINGS";
        public static final String ID = "ID";
        public static final String SAMPLING_SITE_NAME = "SAMPLING_SITE_NAME";
        public static final String CREATED_DATE = "CREATED_DATE";
    }

    public static class MeasurementsTable {
        public static final String TABLE_NAME = "MEASUREMENTS";
        public static final String ID = "ID";
        public static final String PARAMETER_NAME = "PARAMETER_NAME";
        public static final String VALUE = "VALUE";
        public static final String READING_ID = "READING_ID";
    }
}
