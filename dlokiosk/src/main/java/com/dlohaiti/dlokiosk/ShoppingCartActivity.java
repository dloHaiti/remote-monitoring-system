package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.widget.GridView;
import com.dlohaiti.dlokiosk.adapter.ProductGridAdapter;
import com.dlohaiti.dlokiosk.adapter.PromotionGridAdapter;
import com.dlohaiti.dlokiosk.db.ProductCategoryRepository;
import com.dlohaiti.dlokiosk.db.ProductRepository;
import com.dlohaiti.dlokiosk.db.PromotionRepository;
import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.ProductCategories;
import com.dlohaiti.dlokiosk.domain.Promotion;
import com.dlohaiti.dlokiosk.domain.ShoppingCartNew;
import com.google.inject.Inject;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import roboguice.inject.InjectView;

import java.util.Collections;
import java.util.List;

public class ShoppingCartActivity extends SaleActivity {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private PromotionRepository promotionRepository;

    @Inject
    private ShoppingCartNew cart;

    @Inject
    private ProductCategoryRepository productCategoryRepository;

    @InjectView(R.id.product_grid)
    private StickyGridHeadersGridView productGrid;

    @InjectView(R.id.right_grid)
    private GridView promotionGrid;

    private PromotionGridAdapter promotionAdapter;
    private ProductGridAdapter productAdapter;
    private ProductCategories productCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        productCategories = new ProductCategories(productCategoryRepository.findAll());
        Collections.sort(cart.getProducts());
        productAdapter = new ProductGridAdapter(
                this,
                cart.getProducts(),
                productCategories,
                R.layout.layout_product_grid_header,
                R.layout.layout_product_grid_item);
        productGrid.setAdapter(productAdapter);

        List<Promotion> promotions = promotionRepository.list();
        cart.addPromotions(promotions);
        promotionAdapter = new PromotionGridAdapter(this, cart.getPromotions());
        promotionGrid.setAdapter(promotionAdapter);
    }

    @Override
    protected Class<? extends SaleActivity> nextActivity() {
        return PaymentActivity.class;
    }

    public void onProductRemoveButtonClick(Product product) {
        cart.removeProduct(product);
        productAdapter.notifyDataSetChanged();
    }

    public void onPromotionRemoveButtonClick(Promotion promotion) {
        cart.removePromotion(promotion);
        promotionAdapter.notifyDataSetChanged();
    }
}
