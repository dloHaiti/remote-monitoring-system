package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.DeliveryType;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class DeliveryFactoryTest {

    private Clock clock;
    private DeliveryFactory factory;

    @Before
    public void setUp() {
        clock = mock(Clock.class);
        factory = new DeliveryFactory(clock);

        given(clock.now()).willReturn(new Date(0));
    }

    @Test
    public void shouldGetTimeFromClock() {
        Delivery delivery = factory.makeDelivery(24, DeliveryType.RETURNED, "");
        assertThat(delivery.getCreatedDate(), is(new Date(0)));
    }
}
