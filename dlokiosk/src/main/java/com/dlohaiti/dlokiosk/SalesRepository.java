package com.dlohaiti.dlokiosk;

import com.dlohaiti.dlokiosk.domain.Product;

import java.util.ArrayList;
import java.util.List;

public class SalesRepository {
    private List<Product> sales = new ArrayList<Product>();

    public List<Product> list() {
        return sales;
    }

    public void add(List<Product> products) {
        sales = products;
    }
}
