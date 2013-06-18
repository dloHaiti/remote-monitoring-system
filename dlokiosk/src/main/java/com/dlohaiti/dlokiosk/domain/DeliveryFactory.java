package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.DeliveryType;
import com.dlohaiti.dlokiosk.KioskDate;
import com.google.inject.Inject;

import java.text.ParseException;
import java.util.Date;

public class DeliveryFactory {
    private final Clock clock;
    private final KioskDate kioskDate;

    @Inject
    public DeliveryFactory(Clock clock, KioskDate kioskDate) {
        this.clock = clock;
        this.kioskDate = kioskDate;
    }

    public Delivery makeDelivery(int quantity, DeliveryType type, String agentName) {
        return new Delivery(quantity, type, clock.now(), agentName);
    }

    public Delivery makeDelivery(Integer id, Integer quantity, String deliveryType, String createdAtDate, String agentName) {
        DeliveryType type = DeliveryType.valueOf(deliveryType);
        Date createdDate = null;
        try {
            createdDate = kioskDate.getFormat().parse(createdAtDate);
        } catch (ParseException e) {
        }
        return new Delivery(id, quantity, type, createdDate, agentName);
    }
}
