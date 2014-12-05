package com.dlohaiti.dlokiosknew.client;

import com.dlohaiti.dlokiosknew.domain.PaymentHistory;
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
