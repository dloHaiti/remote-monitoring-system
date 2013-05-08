package com.dlohaiti.dlokiosk;

import com.dlohaiti.dlokiosk.domain.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private List<Product> products = new ArrayList<Product>();

    public List<Product> list() {
        products.add(new Product(1L, "5GAL"));
        products.add(new Product(2L, "10GAL"));
        products.add(new Product(3L, "15GAL"));
        return products;
    }

    public Product findById(Long id) {
        for(Product product : products) {
            if(id.equals(product.getId())) {
                return product;
            }
        }
        return new Product(null, null);
    }
}
