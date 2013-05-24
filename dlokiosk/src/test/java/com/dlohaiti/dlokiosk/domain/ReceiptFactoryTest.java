package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import org.junit.Before;
import org.junit.Test;

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
    private ConfigurationRepository configurationRepository;
    private ReceiptFactory factory;

    @Before
    public void setUp() {
        clock = mock(Clock.class);
        configurationRepository = mock(ConfigurationRepository.class);
        given(configurationRepository.getKiosk()).willReturn(new Kiosk("k1", "pw"));
        given(clock.now()).willReturn(new Date(0));
        factory = new ReceiptFactory(configurationRepository, clock);
    }

    @Test
    public void shouldUseKioskInformationFromConfiguration() {
        List<Product> products = asList(productBuilder().build());

        Receipt receipt = factory.makeReceipt(products, new ArrayList<Promotion>());

        assertThat(receipt.getKioskId(), is("k1"));
    }

    @Test
    public void shouldUseTimeInformationFromClock() {
        List<Product> products = asList(productBuilder().build());

        Receipt receipt = factory.makeReceipt(products, new ArrayList<Promotion>());

        assertThat(receipt.getCreatedAt(), is(new Date(0)));
    }


    @Test
    public void shouldGenerateLineItemsForDifferentProducts() {
        List<Product> products = asList(
                productBuilder().withSku("2GAL").build(),
                productBuilder().withSku("5GAL").build()
        );

        Receipt receipt = factory.makeReceipt(products, new ArrayList<Promotion>());

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

        Receipt receipt = factory.makeReceipt(products, new ArrayList<Promotion>());

        assertThat(receipt.getLineItemsCount(), is(4));
    }

}
