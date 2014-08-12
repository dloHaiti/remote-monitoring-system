package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.dlohaiti.dlokiosk.widgets.SelectableArrayAdapter;
import com.dlohaiti.dlokiosk.widgets.SelectableListItem;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class SelectCustomerActivity extends RoboActivity implements AdapterView.OnItemClickListener {

    private final SelectableListItem[] salesChannels = {
            new SelectableListItem(1L, "Customer 1"),
            new SelectableListItem(1L, "Customer 2"),
            new SelectableListItem(1L, "Customer 3"),
            new SelectableListItem(1L, "Customer 4"),
            new SelectableListItem(1L, "Customer 5"),
            new SelectableListItem(1L, "Customer 6"),
            new SelectableListItem(1L, "Customer 7"),
            new SelectableListItem(1L, "Customer 8"),
            new SelectableListItem(1L, "Customer 9"),
            new SelectableListItem(1L, "Customer 10")
    };

    @InjectView(R.id.customer_list)
    private ListView salesChannelsView;

    @InjectView(R.id.continue_button)
    private Button continueButton;

    @InjectView(R.id.all_customers_button)
    private Button allCustomersButton;

    @InjectView(R.id.customers_for_selected_sales_channel_button)
    private Button customersForSelectedSalesChannelButton;

    private SelectableArrayAdapter adapter;
    private SelectableListItem selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_customer_channel);
        adapter = new SelectableArrayAdapter(getApplicationContext(), salesChannels);
        salesChannelsView.setAdapter(adapter);
        salesChannelsView.setOnItemClickListener(this);
        continueButton.setEnabled(false);
        onAllCustomersButtonClick();
        customersForSelectedSalesChannelButton.
                setText((String) getIntent().getExtras().get(SelectSalesChannelActivity.SALES_CHANNEL_PARAM));
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
        Toast.makeText(getApplicationContext(), "Selected customer: " + selectedItem.name(), Toast.LENGTH_SHORT)
                .show();
    }

    public void onBack(View view) {
        finish();
    }

    public void onAllCustomersButtonClick(View view) {
        onAllCustomersButtonClick();
    }

    private void onAllCustomersButtonClick() {
        allCustomersButton.setSelected(true);
        customersForSelectedSalesChannelButton.setSelected(false);

    }


    public void onCustomersForSelectedSalesChannelButtonClick(View view) {
        allCustomersButton.setSelected(false);
        customersForSelectedSalesChannelButton.setSelected(true);
    }
}
