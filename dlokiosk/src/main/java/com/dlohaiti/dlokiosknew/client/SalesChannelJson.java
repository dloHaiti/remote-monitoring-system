package com.dlohaiti.dlokiosknew.client;

public class SalesChannelJson {
    private long id;
    private String name;
    private String description;
    private Boolean delayedDelivery;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDelayedDelivery() {
        return delayedDelivery;
    }

    public void setDelayedDelivery(Boolean delayedDelivery) {
        this.delayedDelivery = delayedDelivery;
    }
}
