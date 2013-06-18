package com.dlohaiti.dlokiosk.client;

public class DeliveryConfigurationJson {
    private Integer minimum;
    private Integer maximum;
    private Integer defaultValue;
    private Integer gallons;
    private MoneyJson price;

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public Integer getDefault() {
        return defaultValue;
    }

    public void setDefault(Integer defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getGallons() {
        return gallons;
    }

    public void setGallons(Integer gallons) {
        this.gallons = gallons;
    }

    public MoneyJson getPrice() {
        return price;
    }

    public void setPrice(MoneyJson price) {
        this.price = price;
    }
}
