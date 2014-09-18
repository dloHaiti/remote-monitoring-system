package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.google.inject.Inject;

public class CustomerAccountClient{
    private final RestClient restClient;

    @Inject
    public CustomerAccountClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public PostResponse send(CustomerAccount account) {
        return restClient.post("/account", account);
    }
}
