package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import static java.util.Arrays.asList;

public class SelectSalesChannelAndCustomerActivity extends RoboActivity {

    @InjectView(R.id.sales_channel_list)
    private ListView salesChannelListView;

    @InjectView(R.id.customer_list)
    private ListView customerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sales_channel_and_customer);

        salesChannelListView
                .setAdapter(new ArrayAdapter<String>(this.getApplicationContext(),
                        R.layout.layout_selectable_list_item, asList("Sales Channel 1", "Sales Channel 2")));
        customerListView
                .setAdapter(new ArrayAdapter<String>(this.getApplicationContext(),
                        R.layout.layout_selectable_list_item, asList("Customer 1", "Customer 2")));

    }

}
