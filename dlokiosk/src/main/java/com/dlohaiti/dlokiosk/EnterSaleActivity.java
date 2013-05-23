package com.dlohaiti.dlokiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.dlohaiti.dlokiosk.db.ProductRepository;
import com.dlohaiti.dlokiosk.db.ReceiptsRepository;
import com.dlohaiti.dlokiosk.domain.Product;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

public class EnterSaleActivity extends RoboActivity {

    @InjectView(R.id.inventory_grid) private GridView inventoryGrid;
    @InjectView(R.id.right_grid) private GridView shoppingCartGrid;
    @Inject private ProductRepository repository;
    @Inject private ReceiptsRepository receiptsRepository;
    private List<Product> shoppingCart = new ArrayList<Product>();
    private ImageAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_sale);
        inventoryGrid.setAdapter(new ImageAdapter(this, repository.list()));
        adapter = new ImageAdapter(this, shoppingCart);
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
                shoppingCart.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void checkout(View v) {
        receiptsRepository.add(shoppingCart);
        finish();
    }

    public void addToShoppingCart(Product product) {
        shoppingCart.add(product);
        adapter.notifyDataSetChanged();
    }

    public void moveToPromotions(View v) {
        startActivityForResult(new Intent(this, EnterPromotionActivity.class), 777);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 777) {
            finish();
        }
    }
}
