package com.dlohaiti.dlokiosk.domain;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

public class Sale {
    private final Long id;
    private final String sku;
    private final Integer quantity;
    private final DateTime createdAt;

    public Sale(Long id, String sku, Integer quantity, String createdAt) {
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
        this.createdAt = DateTime.parse(createdAt, ISODateTimeFormat.basicDateTime());
    }


}
