package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ReceiptsRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ShoppingCart {
    private final List<Product> products = new ArrayList<Product>();
    private final List<Promotion> promotions = new ArrayList<Promotion>();
    private final ReceiptsRepository repository;

    @Inject
    public ShoppingCart(ReceiptsRepository repository) {
        this.repository = repository;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void remove(int position) {
        products.remove(position);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void checkout() {
        repository.add(products, promotions);
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
}
