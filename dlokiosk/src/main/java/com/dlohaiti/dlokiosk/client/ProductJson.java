package com.dlohaiti.dlokiosk.client;

public class ProductJson {
    private String sku;
    private String description;
    private Integer gallons;
    private Integer minimumQuantity;
    private Integer maximumQuantity;
    private boolean requiresQuantity;
    private MoneyJson price;
    private String base64EncodedImage;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGallons() {
        return gallons;
    }

    public void setGallons(Integer gallons) {
        this.gallons = gallons;
    }

    public Integer getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public Integer getMaximumQuantity() {
        return maximumQuantity;
    }

    public void setMaximumQuantity(Integer maximumQuantity) {
        this.maximumQuantity = maximumQuantity;
    }

    public boolean isRequiresQuantity() {
        return requiresQuantity;
    }

    public void setRequiresQuantity(boolean requiresQuantity) {
        this.requiresQuantity = requiresQuantity;
    }

    public MoneyJson getPrice() {
        return price;
    }

    public void setPrice(MoneyJson price) {
        this.price = price;
    }

    public String getBase64EncodedImage() {
        return base64EncodedImage;
    }

    public void setBase64EncodedImage(String base64EncodedImage) {
        this.base64EncodedImage = base64EncodedImage;
    }
}
