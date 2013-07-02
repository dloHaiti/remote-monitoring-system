package com.dlohaiti.dlokiosk.domain;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ShoppingCart {
    private final List<Product> products = new ArrayList<Product>();
    private final List<Promotion> promotions = new ArrayList<Promotion>();
    private final Register register;

    @Inject
    public ShoppingCart(Register register) {
        this.register = register;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(int position) {
        products.remove(position);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void checkout() {
        register.checkout(this);
        clear();
    }

    public void clear() {
        products.clear();
        promotions.clear();
    }

    public boolean isEmpty() {
        return products.isEmpty() && promotions.isEmpty();
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
}
