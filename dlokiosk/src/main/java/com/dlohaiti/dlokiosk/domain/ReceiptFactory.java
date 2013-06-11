package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.ReceiptLineItemType.PRODUCT;
import static com.dlohaiti.dlokiosk.db.ReceiptLineItemType.PROMOTION;

public class ReceiptFactory {
    private final Clock clock;

    @Inject
    public ReceiptFactory(Clock clock) {
        this.clock = clock;
    }

    public Receipt makeReceipt(List<Product> products, List<Promotion> promotions, Money total) {
        List<LineItem> lineItems = buildLineItems(products, promotions);
        int totalGallons = 0;
        for(Product product: products) {
            totalGallons += (product.getGallons() * product.getQuantity());
        }
        return new Receipt(lineItems, clock.now(), totalGallons, total);
    }

    private List<LineItem> buildLineItems(List<Product> products, List<Promotion> promotions) {
        List<LineItem> lineItems = new ArrayList<LineItem>();
        for (Product product : products) {
            lineItems.add(new LineItem(product.getSku(), product.getQuantity(), product.getPrice().times(product.getQuantity()), PRODUCT));
        }
        for (Promotion promotion : promotions) {
            lineItems.add(new LineItem(promotion.getSku(), promotion.getQuantity(), new Money(BigDecimal.ZERO), PROMOTION));
        }
        return lineItems;
    }

    public Receipt makeReceipt(long id, List<LineItem> lineItems, Date date, Integer totalGallons, Money total) {
        return new Receipt(id, lineItems, date, totalGallons, total);
    }

}
