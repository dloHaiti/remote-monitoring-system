package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.DeliveryType;
import com.google.inject.Inject;

public class DeliveryFactory {
    private final Clock clock;

    @Inject
    public DeliveryFactory(Clock clock) {
        this.clock = clock;
    }

    public Delivery makeDelivery(int quantity, DeliveryType type, String agentName) {
        return new Delivery(quantity, type, clock.now(), agentName);
    }

}
