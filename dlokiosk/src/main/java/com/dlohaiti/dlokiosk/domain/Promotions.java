package com.dlohaiti.dlokiosk.domain;

import java.util.ArrayList;

public class Promotions extends ArrayList<Promotion> {
    public Promotions findApplicablePromotionsForProducts(Products products) {
        Promotions applicablePromotions = new Promotions();
        for (Promotion p : this) {
            if (p.appliesTo(products)) {
                applicablePromotions.add(p);
            }
        }
        return applicablePromotions;
    }
}
