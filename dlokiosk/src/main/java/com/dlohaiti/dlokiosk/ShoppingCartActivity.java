package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.widget.GridView;
import com.dlohaiti.dlokiosk.adapter.ProductGridAdapter;
import com.dlohaiti.dlokiosk.adapter.PromotionGridAdapter;
import com.dlohaiti.dlokiosk.db.ProductCategoryRepository;
import com.dlohaiti.dlokiosk.db.ProductRepository;
import com.dlohaiti.dlokiosk.db.PromotionRepository;
import com.dlohaiti.dlokiosk.domain.ProductCategories;
import com.dlohaiti.dlokiosk.domain.Promotion;
import com.google.inject.Inject;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import roboguice.inject.InjectView;

import java.util.List;

public class ShoppingCartActivity extends SaleActivity {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private PromotionRepository promotionRepository;

    @Inject
    private ProductCategoryRepository productCategoryRepository;

    @InjectView(R.id.product_grid)
    private StickyGridHeadersGridView productGrid;

    @InjectView(R.id.right_grid)
    private GridView promotionGrid;

    private PromotionGridAdapter promotionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        productGrid.setAdapter(new ProductGridAdapter(
                getApplicationContext(), productRepository.list(), new ProductCategories(productCategoryRepository.findAll()),
                R.layout.layout_product_grid_header, R.layout.layout_product_grid_item));

        List<Promotion> promotions = promotionRepository.list();
        promotionAdapter = new PromotionGridAdapter(this, promotions);
        promotionGrid.setAdapter(promotionAdapter);
    }

    @Override
    protected Class<? extends SaleActivity> nextActivity() {
        return PaymentActivity.class;
    }
}
