package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.DeliveryTrackingType;
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
        factory = new DeliveryFactory(configurationRepository, clock);

        given(clock.now()).willReturn(new Date(0));
        given(configurationRepository.getKiosk()).willReturn(new Kiosk("k1", "pw"));
    }

    @Test
    public void shouldGetTimeFromClock() {
        Delivery delivery = factory.makeDelivery(24, DeliveryTrackingType.RETURNED);
        assertThat(delivery.getCreatedAt(), is(new Date(0)));
    }

    @Test
    public void shouldGetKioskIdFromConfiguration() {
        Delivery delivery = factory.makeDelivery(24, DeliveryTrackingType.RETURNED);
        assertThat(delivery.getKioskId(), is("k1"));
    }
}
