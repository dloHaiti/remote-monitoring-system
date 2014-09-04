package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.dlohaiti.dlokiosk.adapter.ProductArrayAdapter;
import com.dlohaiti.dlokiosk.adapter.ProductCategoryArrayAdapter;
import com.dlohaiti.dlokiosk.db.ProductCategoryRepository;
import com.dlohaiti.dlokiosk.db.ProductRepository;
import com.dlohaiti.dlokiosk.domain.ProductCategories;
import com.dlohaiti.dlokiosk.domain.ProductCategory;
import com.dlohaiti.dlokiosk.domain.Products;
import com.dlohaiti.dlokiosk.view_holder.LeftPaneListViewHolder;
import com.google.inject.Inject;
import roboguice.inject.InjectView;

public class AddProductsToSaleActivity extends SaleActivity {

    @InjectView(R.id.product_category_list)
    private ListView productCategoryListView;

    @InjectView(R.id.product_list)
    private ListView productListView;

    @Inject
    private ProductCategoryRepository productCategoryRepository;

    @Inject
    private ProductRepository productRepository;

    private ProductCategories productCategories;
    private Products allProducts;
    private Products filteredProductList = new Products();
    private ProductCategory selectedProductCategory;
    private ProductCategoryArrayAdapter productCategoryAdapter;
    private ProductArrayAdapter productListAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products_to_sale);
        loadProductCategories();
    }

    private void loadProductCategories() {
        productCategories = new ProductCategories(productCategoryRepository.findAll());
        if (productCategories.isEmpty()) {
            showNoConfigurationAlert();
        } else {
            productCategories.get(0).select();
            selectedProductCategory = productCategories.get(0);
            loadProducts();
            initialiseProductCategoryList();
            initialiseProductsList();
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
                updateProductList();
            }
        });
    }

    private void loadProducts() {
        allProducts = new Products(productRepository.list());
        updateFilteredProductList();
    }

    private void updateFilteredProductList() {
        filteredProductList.clear();
        filteredProductList.addAll(
                allProducts.findAccountsByCategoryId(selectedProductCategory.id()));
    }

    private void updateProductList() {
        updateFilteredProductList();
        productListAdapter.notifyDataSetChanged();
    }

    private void initialiseProductsList() {
        productListAdapter = new ProductArrayAdapter(getApplicationContext(), filteredProductList);
        productListView.setAdapter(productListAdapter);
    }

    @Override
    protected Class<? extends SaleActivity> nextActivity() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_product_to_cart_activity, menu);

        searchView = ((SearchView) menu.findItem(R.id.search_product).getActionView());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                filteredProductList.clear();
                filteredProductList.addAll(allProducts.filterBy(text, selectedProductCategory.id()));
                productListAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

    public void onCartButtonClick(MenuItem item) {
        Toast.makeText(getApplicationContext(), "Cart", Toast.LENGTH_SHORT);
    }
}
