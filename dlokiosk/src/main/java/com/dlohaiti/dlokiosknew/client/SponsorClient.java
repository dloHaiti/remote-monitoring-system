package com.dlohaiti.dlokiosknew.client;

import com.dlohaiti.dlokiosknew.domain.Sponsor;
import com.google.inject.Inject;

public class SponsorClient{
    private final RestClient restClient;

    @Inject
    public SponsorClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public PostResponse send(Sponsor sponsor) {
        return restClient.post("/sponsors", sponsor);
    }
}