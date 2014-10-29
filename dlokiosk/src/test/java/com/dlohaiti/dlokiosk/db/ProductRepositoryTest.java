package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.Base64ImageConverter;
import com.dlohaiti.dlokiosk.domain.Money;
import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.ProductMrp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.KioskDatabase.ProductMrpsTable;
import static com.dlohaiti.dlokiosk.db.KioskDatabase.ProductsTable;
import static com.dlohaiti.dlokiosk.domain.ProductBuilder.productBuilder;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ProductRepositoryTest {
    KioskDatabase db = new KioskDatabase(Robolectric.application.getApplicationContext());
    ProductRepository repository;
    private Base64ImageConverter imageConverter;

    @Before
    public void setUp() {
        Context applicationContext = Robolectric.application.getApplicationContext();
        imageConverter = new Base64ImageConverter(applicationContext);
        repository = new ProductRepository(db, imageConverter);
    }

    @Test
    public void shouldReturnEmptyListWhenNoProducts() {
        List<Product> products = repository.list();
        assertThat(products.size(), is(0));
    }

    @Test
    public void shouldReturnProductsWithPriceBasedOnSalesChannel() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        saveProductMrps(wdb,
                asList(new ProductMrp(1, 1, new Money(new BigDecimal(1.0), "INR")),
                        new ProductMrp(1, 2, new Money(new BigDecimal(2.0), "INR")),
                        new ProductMrp(2, 1, new Money(new BigDecimal(5.0), "INR"))));
        saveProduct(wdb,
                asList(productBuilder().withId(1l).withSku("P1").withPrice(3.0).build(),
                        productBuilder().withId(2l).withSku("P2").withPrice(4.0).build()));

        List<Product> products = repository.findProductsWithPriceFor(1);
        assertThat(products.size(), is(2));
        assertThat(products.get(0).getPrice().getAmount(), is(roundedBigDecimal(1.00)));
        assertThat(products.get(1).getPrice().getAmount(), is(roundedBigDecimal(5.00)));

        products = repository.findProductsWithPriceFor(2);
        assertThat(products.size(), is(1));
        assertThat(products.get(0).getPrice().getAmount(), is(roundedBigDecimal(2.00)));
    }

    private BigDecimal roundedBigDecimal(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
    }

    private void saveProduct(SQLiteDatabase wdb, List<Product> products) {
        for (Product product : products) {
            ContentValues values = new ContentValues();
            values.put(ProductsTable.ID, product.getId());
            values.put(ProductsTable.SKU, product.getSku());
            values.put(ProductsTable.PRICE, product.getPrice().getAmount().toString());
            values.put(ProductsTable.ICON, imageConverter.toBase64EncodedString(product.getImageResource()));
            values.put(ProductsTable.MINIMUM_QUANTITY, product.getMinimumQuantity());
            values.put(ProductsTable.MAXIMUM_QUANTITY, product.getMaximumQuantity());
            values.put(ProductsTable.REQUIRES_QUANTITY, String.valueOf(product.requiresQuantity()));
            values.put(ProductsTable.CURRENCY, product.getPrice().getCurrencyCode());
            values.put(ProductsTable.CATEGORY_ID, product.getCategoryId());
            wdb.insert(ProductsTable.TABLE_NAME, null, values);

        }
    }

    private void saveProductMrps(SQLiteDatabase wdb, List<ProductMrp> mrps) {
        for (ProductMrp mrp : mrps) {
            ContentValues values = new ContentValues();
            values.put(ProductMrpsTable.PRODUCT_ID, mrp.productId());
            values.put(ProductMrpsTable.CHANNEL_ID, mrp.channelId());
            values.put(ProductMrpsTable.PRICE, mrp.price().amountAsString());
            values.put(ProductMrpsTable.CURRENCY, mrp.price().getCurrencyCode());

            wdb.insert(ProductMrpsTable.TABLE_NAME, null, values);
        }
    }
}
