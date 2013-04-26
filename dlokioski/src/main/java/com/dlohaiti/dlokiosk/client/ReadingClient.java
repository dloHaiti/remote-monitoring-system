package com.dlohaiti.dlokiosk.client;

public class ReadingClient {
    private RestClient restClient;

    public ReadingClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean getServerStatus() {
        HealthcheckStatus status = restClient.get("/healthcheck", HealthcheckStatus.class);

        return status.db;
    }
}
