package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import com.dlohaiti.dlokiosk.adapter.ProductGridAdapter;
import com.dlohaiti.dlokiosk.db.ProductCategoryRepository;
import com.dlohaiti.dlokiosk.db.ProductRepository;
import com.dlohaiti.dlokiosk.domain.ProductCategories;
import com.google.inject.Inject;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import roboguice.inject.InjectView;

public class ShoppingCartActivity extends SaleActivity {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductCategoryRepository productCategoryRepository;

    @InjectView(R.id.product_grid)
    private StickyGridHeadersGridView productGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        productGrid.setAdapter(new ProductGridAdapter(
                getApplicationContext(), productRepository.list(), new ProductCategories(productCategoryRepository.findAll()),
                R.layout.layout_product_grid_header, R.layout.layout_product_grid_item));
    }

    @Override
    protected Class<? extends SaleActivity> nextActivity() {
        return null;
    }
}
