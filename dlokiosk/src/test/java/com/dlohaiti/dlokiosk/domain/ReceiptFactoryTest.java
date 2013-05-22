package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

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
        List<Product> products = asList(new Product(1L, "5GAL", null));

        Receipt receipt = factory.makeReceipt(products);

        assertThat(receipt.getKioskId(), is("k1"));
    }

    @Test
    public void shouldUseTimeInformationFromClock() {
        List<Product> products = asList(new Product(1L, "5GAL", null));

        Receipt receipt = factory.makeReceipt(products);

        assertThat(receipt.getCreatedAt(), is(new Date(0)));
    }


    @Test
    public void shouldGenerateReceiptForMultipleProductsWith1Quantity() {
        List<Product> products = asList(new Product(1L, "5GAL", null), new Product(2L, "10GAL", null));

        Receipt receipt = factory.makeReceipt(products);

        assertThat(receipt.getLineItems(), is(2));
    }

    @Test
    public void shouldGenerateReceiptForMultipleProductsWithMultipleQuantities() {
        List<Product> products = asList(
                new Product(1L, "5GAL", null),
                new Product(1L, "5GAL", null),
                new Product(1L, "5GAL", null),
                new Product(2L, "10GAL", null)
        );

        Receipt receipt = factory.makeReceipt(products);

        assertThat(receipt.getLineItems(), is(4));
    }

}
