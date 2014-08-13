package com.dlohaiti.dlokiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.dlohaiti.dlokiosk.db.SalesChannelRepository;
import com.dlohaiti.dlokiosk.domain.SalesChannel;
import com.dlohaiti.dlokiosk.widgets.SelectableArrayAdapter;
import com.dlohaiti.dlokiosk.widgets.SelectableListItem;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

public class SelectSalesChannelActivity extends RoboActivity implements AdapterView.OnItemClickListener {

    public static final String SALES_CHANNEL_PARAM = "SALES_CHANNEL";
    private List<SalesChannel> listItems;

    @Inject
    private SalesChannelRepository salesChannelRepository;

    @InjectView(R.id.sales_channel_list)
    private ListView salesChannelsView;

    @InjectView(R.id.continue_button)
    private Button continueButton;

    private SelectableArrayAdapter adapter;
    private SelectableListItem selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_sales_channel);
        loadSalesChannels();
        initialiseSalesChannelList();
        initialiseButtons();
    }

    private void initialiseButtons() {
        continueButton.setEnabled(false);
    }

    private void initialiseSalesChannelList() {
        adapter = new SelectableArrayAdapter(getApplicationContext(), listItems);
        salesChannelsView.setAdapter(adapter);
        salesChannelsView.setOnItemClickListener(this);
    }

    private void loadSalesChannels() {
        listItems = new ArrayList<SalesChannel>(salesChannelRepository.findAll());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View listItemView, int position, long id) {
        SelectableListItem clickedSalesChannel = listItems.get(position);
        if (clickedSalesChannel.isSelected()) {
            clickedSalesChannel.unSelect();
            selectedItem = null;
            continueButton.setEnabled(false);
        } else {
            clickedSalesChannel.select();
            if (selectedItem != null) {
                selectedItem.unSelect();
            }
            selectedItem = clickedSalesChannel;
            continueButton.setEnabled(true);
        }
        adapter.notifyDataSetChanged();
    }

    public void onContinue(View view) {
        Intent intent = new Intent(this, SelectCustomerActivity.class);
        intent.putExtra(SALES_CHANNEL_PARAM, selectedItem.name());
        startActivity(intent);
    }
}
