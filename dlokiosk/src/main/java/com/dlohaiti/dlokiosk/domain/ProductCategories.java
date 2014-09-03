package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.exception.NoSalesChannelWithGivenIdException;

import java.util.ArrayList;
import java.util.List;

public class ProductCategories extends ArrayList<ProductCategory> {
    public ProductCategories(List<ProductCategory> categories) {
        super(categories);
    }

    public ProductCategory findProductCategoryById(long id) {
        for (ProductCategory productCategory : this) {
            if (productCategory.id() == id) {
                return productCategory;
            }
        }
        throw new NoSalesChannelWithGivenIdException(id);
    }
}
