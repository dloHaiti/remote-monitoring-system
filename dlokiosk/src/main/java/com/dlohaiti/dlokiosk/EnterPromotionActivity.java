package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import com.dlohaiti.dlokiosk.db.PromotionRepository;
import com.dlohaiti.dlokiosk.domain.Promotion;
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
    private List<Promotion> promotionCart = new ArrayList<Promotion>();
    private ImageAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_sale);
        inventoryGrid.setAdapter(new ImageAdapter(this, promotionRepository.list()));
        adapter = new ImageAdapter(this, promotionCart);
        shoppingCartGrid.setAdapter(adapter);
        inventoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                promotionCart.add(promotionRepository.findById(id));
                adapter.notifyDataSetChanged();
            }
        });
        shoppingCartGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                promotionCart.remove(promotionRepository.findById(id));
                adapter.notifyDataSetChanged();
            }
        });
        actionButton.setText(R.string.checkout);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish();
            }
        });
    }
}
