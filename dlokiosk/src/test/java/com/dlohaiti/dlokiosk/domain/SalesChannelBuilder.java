package com.dlohaiti.dlokiosk.domain;

public class SalesChannelBuilder {
    private long id = 1;
    private String name = "SalesChannel1";
    private String description = "SalesChannel2";

    public SalesChannelBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public SalesChannelBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SalesChannelBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public SalesChannel build() {
        return new SalesChannel(id, name, description);
    }
}
