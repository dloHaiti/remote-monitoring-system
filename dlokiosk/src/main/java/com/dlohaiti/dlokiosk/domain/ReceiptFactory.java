package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.ReceiptLineItemType.PRODUCT;
import static com.dlohaiti.dlokiosk.db.ReceiptLineItemType.PROMOTION;

public class ReceiptFactory {
    private final ConfigurationRepository configurationRepository;
    private final Clock clock;

    @Inject
    public ReceiptFactory(ConfigurationRepository configurationRepository, Clock clock) {
        //TODO: this leaves when auth comes into the picture
        this.configurationRepository = configurationRepository;
        //TODO: move this to receipt when auth comes into picture
        this.clock = clock;
    }

    public Receipt makeReceipt(List<Product> products, List<Promotion> promotions) {
        Kiosk kiosk = configurationRepository.getKiosk();
        List<LineItem> lineItems = buildLineItems(products, promotions);
        return new Receipt(lineItems, kiosk.getId(), clock.now());
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
}
