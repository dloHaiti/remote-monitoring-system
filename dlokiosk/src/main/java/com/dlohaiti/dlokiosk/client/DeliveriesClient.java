package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.domain.Delivery;
import com.google.inject.Inject;

public class DeliveriesClient {
    private RestClient restClient;

    @Inject
    public DeliveriesClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public PostResponse send(Delivery delivery) {
        return restClient.post("/deliveries", delivery);
    }
}
