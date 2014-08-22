package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import static java.util.Arrays.asList;

public class SelectSalesChannelAndCustomerActivity extends RoboActivity {

    @InjectView(R.id.sales_channel_list)
    private ListView salesChannelListView;

    @InjectView(R.id.customer_list)
    private ListView customerListView;

    private ArrayAdapter<String> customerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sales_channel_and_customer);

        salesChannelListView
                .setAdapter(new ArrayAdapter<String>(this.getApplicationContext(),
                        R.layout.layout_selectable_list_item,
                        asList("Sales Channel 1", "Sales Channel 2", "Sales Channel 3", "Sales Channel 4")));
        customerListAdapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.layout_selectable_list_item,
                asList("Customer 1", "Customer 2", "Customer 3", "Customer 4", "Customer 5",
                        "Customer 6", "Customer 7", "Customer 8", "Customer 9", "Customer 10",
                        "Customer 11", "Customer 12", "Customer 13", "Customer 14", "Customer 15"));
        customerListView.setAdapter(customerListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_sales_channel_and_customer_activity, menu);
        ((SearchView) menu
                .findItem(R.id.search_customer)
                .getActionView())
                .setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String text) {
                        menu.findItem(R.id.search_customer).getActionView().clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String text) {
                        customerListAdapter.getFilter().filter(text);
                        return true;
                    }
                });

        return true;
    }
}
