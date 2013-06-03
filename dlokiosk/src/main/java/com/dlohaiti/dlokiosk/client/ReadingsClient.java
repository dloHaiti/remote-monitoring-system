package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.domain.Reading;
import com.google.inject.Inject;

public class ReadingsClient {
    private final RestClient restClient;

    @Inject
    public ReadingsClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean send(Reading reading) {
        return restClient.post("/readings", reading);
    }
}
