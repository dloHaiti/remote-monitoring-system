package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.dlohaiti.dlokiosk.adapter.ProductGridAdapter;
import com.dlohaiti.dlokiosk.adapter.PromotionGridAdapter;
import com.dlohaiti.dlokiosk.db.ProductCategoryRepository;
import com.dlohaiti.dlokiosk.db.ProductRepository;
import com.dlohaiti.dlokiosk.db.PromotionRepository;
import com.dlohaiti.dlokiosk.domain.*;
import com.google.inject.Inject;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import roboguice.inject.InjectView;

import java.util.Collections;

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
    private StickyGridHeadersGridView productGridView;

    @InjectView(R.id.right_grid)
    private GridView promotionGridView;

    @InjectView(R.id.total_price)
    private TextView totalPriceView;

    @InjectView(R.id.total_price_currency)
    private TextView totalPriceCurrencyView;

    @InjectView(R.id.discounted_price)
    private TextView discountedPriceView;

    @InjectView(R.id.discounted_price_currency)
    private TextView discountedPriceCurrencyView;

    @InjectView(R.id.sales_channel)
    private TextView salesChannelView;

    @InjectView(R.id.customer_account)
    private TextView customerAccountView;

    // Added for Disabling the Discounted price when there are no discounts for it.

    @InjectView(R.id.discounted_price_label)
    private TextView discountedPriceLabel;

    @InjectView(R.id.discounted_price)
    private TextView discountedPrice;

    @InjectView(R.id.discounted_price_currency)
    private TextView discountedPriceCurrency;

    @InjectView(R.id.choose_promotion_label)
    private TextView choosePromotionLabel;

    private PromotionGridAdapter promotionAdapter;
    private ProductGridAdapter productAdapter;
    private Promotions allPromotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        initialiseProductGrid();
        initialisePromotionGrid();
        initialiseOrderSummary();
        updatePrices();
        initialiseCurrency();
    }

    private void initialiseCurrency() {
        totalPriceCurrencyView.setText(currency());
        discountedPriceCurrencyView.setText(currency());
    }

    private void initialiseOrderSummary() {
        salesChannelView.setText(cart.salesChannel().name());
        customerAccountView.setText(cart.customerAccount().getContactName());
    }

    private void updatePrices() {
        totalPriceView.setText(String.valueOf(cart.getActualTotal().getAmount()));
        if (!cart.getPromotions().isEmpty())
            discountedPriceView.setText(String.valueOf(cart.getDiscountedTotal().getAmount()));
    }

    private void initialiseProductGrid() {
        ProductCategories productCategories = new ProductCategories(productCategoryRepository.findAll());
        Collections.sort(cart.getProducts());
        productAdapter = new ProductGridAdapter(
                this,
                cart.getProducts(),
                productCategories,
                R.layout.layout_product_grid_header,
                R.layout.layout_removable_grid_item);
        productGridView.setAdapter(productAdapter);
    }

    private void initialisePromotionGrid() {
        allPromotions = promotionRepository.list();
        cart.overwrite(allPromotions.findApplicablePromotionsForProducts(cart.getProducts()));
        promotionAdapter = new PromotionGridAdapter(this, cart.getPromotions());
        promotionGridView.setAdapter(promotionAdapter);
        hideDiscountedPriceIfNoPromotion();
    }

    private void hideDiscountedPriceIfNoPromotion() {
        if (cart.getPromotions().isEmpty()) {
            discountedPriceLabel.setVisibility(View.GONE);
            discountedPrice.setVisibility(View.GONE);
            discountedPriceCurrency.setVisibility(View.GONE);
            choosePromotionLabel.setText("No Promotions Are Available");
        }
    }

    @Override
    protected Class<? extends Activity> nextActivity() {
        return PaymentActivity.class;
    }

    public void onProductRemoveButtonClick(Product product) {
        product.setQuantity(null);
        cart.removeProduct(product);
        productAdapter.notifyDataSetChanged();
        cart.overwrite(allPromotions.findApplicablePromotionsForProducts(cart.getProducts()));
        promotionGridView.setAdapter(promotionAdapter);
        updatePrices();
        hideDiscountedPriceIfNoPromotion();
    }

    public void onPromotionRemoveButtonClick(Promotion promotion) {
        cart.removePromotion(promotion);
        promotionAdapter.notifyDataSetChanged();
        updatePrices();
    }

    @Override
    public void onContinue(View view) {
        continueWhenCartIsNotEmpty();
    }

}
