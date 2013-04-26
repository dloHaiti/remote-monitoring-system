package com.dlohaiti.dlokiosk.client;

public class HealthcheckClient {
    private RestClient restClient;

    public HealthcheckClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean getServerStatus() {
        HealthcheckStatus status = restClient.get("/healthcheck", HealthcheckStatus.class);

        return status.getDb();
    }
}
