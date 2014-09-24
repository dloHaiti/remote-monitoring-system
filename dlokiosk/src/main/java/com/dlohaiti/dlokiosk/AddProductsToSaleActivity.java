package com.dlohaiti.dlokiosk;

import android.app.Activity;
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
import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.ProductCategories;
import com.dlohaiti.dlokiosk.domain.ProductCategory;
import com.dlohaiti.dlokiosk.domain.Products;
import com.dlohaiti.dlokiosk.view_holder.LeftPaneListViewHolder;
import com.google.inject.Inject;
import roboguice.inject.InjectView;

import java.text.MessageFormat;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
    private MenuItem cartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products_to_sale);
        loadProductCategories();
    }

    @Override
    protected void onResume() {
        super.onResume();

        productListAdapter.notifyDataSetChanged();
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
        }
    }

    private void initialiseProductCategoryList() {
        productCategoryAdapter = new ProductCategoryArrayAdapter(getApplicationContext(), productCategories);
        productCategoryListView.setAdapter(productCategoryAdapter);
        productCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                clearProductSearch();
                ProductCategory tappedProductCategory = productCategories
                        .findProductCategoryById(((LeftPaneListViewHolder) view.getTag()).id);
                if (selectedProductCategory != null) {
                    selectedProductCategory.unSelect();
                }
                tappedProductCategory.select();
                selectedProductCategory = tappedProductCategory;
                productCategoryAdapter.notifyDataSetChanged();
                updateProductList();
            }
        });
    }

    private void clearProductSearch() {
        if (isNotBlank(searchView.getQuery())) {
            searchView.setQuery("", true);
        }
        searchView.setIconified(true);
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
        productListAdapter = new ProductArrayAdapter(this, filteredProductList, cart);
        productListView.setAdapter(productListAdapter);
    }

    @Override
    protected Class<? extends Activity> nextActivity() {
        return ShoppingCartActivity.class;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_product_to_cart_activity, menu);

        searchView = ((SearchView) menu.findItem(R.id.search_product).getActionView());
        cartView = menu.findItem(R.id.cart);
        setCartViewTitle();

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
        onContinue(null);
    }

    public void onAddProduct(Product product, Integer quantity, Long id) {
        Product newProduct = product.withQuantity(quantity);
        filteredProductList.updateProductById(id, newProduct);
        allProducts.updateProductById(newProduct.getId(), newProduct);
        cart.addOrUpdateProduct(newProduct);
        setCartViewTitle();
        productListAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), getString(R.string.product_added_message), Toast.LENGTH_SHORT).show();
    }

    private void setCartViewTitle() {
        cartView.setTitle(MessageFormat.format(getString(R.string.cart_menu_item_title), cart.getProducts().size()));
    }

    @Override
    public void onContinue(View view) {
        continueWhenCartIsNotEmpty();
    }
}
