package com.dlohaiti.dlokiosk.domain;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

public class Products extends ArrayList<Product> {
    public Products(List<Product> products) {
        super(products);
    }

    public Products() {
    }

    public Products findAccountsByCategoryId(long categoryId) {
        Products products = new Products();
        for (Product product : this) {
            if (product.getCategoryId() == categoryId) {
                products.add(product);
            }
        }
        return products;
    }

    public Products filterBy(String text, long categoryId) {
        Products products = new Products();
        for (Product product : this) {
            if (product.getCategoryId() == categoryId && containsIgnoreCase(product.getSku(), text)) {
                products.add(product);
            }
        }
        return products;
    }
}
