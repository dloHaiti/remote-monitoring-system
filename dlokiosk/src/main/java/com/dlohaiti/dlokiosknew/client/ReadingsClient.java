package com.dlohaiti.dlokiosknew.client;

import com.dlohaiti.dlokiosknew.domain.Reading;
import com.google.inject.Inject;

public class ReadingsClient {
    private final RestClient restClient;

    @Inject
    public ReadingsClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public PostResponse send(Reading reading) {
        return restClient.post("/readings", reading);
    }
}

