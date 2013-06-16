package com.dlohaiti.dlokiosk.domain;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dlohaiti.dlokiosk.domain.ProductBuilder.productBuilder;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ReceiptFactoryTest {

    private Clock clock;
    private ReceiptFactory factory;

    @Before
    public void setUp() {
        clock = mock(Clock.class);
        given(clock.now()).willReturn(new Date(0));
        factory = new ReceiptFactory(clock);
    }

    @Test
    public void shouldUseTimeInformationFromClock() {
        List<Product> products = asList(productBuilder().build());

        Receipt receipt = factory.makeReceipt(products, new ArrayList<Promotion>(), new Money(BigDecimal.ZERO));

        assertThat(receipt.getCreatedDate(), is(new Date(0)));
    }


    @Test
    public void shouldGenerateLineItemsForDifferentProducts() {
        List<Product> products = asList(
                productBuilder().withSku("2GAL").build(),
                productBuilder().withSku("5GAL").build()
        );

        Receipt receipt = factory.makeReceipt(products, new ArrayList<Promotion>(), new Money(BigDecimal.ZERO));

        assertThat(receipt.getLineItemsCount(), is(2));
    }

    @Test
    public void shouldGenerateLineItemsForEachQuantityOfSameItem() {
        List<Product> products = asList(
                productBuilder().withSku("5GAL").build(),
                productBuilder().withSku("5GAL").build(),
                productBuilder().withSku("5GAL").build(),
                productBuilder().withSku("10GAL").build()
        );

        Receipt receipt = factory.makeReceipt(products, new ArrayList<Promotion>(), new Money(BigDecimal.ZERO));

        assertThat(receipt.getLineItemsCount(), is(4));
    }

    @Test
    public void shouldCalculateTotalGallonsFromProducts() {
        List<Product> products = asList(
                productBuilder().withGallons(5).withQuantity(1).build(),
                productBuilder().withGallons(5).withQuantity(1).build(),
                productBuilder().withGallons(10).withQuantity(2).build()
        );
        Receipt receipt = factory.makeReceipt(products, new ArrayList<Promotion>(), new Money(BigDecimal.ZERO));
        assertThat(receipt.getTotalGallons(), is(30));
    }

}
