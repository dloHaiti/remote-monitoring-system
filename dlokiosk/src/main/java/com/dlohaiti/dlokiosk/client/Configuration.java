package com.dlohaiti.dlokiosk.client;

import java.util.List;

public class Configuration {
    private List<ProductJson> products;
    private List<PromotionJson> promotions;
    private List<ParameterJson> parameters;
    private DeliveryJson delivery;
    private List<SalesChannelJson> salesChannels;
    private List<CustomerAccountJson> customerAccounts;
    private List<ProductCategoryJson> productCategories;
    private ConfigurationJson configuration;

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

    public List<SalesChannelJson> getSalesChannels() {
        return salesChannels;
    }

    public void setSalesChannels(List<SalesChannelJson> salesChannels) {
        this.salesChannels = salesChannels;
    }

    public List<CustomerAccountJson> getCustomerAccounts() {
        return customerAccounts;
    }

    public void setCustomerAccounts(List<CustomerAccountJson> customerAccounts) {
        this.customerAccounts = customerAccounts;
    }

    public List<ProductCategoryJson> getProductCategories() {
        return this.productCategories;
    }

    public void setProductCategories(List<ProductCategoryJson> productCategories) {
        this.productCategories = productCategories;
    }

    public ConfigurationJson getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ConfigurationJson configuration) {
        this.configuration = configuration;
    }
}
