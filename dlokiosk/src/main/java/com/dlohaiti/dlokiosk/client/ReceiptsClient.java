package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.domain.Receipt;
import com.google.inject.Inject;

public class ReceiptsClient {
    private RestClient restClient;

    @Inject
    public ReceiptsClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public PostResponse send(Receipt receipt) {
        return restClient.post("/receipts", receipt);
    }
}
