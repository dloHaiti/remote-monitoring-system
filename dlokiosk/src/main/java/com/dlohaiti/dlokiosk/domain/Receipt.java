package com.dlohaiti.dlokiosk.domain;

import java.util.*;

public class Receipt {
    private List<OrderedProduct> orderedProducts = new ArrayList<OrderedProduct>();
    private Date createdAt;
    private String kioskId = "k1";

    public Receipt(Iterable<Product> products) {
        this(products, new Date(), "k1");
    }

    public Receipt(Iterable<Product> products, Date createdAt) {
        this(products, createdAt, "k1");
    }

    public Receipt(Iterable<Product> products, Date createdAt, String kioskId) {
        for (Product product : products) {
            boolean alreadyAdded = false;
            for (OrderedProduct ordered : orderedProducts) {
                if (ordered.getSku().equals(product.getSku())) {
                    alreadyAdded = true;
                    ordered.incrementQuantity();
                    break;
                }
            }
            if (!alreadyAdded) {
                orderedProducts.add(new OrderedProduct(product.getSku(), 1));
            }
        }
        this.createdAt = createdAt;
        this.kioskId = kioskId;
    }

    public Receipt(List<OrderedProduct> orderedProducts, Date createdAt) {
        this.orderedProducts = orderedProducts;
        this.createdAt = createdAt;
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public Integer getTotalItems() {
        int total = 0;
        for (OrderedProduct op : orderedProducts) {
            total += op.getQuantity();
        }
        return total;
    }

    public Integer getTotalProducts() {
        return orderedProducts.size();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getKioskId() {
        return kioskId;
    }
}
