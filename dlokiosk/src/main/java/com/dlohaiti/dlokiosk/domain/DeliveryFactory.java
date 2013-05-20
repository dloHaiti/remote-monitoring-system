package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.DeliveryTrackingType;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;

import java.text.ParseException;
import java.util.Date;

public class DeliveryFactory {
    private final ConfigurationRepository configurationRepository;
    private final Clock clock;
    private final KioskDate kioskDate;

    @Inject
    public DeliveryFactory(ConfigurationRepository configurationRepository, Clock clock, KioskDate kioskDate) {
        this.configurationRepository = configurationRepository;
        this.clock = clock;
        this.kioskDate = kioskDate;
    }

    public Delivery makeDelivery(int quantity, DeliveryTrackingType type) {
        String kioskId = configurationRepository.getKiosk().getId();
        return new Delivery(quantity, type, clock.now(), kioskId);
    }

    public Delivery makeDelivery(Integer id, Integer quantity, String deliveryTrackingType, String kioskId, String createdAtDate) {
        DeliveryTrackingType type = DeliveryTrackingType.valueOf(deliveryTrackingType);
        Date createdAt = null;
        try {
            createdAt = kioskDate.getFormat().parse(createdAtDate);
        } catch (ParseException e) {
        }
        return new Delivery(id, quantity, type, createdAt, kioskId);
    }
}
