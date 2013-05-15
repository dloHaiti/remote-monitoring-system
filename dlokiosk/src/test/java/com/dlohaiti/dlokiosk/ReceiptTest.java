package com.dlohaiti.dlokiosk;

import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.Receipt;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReceiptTest {

    private List<Product> products;

    @Before
    public void setUp() {
        products = new ArrayList<Product>();
    }

    @Test
    public void shouldGenerateReceiptForOneProductOrder() {
        products.add(new Product(1L, "5GAL", null));

        Receipt receipt = new Receipt(products);

        assertThat(receipt.getTotalItems(), is(1));
        assertThat(receipt.getTotalProducts(), is(1));
    }

    @Test
    public void shouldGenerateReceiptForMultipleProductsWith1Quantity() {
        products.add(new Product(1L, "5GAL", null));
        products.add(new Product(2L, "10GAL", null));

        Receipt receipt = new Receipt(products);

        assertThat(receipt.getTotalItems(), is(2));
        assertThat(receipt.getTotalProducts(), is(2));
    }

    @Test
    public void shouldGenerateReceiptForMultipleProductsWithMultipleQuantities() {
        products.add(new Product(1L, "5GAL", null));
        products.add(new Product(1L, "5GAL", null));
        products.add(new Product(1L, "5GAL", null));
        products.add(new Product(2L, "10GAL", null));

        Receipt receipt = new Receipt(products);

        assertThat(receipt.getTotalItems(), is(4));
        assertThat(receipt.getTotalProducts(), is(2));
    }

    @Test
    public void shouldHaveTimestampOfWhenReceiptWasCreated() {
        products.add(new Product(1L, "5GAL", null));
        Date now = new Date();

        Receipt receipt = new Receipt(products, now);

        assertThat(receipt.getCreatedAt(), is(now));
    }

    @Test
    public void shouldKnowWhichKioskItCameFrom() {
        products.add(new Product(1L, "5GAL", null));

        Receipt receipt = new Receipt(products, new Date(), "k1");

        assertThat(receipt.getKioskId(), is("k1"));
    }
}
