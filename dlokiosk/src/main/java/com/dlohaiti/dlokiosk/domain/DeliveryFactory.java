package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.DeliveryType;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
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

    public Delivery makeDelivery(int quantity, DeliveryType type) {
        return new Delivery(quantity, type, clock.now());
    }

    public Delivery makeDelivery(Integer id, Integer quantity, String deliveryType, String createdAtDate) {
        DeliveryType type = DeliveryType.valueOf(deliveryType);
        Date createdAt = null;
        try {
            createdAt = kioskDate.getFormat().parse(createdAtDate);
        } catch (ParseException e) {
        }
        return new Delivery(id, quantity, type, createdAt);
    }
}
