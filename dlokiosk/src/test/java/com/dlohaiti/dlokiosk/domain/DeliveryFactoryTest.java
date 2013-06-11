package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.DeliveryType;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class DeliveryFactoryTest {

    private Clock clock;
    private ConfigurationRepository configurationRepository;
    private DeliveryFactory factory;

    @Before
    public void setUp() {
        clock = mock(Clock.class);
        configurationRepository = mock(ConfigurationRepository.class);
        factory = new DeliveryFactory(clock, new KioskDate());

        given(clock.now()).willReturn(new Date(0));
        given(configurationRepository.getKiosk()).willReturn(new Kiosk("k1", "pw"));
    }

    @Test
    public void shouldGetTimeFromClock() {
        Delivery delivery = factory.makeDelivery(24, DeliveryType.RETURNED);
        assertThat(delivery.getCreatedAt(), is(new Date(0)));
    }
}
