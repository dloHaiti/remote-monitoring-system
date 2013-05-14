package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.domain.Reading;
import com.dlohaiti.dlokiosk.domain.Sale;
import com.google.inject.Inject;

public class SalesClient {
    private RestClient restClient;

    @Inject
    public SalesClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean send(Sale sale) {
        return restClient.post("/sales", sale);
    }
}
