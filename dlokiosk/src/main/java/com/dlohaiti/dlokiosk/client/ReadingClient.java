package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.domain.Reading;
import com.google.inject.Inject;

public class ReadingClient {
    private RestClient restClient;

    @Inject
    public ReadingClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean send(Reading reading) {
        return restClient.post("/reading", reading);
    }
}
