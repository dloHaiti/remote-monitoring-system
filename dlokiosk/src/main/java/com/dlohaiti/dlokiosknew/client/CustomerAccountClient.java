package com.dlohaiti.dlokiosknew.client;

import com.dlohaiti.dlokiosknew.domain.CustomerAccount;
import com.google.inject.Inject;

public class CustomerAccountClient{
    private final RestClient restClient;

    @Inject
    public CustomerAccountClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public PostResponse send(CustomerAccount account) {
        return restClient.post("/accounts", account);
    }
}
