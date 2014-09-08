package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.dlohaiti.dlokiosk.db.ProductRepository;
import com.google.inject.Inject;
import roboguice.inject.InjectView;

import static java.util.Arrays.asList;

public class ShoppingCartActivity extends SaleActivity {

    @Inject
    private ProductRepository productRepository;

    @InjectView(R.id.product_list)
    private ListView productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, asList("Product 1", "Product 2", "Product 3", "Product 4", "Product 5", "Product 6", "Product 7", "Product 8", "Product 9"));
        productList.setAdapter(adapter);
    }

    @Override
    protected Class<? extends SaleActivity> nextActivity() {
        return null;
    }
}
