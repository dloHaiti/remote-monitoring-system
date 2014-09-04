package com.dlohaiti.dlokiosk.domain;

import java.util.ArrayList;
import java.util.List;

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
}
