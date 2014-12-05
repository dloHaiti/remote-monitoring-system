package com.dlohaiti.dlokiosknew.client;

import com.dlohaiti.dlokiosknew.domain.Delivery;
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
