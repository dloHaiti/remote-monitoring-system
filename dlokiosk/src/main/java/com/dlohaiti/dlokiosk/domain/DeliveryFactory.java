package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.DeliveryTrackingType;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;

public class DeliveryFactory {
    private final ConfigurationRepository configurationRepository;
    private final Clock clock;

    @Inject
    public DeliveryFactory(ConfigurationRepository configurationRepository, Clock clock) {
        this.configurationRepository = configurationRepository;
        this.clock = clock;
    }

    public Delivery makeDelivery(int quantity, DeliveryTrackingType type) {
        String kioskId = configurationRepository.getKiosk().getId();
        return new Delivery(quantity, type, clock.now(), kioskId);
    }
}
