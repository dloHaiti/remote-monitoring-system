package com.dlohaiti.dlokiosk.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Receipt {
    private final Map<String, Integer> orderedProducts = new HashMap<String, Integer>();
    private final Date createdAt;
    private final String kioskId;

    public Receipt(Iterable<Product> products) {
        this(products, new Date(), "k1");
    }

    public Receipt(Iterable<Product> products, Date createdAt) {
        this(products, createdAt, "k1");
    }

    public Receipt(Iterable<Product> products, Date createdAt, String kioskId) {
        for (Product p : products) {
            if (orderedProducts.containsKey(p.getSku())) {
                orderedProducts.put(p.getSku(), orderedProducts.get(p.getSku()) + 1);
            } else {
                orderedProducts.put(p.getSku(), 1);
            }
        }
        this.createdAt = createdAt;
        this.kioskId = kioskId;
    }

    public Integer getTotalItems() {
        int total = 0;
        for (Integer itemTotal : orderedProducts.values()) {
            total += itemTotal;
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
