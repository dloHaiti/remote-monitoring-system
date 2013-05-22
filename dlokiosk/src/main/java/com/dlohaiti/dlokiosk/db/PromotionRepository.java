package com.dlohaiti.dlokiosk.db;

import com.google.inject.Inject;

public class PromotionRepository {
    private final KioskDatabase db;

    @Inject
    public PromotionRepository(KioskDatabase db) {
        this.db = db;
    }
}
