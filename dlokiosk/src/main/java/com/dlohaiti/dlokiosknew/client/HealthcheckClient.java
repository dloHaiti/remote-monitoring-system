package com.dlohaiti.dlokiosknew.client;

import com.google.inject.Inject;

public class HealthcheckClient {
    private RestClient restClient;

    @Inject
    public HealthcheckClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean getServerStatus() {
        HealthcheckStatus status = restClient.get("/healthcheck", HealthcheckStatus.class);

        return status.getDb();
    }
}
