package com.dlohaiti.dlokiosknew.domain;

import java.util.ArrayList;
import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

public class Products extends ArrayList<Product> {
    public Products(Collection<Product> products) {
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
            if (product.getCategoryId() == categoryId && containsIgnoreCase(product.getDescription(), text)) {
                products.add(product);
            }
        }
        return products;
    }

    public void updateProductById(Long id, Product updatedProduct) {
        for (int i = 0; i < size(); i++) {
            if (get(i).getId().equals(id)) {
                set(i, updatedProduct);
                return;
            }
        }
    }

    public Integer getIndexOf(Product product) {
        for (int index = 0; index < size(); index++) {
            if (get(index).getId().equals(product.getId())) {
                return index;
            }
        }
        return null;
    }

    public Product findById(Long id) {
        for (Product product : this) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }
}
