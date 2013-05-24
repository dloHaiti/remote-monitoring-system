package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import com.dlohaiti.dlokiosk.db.PromotionRepository;
import com.dlohaiti.dlokiosk.domain.Promotion;
import com.dlohaiti.dlokiosk.domain.ShoppingCart;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

public class EnterPromotionActivity extends RoboActivity {

    @InjectView(R.id.action_button) private Button actionButton;
    @InjectView(R.id.inventory_grid) private GridView inventoryGrid;
    @InjectView(R.id.right_grid) private GridView shoppingCartGrid;
    @Inject private PromotionRepository promotionRepository;
    @Inject private ShoppingCart shoppingCart;
    private ImageAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_sale);

        List<Promotion> promotions = new ArrayList<Promotion>();
        for(Promotion p : promotionRepository.list()) {
            if(p.appliesTo(shoppingCart.getProducts())) {
                promotions.add(p);
            }
        }
        inventoryGrid.setAdapter(new ImageAdapter(this, promotions));
        adapter = new ImageAdapter(this, shoppingCart.getPromotions());
        shoppingCartGrid.setAdapter(adapter);
        inventoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shoppingCart.addPromotion(promotionRepository.findById(id));
                adapter.notifyDataSetChanged();
            }
        });
        shoppingCartGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shoppingCart.removePromotion(promotionRepository.findById(id));
                adapter.notifyDataSetChanged();
            }
        });
        actionButton.setText(R.string.checkout);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                shoppingCart.checkout();
                finish();
            }
        });
    }
}
