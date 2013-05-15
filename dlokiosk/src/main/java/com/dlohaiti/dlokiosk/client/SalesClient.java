package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.domain.Receipt;
import com.google.inject.Inject;

public class SalesClient {
    private RestClient restClient;

    @Inject
    public SalesClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean send(Receipt receipt) {
        return restClient.post("/sales", receipt);
    }
}
