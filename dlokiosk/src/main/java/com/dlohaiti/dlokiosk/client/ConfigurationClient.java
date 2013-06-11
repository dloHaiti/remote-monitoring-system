package com.dlohaiti.dlokiosk.client;

import com.google.inject.Inject;

public class ConfigurationClient {
    private final RestClient client;

    @Inject
    public ConfigurationClient(RestClient client) {
        this.client = client;
    }

    public Configuration fetch() {
        return client.get("/configuration", Configuration.class);
    }
}
