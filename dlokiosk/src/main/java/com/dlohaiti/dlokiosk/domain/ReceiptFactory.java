package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

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

    public Receipt makeReceipt(List<Product> products) {
        Kiosk kiosk = configurationRepository.getKiosk();
        List<OrderedProduct> orderedProducts = buildOrderedProducts(products);
        return new Receipt(orderedProducts, kiosk.getId(), clock.now());
    }

    private List<OrderedProduct> buildOrderedProducts(List<Product> products) {
        List<OrderedProduct> orderedProducts = new ArrayList<OrderedProduct>();
        for (Product product : products) {
            orderedProducts.add(new OrderedProduct(product.getSku(), product.getQuantity()));
        }
        return orderedProducts;
    }
}
