package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.SalesChannelRepository;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.dlohaiti.dlokiosk.domain.SalesChannel;
import com.dlohaiti.dlokiosk.widgets.SelectableArrayAdapter;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;

public class SelectSalesChannelAndCustomerActivity extends RoboActivity {

    @InjectView(R.id.sales_channel_list)
    private ListView salesChannelListView;

    @InjectView(R.id.customer_list)
    private ListView customerListView;

    @InjectView(R.id.continue_button)
    private Button continueButton;

    private ArrayAdapter<String> customerListAdapter;
    private SelectableArrayAdapter salesChannelAdapter;

    @Inject
    private SalesChannelRepository salesChannelRepository;

    @Inject
    private CustomerAccountRepository customerAccountRepository;

    private ArrayList<SalesChannel> salesChannels;
    private ArrayList<CustomerAccount> allCustomerAccounts;
    private ArrayList<CustomerAccount> filteredCustomerAccounts;
    private SalesChannel selectedSalesChannel;
    private CustomerAccount selectedCustomerAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sales_channel_and_customer);

        loadSalesChannels();
        loadCustomerAccounts();

        initialiseSalesChannelList();
        initialiseCustomerList();
    }

    private void loadSalesChannels() {
        salesChannels = new ArrayList<SalesChannel>(salesChannelRepository.findAll());
    }

    private void loadCustomerAccounts() {
        allCustomerAccounts = new ArrayList<CustomerAccount>(customerAccountRepository.findAll());
        filteredCustomerAccounts = allCustomerAccounts;
    }

    private void initialiseSalesChannelList() {
        salesChannelAdapter = new SelectableArrayAdapter(getApplicationContext(), salesChannels);
        salesChannelListView.setAdapter(salesChannelAdapter);
        salesChannelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SalesChannel tappedSalesChannel = salesChannels.get(position);
                if (tappedSalesChannel.isSelected()) {
                    tappedSalesChannel.unSelect();
                    selectedSalesChannel = null;
                    continueButton.setEnabled(false);
                } else {
                    tappedSalesChannel.select();
                    if (selectedSalesChannel != null) {
                        selectedSalesChannel.unSelect();
                    }
                    selectedSalesChannel = tappedSalesChannel;
                    continueButton.setEnabled(true);
                }
                salesChannelAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initialiseCustomerList() {
        customerListAdapter = new SelectableArrayAdapter(getApplicationContext(), filteredCustomerAccounts);
        customerListView.setAdapter(customerListAdapter);
        customerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CustomerAccount tappedCustomerAccount = filteredCustomerAccounts.get(position);
                if (tappedCustomerAccount.isSelected()) {
                    tappedCustomerAccount.unSelect();
                    selectedCustomerAccount = null;
                    continueButton.setEnabled(false);
                } else {
                    tappedCustomerAccount.select();
                    if (selectedCustomerAccount != null) {
                        selectedCustomerAccount.unSelect();
                    }
                    selectedCustomerAccount = tappedCustomerAccount;
                    continueButton.setEnabled(true);
                }
                customerListAdapter.notifyDataSetChanged();
            }
        });
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
