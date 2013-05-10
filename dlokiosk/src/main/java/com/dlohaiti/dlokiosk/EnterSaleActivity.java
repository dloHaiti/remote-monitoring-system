package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.dlohaiti.dlokiosk.domain.Product;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

public class EnterSaleActivity extends RoboActivity {

    @InjectView(R.id.inventory_grid) private GridView inventoryGrid;
    @InjectView(R.id.shopping_cart_grid) private GridView shoppingCartGrid;
    @Inject private ProductRepository repository;
    @Inject private SalesRepository salesRepository;
    private List<Product> shoppingCart = new ArrayList<Product>();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_sale);
        inventoryGrid.setAdapter(new ImageAdapter(this, repository.list()));
        final ImageAdapter adapter = new ImageAdapter(this, shoppingCart);
        shoppingCartGrid.setAdapter(adapter);

        inventoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                shoppingCart.add(repository.findById(id));
                adapter.notifyDataSetChanged();
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
        salesRepository.add(shoppingCart);
        finish();
    }
}
