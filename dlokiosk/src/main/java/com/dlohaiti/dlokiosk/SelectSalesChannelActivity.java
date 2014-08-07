package com.dlohaiti.dlokiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.dlohaiti.dlokiosk.widgets.SelectableArrayAdapter;
import com.dlohaiti.dlokiosk.widgets.SelectableListItem;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class SelectSalesChannelActivity extends RoboActivity implements AdapterView.OnItemClickListener {

    public static final String SALES_CHANNEL_PARAM = "SALES_CHANNEL";
    private final SelectableListItem[] salesChannels = {
            new SelectableListItem("Sales Channel 1"),
            new SelectableListItem("Sales Channel 2"),
            new SelectableListItem("Sales Channel 3"),
            new SelectableListItem("Sales Channel 4"),
            new SelectableListItem("Sales Channel 5"),
            new SelectableListItem("Sales Channel 6"),
            new SelectableListItem("Sales Channel 7"),
            new SelectableListItem("Sales Channel 8"),
            new SelectableListItem("Sales Channel 9"),
            new SelectableListItem("Sales Channel 10")
    };

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
        adapter = new SelectableArrayAdapter(getApplicationContext(), salesChannels);
        salesChannelsView.setAdapter(adapter);
        salesChannelsView.setOnItemClickListener(this);
        continueButton.setEnabled(false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View listItemView, int position, long id) {
        SelectableListItem clickedSalesChannel = salesChannels[position];
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
