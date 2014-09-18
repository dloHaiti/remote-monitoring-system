package com.dlohaiti.dlokiosk.domain;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ShoppingCartNew {
    private final Products products = new Products();
    private final List<Promotion> promotions = new ArrayList<Promotion>();
    private SalesChannel salesChannel;
    private CustomerAccount customerAccount;
    private final RegisterNew register;

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

    public void removeProduct(int position) {
        products.remove(position);
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

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);
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

    public BigDecimal getTotal() {
        return register.total(this).getAmount();
    }

    public void removePromotion(int id) {
        promotions.remove(id);
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
}
