package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.domain.Money;
import com.dlohaiti.dlokiosk.domain.ProductMrp;
import com.dlohaiti.dlokiosk.domain.ProductMrps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.math.BigDecimal;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.KioskDatabase.ProductMrpsTable;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ProductMrpRepositoryTest {

    KioskDatabase db = new KioskDatabase(Robolectric.application.getApplicationContext());
    ProductMrpRepository repository;

    @Before
    public void setUp() {
        repository = new ProductMrpRepository(db);
    }

    @Test
    public void shouldReturnEmptySetWhenNoProductMrpsArePresent() {
        ProductMrps list = repository.findAll();
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldReturnAllSponsorsInSetInAlphabeticalOrder() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        List<ProductMrp> productMrps = asList(new ProductMrp(1, 1, new Money(new BigDecimal(1.0), "HTG")),
                new ProductMrp(2, 2, new Money(new BigDecimal(2.0), "HTG")));
        saveProductMrps(wdb, productMrps);

        ProductMrps list = repository.findAll();

        assertThat(list.size(), is(2));
        assertThat(list, is(asList(new ProductMrp(1, 1, new Money(new BigDecimal(1.0), "HTG")),
                new ProductMrp(2, 2, new Money(new BigDecimal(2.0), "HTG")))));
    }

    @Test
    public void shouldReplaceAll() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        saveProductMrps(wdb, asList(new ProductMrp(1, 1, new Money(new BigDecimal(1.0), "HTG")),
                new ProductMrp(2, 2, new Money(new BigDecimal(2.0), "HTG"))));
        assertThat(repository.findAll(),
                is(asList(new ProductMrp(1, 1, new Money(new BigDecimal(1.0), "HTG")),
                        new ProductMrp(2, 2, new Money(new BigDecimal(2.0), "HTG")))));

        boolean success = repository.replaceAll(new ProductMrps(asList(new ProductMrp(3, 3, new Money(new BigDecimal(3.0), "INR")),
                new ProductMrp(4, 4, new Money(new BigDecimal(4.0), "INR")))));

        assertThat(success, is(true));
        assertThat(repository.findAll(),
                is(asList(new ProductMrp(3, 3, new Money(new BigDecimal(3.0), "INR")),
                        new ProductMrp(4, 4, new Money(new BigDecimal(4.0), "INR")))));
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
