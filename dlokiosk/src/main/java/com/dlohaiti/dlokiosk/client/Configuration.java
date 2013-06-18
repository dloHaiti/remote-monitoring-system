package com.dlohaiti.dlokiosk.client;

import java.util.List;

public class Configuration {
    private List<ProductJson> products;
    private List<PromotionJson> promotions;
    private List<ParameterJson> parameters;
    private DeliveryJson delivery;

    public List<ProductJson> getProducts() {
        return products;
    }

    public void setProducts(List<ProductJson> products) {
        this.products = products;
    }

    public List<PromotionJson> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionJson> promotions) {
        this.promotions = promotions;
    }

    public List<ParameterJson> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterJson> parameters) {
        this.parameters = parameters;
    }

    public DeliveryJson getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryJson delivery) {
        this.delivery = delivery;
    }
}
