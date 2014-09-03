package com.dlohaiti.dlokiosk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.dlohaiti.dlokiosk.adapter.ProductCategoryArrayAdapter;
import com.dlohaiti.dlokiosk.db.ProductCategoryRepository;
import com.dlohaiti.dlokiosk.domain.ProductCategories;
import com.dlohaiti.dlokiosk.domain.ProductCategory;
import com.dlohaiti.dlokiosk.view_holder.LeftPaneListViewHolder;
import com.google.inject.Inject;
import roboguice.inject.InjectView;

public class AddProductsToSaleActivity extends SaleActivity {

    @InjectView(R.id.product_category_list)
    private ListView productCategoryListView;

    @Inject
    private ProductCategoryRepository productCategoryRepository;

    private ProductCategories productCategories;
    private ProductCategory selectedProductCategory;
    private ProductCategoryArrayAdapter productCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products_to_sale);
        loadProductCategories();
    }

    private void loadProductCategories() {
        productCategories = new ProductCategories(productCategoryRepository.findAll());
        if (productCategories.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.no_configuration_error_message)
                    .setTitle(R.string.no_configuration_error_title)
                    .setCancelable(false)
                    .setNeutralButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    finish();
                                }
                            })
                    .show();
        } else {
            productCategories.get(0).select();
            selectedProductCategory = productCategories.get(0);
            initialiseProductCategoryList();
            continueButton.setEnabled(false);
        }
    }

    private void initialiseProductCategoryList() {
        productCategoryAdapter = new ProductCategoryArrayAdapter(getApplicationContext(), productCategories);
        productCategoryListView.setAdapter(productCategoryAdapter);
        productCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                clearCustomerSearch();
                ProductCategory tappedProductCategory = productCategories
                        .findProductCategoryById(((LeftPaneListViewHolder) view.getTag()).id);
                if (selectedProductCategory != null) {
                    selectedProductCategory.unSelect();
                }
                tappedProductCategory.select();
                selectedProductCategory = tappedProductCategory;
                productCategoryAdapter.notifyDataSetChanged();
                continueButton.setEnabled(false);
//                updateCustomerList();
//                updateSalesChannelCustomerToggleButtonStatus();
            }
        });
    }

    @Override
    protected Class<? extends SaleActivity> nextActivity() {
        return null;
    }
}
