package com.dlohaiti.dlokiosk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class KioskDatabase extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "kiosk.db";
    private final static int DATABASE_VERSION = 2;

    @Inject
    public KioskDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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
                        "%s TEXT," +
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
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                DeliveriesTable.TABLE_NAME,
                DeliveriesTable.ID,
                DeliveriesTable.DELIVERY_TYPE,
                DeliveriesTable.QUANTITY,
                DeliveriesTable.CREATED_DATE,
                DeliveriesTable.AGENT_NAME
        );
        String createDeliveryAgents = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s TEXT" +
                        ")",
                DeliveryAgentsTable.TABLE_NAME,
                DeliveryAgentsTable.ID,
                DeliveryAgentsTable.NAME
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
                        "%s TEXT," +
                        "%s INTEGER" +
                        ")",
                ParametersTable.TABLE_NAME,
                ParametersTable.ID,
                ParametersTable.NAME,
                ParametersTable.UNIT_OF_MEASURE,
                ParametersTable.MINIMUM,
                ParametersTable.MAXIMUM,
                ParametersTable.IS_OK_NOT_OK,
                ParametersTable.PRIORITY
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

        String createSalesChannels = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                SalesChannelsTable.TABLE_NAME,
                SalesChannelsTable.ID,
                SalesChannelsTable.NAME,
                SalesChannelsTable.DESCRIPTION
        );

        String createCustomerAccounts = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s INTEGER" +
                        ")",
                CustomerAccountsTable.TABLE_NAME,
                CustomerAccountsTable.ID,
                CustomerAccountsTable.NAME,
                CustomerAccountsTable.CONTACT_NAME,
                CustomerAccountsTable.ADDRESS,
                CustomerAccountsTable.PHONE_NUMBER,
                CustomerAccountsTable.KIOSK_ID
        );

        String createSalesChannelCustomerAccounts = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER ," +
                        "%s INTEGER " +
                        ")",
                SalesChannelCustomerAccountsTable.TABLE_NAME,
                SalesChannelCustomerAccountsTable.CUSTOMER_ACCOUNT_ID,
                SalesChannelCustomerAccountsTable.SALES_CHANNEL_ID
        );

        String insertConfig = String.format(
                "INSERT INTO %s(%s, %s) VALUES (?, ?)",
                ConfigurationTable.TABLE_NAME,
                ConfigurationTable.KEY,
                ConfigurationTable.VALUE
        );


        db.execSQL(createDeliveryAgents);
        db.execSQL(createMeasurements);
        db.execSQL(createMeasurementLineItems);
        db.execSQL(createSamplingSites);
        db.execSQL(createParameters);
        db.execSQL(createSamplingSiteParameters);
        db.execSQL(createReceipts);
        db.execSQL(createReceiptLineItems);
        db.execSQL(createProducts);
        db.execSQL(createConfiguration);
        db.execSQL(createDeliveries);
        db.execSQL(createPromotions);
        db.execSQL(createSalesChannels);
        db.execSQL(createCustomerAccounts);
        db.execSQL(createSalesChannelCustomerAccounts);
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.KIOSK_ID.name(), "kiosk01"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.KIOSK_PASSWORD.name(), "pw"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.DELIVERY_TRACKING_MIN.name(), "0"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.DELIVERY_TRACKING_MAX.name(), "24"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.DELIVERY_TRACKING_DEFAULT.name(), "24"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.SERVER_URL.name(), "http://10.0.2.2:8080/dloserver"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.LAST_UPDATE.name(), "20130615"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
        public static final String CREATED_AT = "CREATED_DATE";
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
        public static final String CREATED_DATE = "CREATED_DATE";
        public static final String AGENT_NAME = "AGENT_NAME";
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
        public static final String PRIORITY = "PRIORITY";
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

    public static class DeliveryAgentsTable {
        public static final String TABLE_NAME = "DELIVERY_AGENTS";
        public static final String ID = "ID";
        public static final String NAME = "NAME";
    }

    public static class SalesChannelsTable {
        public static final String TABLE_NAME = "SALES_CHANNELS";
        public static final String ID = "ID";
        public static final String NAME = "NAME";
        public static final String DESCRIPTION = "DESCRIPTION";
    }

    public static class CustomerAccountsTable {
        public static final String TABLE_NAME = "CUSTOMER_ACCOUNTS";
        public static final String ID = "ID";
        public static final String NAME = "NAME";
        public static final String CONTACT_NAME = "CONTACT_NAME";
        public static final String ADDRESS = "ADDRESS";
        public static final String PHONE_NUMBER = "PHONE_NUMBER";
        public static final String KIOSK_ID = "KIOSK_ID";
    }

    public static class SalesChannelCustomerAccountsTable {
        public static final String TABLE_NAME = "SALES_CHANNEL_CUSTOMER_ACCOUNTS";
        public static final String CUSTOMER_ACCOUNT_ID = "CUSTOMER_ACCOUNT_ID";
        public static final String SALES_CHANNEL_ID = "SALES_CHANNEL_ID";
    }
}
