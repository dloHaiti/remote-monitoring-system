package com.dlohaiti.dlokiosknew.db;

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
                        "%s DOUBLE," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s INTEGER," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                ReceiptsTable.TABLE_NAME,
                ReceiptsTable.ID,
                ReceiptsTable.TOTAL_GALLONS,
                ReceiptsTable.TOTAL,
                ReceiptsTable.CREATED_AT,
                ReceiptsTable.SALES_CHANNEL_ID,
                ReceiptsTable.CUSTOMER_ACCOUNT_ID,
                ReceiptsTable.PAYMENT_MODE,
                ReceiptsTable.IS_SPONSOR_SELECTED,
                ReceiptsTable.SPONSOR_ID,
                ReceiptsTable.SPONSOR_AMOUNT,
                ReceiptsTable.CUSTOMER_AMOUNT,
                ReceiptsTable.PAYMENT_TYPE,
                ReceiptsTable.DELIVERY_TIME
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
                        "%s DOUBLE," +
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
                ProductsTable.GALLONS,
                ProductsTable.CATEGORY_ID
        );
        String createProductCategories = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                ProductsCategoryTable.TABLE_NAME,
                ProductsCategoryTable.ID,
                ProductsCategoryTable.NAME,
                ProductsCategoryTable.ICON
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
                ParametersTable.IS_USED_IN_TOTALIZER,
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
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                ReadingsTable.TABLE_NAME,
                ReadingsTable.ID,
                ReadingsTable.SAMPLING_SITE_NAME,
                ReadingsTable.CREATED_DATE,
                ReadingsTable.IS_SYNCED
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
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                SalesChannelsTable.TABLE_NAME,
                SalesChannelsTable.ID,
                SalesChannelsTable.NAME,
                SalesChannelsTable.DESCRIPTION,
                SalesChannelsTable.DELAYED_DELIVERY
        );

        String createCustomerTypes = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s TEXT" +
                        ")",
                CustomerTypeTable.TABLE_NAME,
                CustomerTypeTable.ID,
                CustomerTypeTable.NAME
        );

        String createCustomerAccounts = String.format(
                "CREATE TABLE %s(" +
                        "%s STRING PRIMARY KEY," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s INTEGER," +
                        "%s STRING," +
                        "%s TEXT" +
                        ")",
                CustomerAccountsTable.TABLE_NAME,
                CustomerAccountsTable.ID,
                CustomerAccountsTable.NAME,
                CustomerAccountsTable.CONTACT_NAME,
                CustomerAccountsTable.CUSTOMER_TYPE,
                CustomerAccountsTable.ADDRESS,
                CustomerAccountsTable.PHONE_NUMBER,
                CustomerAccountsTable.GPS_COORDINATES,
                CustomerAccountsTable.KIOSK_ID,
                CustomerAccountsTable.DUE_AMOUNT,
                CustomerAccountsTable.IS_SYNCED
        );

        String createSalesChannelCustomerAccounts = String.format(
                "CREATE TABLE %s(" +
                        "%s STRING," +
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

        String createSponsors = String.format(
                "CREATE TABLE %s(" +
                        "%s STRING PRIMARY KEY," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT" +
                        ")",
                SponsorsTable.TABLE_NAME,
                SponsorsTable.ID,
                SponsorsTable.NAME,
                SponsorsTable.CONTACT_NAME,
                SponsorsTable.PHONE_NUMBER,
                SponsorsTable.IS_SYNCED
        );

        String createSponsorCustomerAccounts = String.format(
                "CREATE TABLE %s(" +
                        "%s STRING ," +
                        "%s STRING " +
                        ")",
                SponsorCustomerAccountsTable.TABLE_NAME,
                SponsorCustomerAccountsTable.CUSTOMER_ACCOUNT_ID,
                SponsorCustomerAccountsTable.SPONSOR_ID
        );

        String createProductMrps = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER ," +
                        "%s INTEGER ," +
                        "%s STRING ," +
                        "%s STRING" +
                        ")",
                ProductMrpsTable.TABLE_NAME,
                ProductMrpsTable.PRODUCT_ID,
                ProductMrpsTable.CHANNEL_ID,
                ProductMrpsTable.PRICE,
                ProductMrpsTable.CURRENCY
        );


        String createPaymentHistory = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s STRING ," +
                        "%s INTEGER ," +
                        "%s STRING ," +
                        "%s TEXT" +
                        ")",
                PaymentHistoryTable.TABLE_NAME,
                PaymentHistoryTable.ID,
                PaymentHistoryTable.CUSTOMER_ACCOUNT_ID,
                PaymentHistoryTable.RECEIPT_ID,
                PaymentHistoryTable.AMOUNT,
                PaymentHistoryTable.PAYMENT_DATE

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
        db.execSQL(createProductCategories);
        db.execSQL(createConfiguration);
        db.execSQL(createDeliveries);
        db.execSQL(createPromotions);
        db.execSQL(createCustomerTypes);
        db.execSQL(createSalesChannels);
        db.execSQL(createCustomerAccounts);
        db.execSQL(createSalesChannelCustomerAccounts);
        db.execSQL(createSponsors);
        db.execSQL(createSponsorCustomerAccounts);
        db.execSQL(createProductMrps);
        db.execSQL(createPaymentHistory);
        insertKioskConfig(db, insertConfig);
    }

    private void insertKioskConfig(SQLiteDatabase db, String insertConfig) {
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.KIOSK_ID.name(), "Saintard"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.KIOSK_PASSWORD.name(), "pw1"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.DELIVERY_TRACKING_MIN.name(), "0"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.DELIVERY_TRACKING_MAX.name(), "24"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.DELIVERY_TRACKING_DEFAULT.name(), "24"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.SERVER_URL.name(), "http://104.131.40.239:8080/dloserver"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.LAST_UPDATE.name(), "20130615"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.UNIT_OF_MEASURE.name(), "gallon"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.CURRENCY.name(), "HTG"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.LOCALE.name(), "ht:HT"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.DATE_FORMAT.name(), "yyyy-MM-dd hh:mm:ss"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.PAYMENT_MODE.name(), "cash,mobile"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.PAYMENT_TYPE.name(), "Now,Post-Pay"});
        db.execSQL(insertConfig, new Object[]{ConfigurationKey.DELIVERY_TIME.name(), "Morning,Afternoon"});
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
        public static final String CATEGORY_ID = "CATEGORY_ID";
    }

    public static class ProductsCategoryTable {
        public static final String TABLE_NAME = "PRODUCT_CATEGORIES";
        public static final String ID = "ID";
        public static final String NAME = "NAME";
        public static final String ICON = "ICON";
    }

    public static class ReceiptsTable {
        public static final String TABLE_NAME = "RECEIPTS";
        public static final String ID = "ID";
        public static final String CREATED_AT = "CREATED_DATE";
        public static final String TOTAL_GALLONS = "TOTAL_GALLONS";
        public static final String TOTAL = "TOTAL";
        public static final String SALES_CHANNEL_ID = "SALES_CHANNEL_ID";
        public static final String CUSTOMER_ACCOUNT_ID = "CUSTOMER_ACCOUNT_ID";
        public static final String PAYMENT_MODE = "PAYMENT_MODE";
        public static final String IS_SPONSOR_SELECTED = "IS_SPONSOR_SELECTED";
        public static final String SPONSOR_ID = "SPONSOR_ID";
        public static final String SPONSOR_AMOUNT = "SPONSOR_AMOUNT";
        public static final String CUSTOMER_AMOUNT = "CUSTOMER_AMOUNT";
        public static final String PAYMENT_TYPE = "PAYMENT_TYPE";
        public static final String DELIVERY_TIME = "DELIVERY_TIME";
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
        public static final String IS_USED_IN_TOTALIZER = "IS_USED_IN_TOTALIZER";
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
        public static final String IS_SYNCED = "IS_SYNCED";
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
        public static final String DELAYED_DELIVERY = "DELAYED_DELIVERY";
    }

    public static class CustomerTypeTable {
        public static final String TABLE_NAME = "CUSTOMER_TYPES";
        public static final String ID = "ID";
        public static final String NAME = "NAME";
    }

    public static class CustomerAccountsTable {
        public static final String TABLE_NAME = "CUSTOMER_ACCOUNTS";
        public static final String ID = "ID";
        public static final String NAME = "NAME";
        public static final String CONTACT_NAME = "CONTACT_NAME";
        public static final String CUSTOMER_TYPE = "CUSTOMER_TYPE";
        public static final String ADDRESS = "ADDRESS";
        public static final String PHONE_NUMBER = "PHONE_NUMBER";
        public static final String GPS_COORDINATES = "GPS_COORDINATES";
        public static final String KIOSK_ID = "KIOSK_ID";
        public static final String DUE_AMOUNT = "DUE_DATE";
        public static final String IS_SYNCED = "IS_SYNCED";
    }

    public static class SalesChannelCustomerAccountsTable {
        public static final String TABLE_NAME = "SALES_CHANNEL_CUSTOMER_ACCOUNTS";
        public static final String CUSTOMER_ACCOUNT_ID = "CUSTOMER_ACCOUNT_ID";
        public static final String SALES_CHANNEL_ID = "SALES_CHANNEL_ID";
    }

    public static class SponsorsTable {
        public static final String TABLE_NAME = "SPONSORS";
        public static final String ID = "ID";
        public static final String NAME = "NAME";
        public static final String CONTACT_NAME = "CONTACT_NAME";
        public static final String PHONE_NUMBER = "PHONE_NUMBER";
        public static final String IS_SYNCED = "IS_SYNCED";
    }

    public static class SponsorCustomerAccountsTable {
        public static final String TABLE_NAME = "SPONSOR_CUSTOMER_ACCOUNTS";
        public static final String CUSTOMER_ACCOUNT_ID = "CUSTOMER_ACCOUNT_ID";
        public static final String SPONSOR_ID = "SPONSOR_ID";
    }

    public static class ProductMrpsTable {
        public static final String TABLE_NAME = "PRODUCT_MRPS";
        public static final String PRODUCT_ID = "PRODUCT_ID";
        public static final String CHANNEL_ID = "CHANNEL_ID";
        public static final String PRICE = "PRICE";
        public static final String CURRENCY = "CURRENCY";
    }

    public static class PaymentHistoryTable {
        public static final String TABLE_NAME = "PAYMENT_HISTORY";
        public static final String ID = "ID";
        public static final String CUSTOMER_ACCOUNT_ID = "CUSTOMER_ACCOUNT_ID";
        public static final String RECEIPT_ID = "RECEIPT_ID";
        public static final String AMOUNT = "AMOUNT";
        public static final String PAYMENT_DATE = "PAYMENT_DATE";
    }

}
