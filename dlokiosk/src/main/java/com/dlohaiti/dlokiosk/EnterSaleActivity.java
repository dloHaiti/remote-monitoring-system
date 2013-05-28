package com.dlohaiti.dlokiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.dlohaiti.dlokiosk.db.ProductRepository;
import com.dlohaiti.dlokiosk.domain.Product;
import com.dlohaiti.dlokiosk.domain.ShoppingCart;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class EnterSaleActivity extends RoboActivity {
    @InjectView(R.id.left_grid) private GridView inventoryGrid;
    @InjectView(R.id.right_grid) private GridView shoppingCartGrid;
    @Inject private ProductRepository repository;
    @Inject private ShoppingCart sc;
    private ImageAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_sale);
        inventoryGrid.setAdapter(new ImageAdapter<Product>(this, repository.list()));
        adapter = new ImageAdapter<Product>(this, sc.getProducts());
        shoppingCartGrid.setAdapter(adapter);

        inventoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Product product = repository.findById(id);
                if (product.requiresQuantity()) {
                    NumberPickerDialog numberPickerDialog = new NumberPickerDialog(product);
                    numberPickerDialog.show(getFragmentManager(), "numberPickerDialog");
                } else {
                    addToShoppingCart(product);
                }
            }
        });

        shoppingCartGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sc.removeProduct(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void addToShoppingCart(Product product) {
        sc.addProduct(product);
        adapter.notifyDataSetChanged();
    }

    public void moveToPromotions(View v) {
        startActivity(new Intent(this, EnterPromotionActivity.class));
        finish();
    }

    @Override public void onBackPressed() {
        sc.clear();
        finish();
    }
}
