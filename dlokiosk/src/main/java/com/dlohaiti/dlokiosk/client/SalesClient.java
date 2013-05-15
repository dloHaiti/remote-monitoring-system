package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.domain.ReceiptLineItem;
import com.google.inject.Inject;

public class SalesClient {
    private RestClient restClient;

    @Inject
    public SalesClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean send(ReceiptLineItem receiptLineItem) {
        return restClient.post("/sales", receiptLineItem);
    }
}
