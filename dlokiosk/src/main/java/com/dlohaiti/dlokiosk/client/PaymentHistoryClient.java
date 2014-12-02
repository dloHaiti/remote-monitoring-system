package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.domain.PaymentHistory;
import com.dlohaiti.dlokiosk.domain.Receipt;
import com.google.inject.Inject;

public class PaymentHistoryClient {
    private RestClient restClient;

    @Inject
    public PaymentHistoryClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public PostResponse send(PaymentHistory history) {
        return restClient.post("/history", history);
    }
}
