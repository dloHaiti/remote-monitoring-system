package com.dlohaiti.dlokiosk.domain;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class ShoppingCartNew {
    private final Products products = new Products();
    private final Promotions promotions = new Promotions();
    private SalesChannel salesChannel;
    private CustomerAccount customerAccount;
    private final RegisterNew register;
    private String paymentType;
    public boolean isSponsorSelected;
    private Sponsor sponsor;
    private Money sponsorAmount;
    private Money customerAmount;

    @Inject
    public ShoppingCartNew(RegisterNew register) {
        this.register = register;
    }

    public void addOrUpdateProduct(Product newProduct) {
        Integer existingProductIndex = products.getIndexOf(newProduct);
        if (existingProductIndex != null) {
            products.set(existingProductIndex, newProduct);
        } else {
            products.add(newProduct);
        }
    }

    public Products getProducts() {
        return products;
    }

    public void checkout() {
        register.checkout(this);
        clear();
    }

    public void clear() {
        products.clear();
        promotions.clear();
        salesChannel = null;
        customerAccount = null;
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public Promotions getPromotions() {
        return promotions;
    }

    public void removePromotion(Promotion promotion) {
        promotions.remove(promotion);
    }

    public Money getSubtotal() {
        return register.subtotal(this);
    }

    public String getCurrencyCode() {
        String currencyCode = "";
        if (products.isEmpty()) {
            return currencyCode;
        } else {
            currencyCode = products.get(0).getPrice().getCurrencyCode();
        }
        return currencyCode;
    }

    public Money getTotal() {
        return register.total(this);
    }

    public void clearPromotions() {
        promotions.clear();
    }

    public SalesChannel salesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(SalesChannel channel) {
        this.salesChannel = channel;
    }

    public CustomerAccount customerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount account) {
        this.customerAccount = account;
    }

    public void addPromotions(List<Promotion> promotions) {
        this.promotions.addAll(promotions);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void overwrite(Promotions applicablePromotions) {
        clearPromotions();
        addPromotions(applicablePromotions);
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public void setSponsorAmount(Money sponsorAmount) {
        this.sponsorAmount = sponsorAmount;
        this.customerAmount = getTotal().minus(sponsorAmount.getAmount());
    }

    public Money customerAmount() {
        return customerAmount;
    }

    public Money sponsorAmount() {
        return sponsorAmount;
    }
}
