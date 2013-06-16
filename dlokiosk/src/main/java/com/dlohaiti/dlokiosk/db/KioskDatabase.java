package com.dlohaiti.dlokiosk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.dlohaiti.dlokiosk.domain.PromotionApplicationType;
import com.dlohaiti.dlokiosk.domain.PromotionType;
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

        String insertProduct = String.format(
                "INSERT INTO %s(%s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                ProductsTable.TABLE_NAME,
                ProductsTable.SKU,
                ProductsTable.REQUIRES_QUANTITY,
                ProductsTable.MINIMUM_QUANTITY,
                ProductsTable.MAXIMUM_QUANTITY,
                ProductsTable.PRICE,
                ProductsTable.CURRENCY,
                ProductsTable.DESCRIPTION,
                ProductsTable.GALLONS,
                ProductsTable.ICON
        );

        String insertConfig = String.format(
                "INSERT INTO %s(%s, %s) VALUES (?, ?)",
                ConfigurationTable.TABLE_NAME,
                ConfigurationTable.KEY,
                ConfigurationTable.VALUE
        );

        String insertPromo = String.format(
                "INSERT INTO %s(%s, %s, %s, %s, %s, %s, %s, %s) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                PromotionsTable.TABLE_NAME,
                PromotionsTable.APPLIES_TO,
                PromotionsTable.PRODUCT_SKU,
                PromotionsTable.START_DATE,
                PromotionsTable.END_DATE,
                PromotionsTable.AMOUNT,
                PromotionsTable.TYPE,
                PromotionsTable.SKU,
                PromotionsTable.ICON
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
        db.execSQL(insertPromo, new Object[]{PromotionApplicationType.BASKET.name(), "", "2013-01-01 00:00:00 EDT",
                "2013-12-01 00:00:00 EDT", "25", PromotionType.PERCENT.name(), "25P_OFF_BASKET", "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"});
        db.execSQL(insertPromo, new Object[]{PromotionApplicationType.BASKET.name(), "", "2013-01-01 00:00:00 EDT",
                "2013-12-01 00:00:00 EDT", "5", PromotionType.AMOUNT.name(), "5HTG_OFF_BASKET", "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"});
        db.execSQL(insertPromo, new Object[]{PromotionApplicationType.SKU.name(), "2GALLON", "2013-01-01 00:00:00 EDT",
                "2013-12-01 00:00:00 EDT", "1", PromotionType.AMOUNT.name(), "1HTG_OFF_2GALLON", "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"});
        db.execSQL(insertPromo, new Object[]{PromotionApplicationType.SKU.name(), "5GALLON", "2013-01-01 00:00:00 EDT",
                "2013-12-01 00:00:00 EDT", "10", PromotionType.PERCENT.name(), "10P_OFF_5GALLON", "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"});
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
