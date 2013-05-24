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
    private final static int ENTER_PROMOTION_ACTIVITY_CODE = 777;

    @InjectView(R.id.inventory_grid) private GridView inventoryGrid;
    @InjectView(R.id.right_grid) private GridView shoppingCartGrid;
    @Inject private ProductRepository repository;
    @Inject private ShoppingCart sc;
    private ImageAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_sale);
        inventoryGrid.setAdapter(new ImageAdapter(this, repository.list()));
        adapter = new ImageAdapter(this, sc.getProducts());
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
                sc.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void addToShoppingCart(Product product) {
        sc.addProduct(product);
        adapter.notifyDataSetChanged();
    }

    public void moveToPromotions(View v) {
        Intent intent = new Intent(this, EnterPromotionActivity.class);
        startActivityForResult(intent, ENTER_PROMOTION_ACTIVITY_CODE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ENTER_PROMOTION_ACTIVITY_CODE) {
            finish();
        }
    }
}
