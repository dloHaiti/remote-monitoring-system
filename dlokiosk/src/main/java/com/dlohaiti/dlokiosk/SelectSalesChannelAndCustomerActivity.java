package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.SalesChannelRepository;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.dlohaiti.dlokiosk.domain.SalesChannel;
import com.dlohaiti.dlokiosk.widgets.CustomerAccountArrayAdapter;
import com.dlohaiti.dlokiosk.widgets.SalesChannelArrayAdapter;
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

    private CustomerAccountArrayAdapter customerListAdapter;
    private SalesChannelArrayAdapter salesChannelAdapter;

    @Inject
    private SalesChannelRepository salesChannelRepository;

    @Inject
    private CustomerAccountRepository customerAccountRepository;

    private ArrayList<SalesChannel> salesChannels;
    private ArrayList<CustomerAccount> allCustomerList;
    private ArrayList<CustomerAccount> filteredCustomerList = new ArrayList<CustomerAccount>();
    private SalesChannel selectedSalesChannel;
    private CustomerAccount selectedCustomerAccount;
    private boolean showAllCustomers = true;
    private MenuItem salesChannelCustomerToggle;

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
        salesChannels.get(0).select();
        selectedSalesChannel = salesChannels.get(0);
    }

    private void loadCustomerAccounts() {
        allCustomerList = new ArrayList<CustomerAccount>(customerAccountRepository.findAll());
        updateFilteredCustomerList();
    }

    private void initialiseSalesChannelList() {
        salesChannelAdapter = new SalesChannelArrayAdapter(getApplicationContext(), salesChannels);
        salesChannelListView.setAdapter(salesChannelAdapter);
        salesChannelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SalesChannel tappedSalesChannel = salesChannels.get(position);
                if (selectedSalesChannel != null) {
                    selectedSalesChannel.unSelect();
                }
                tappedSalesChannel.select();
                selectedSalesChannel = tappedSalesChannel;
                salesChannelAdapter.notifyDataSetChanged();
                continueButton.setEnabled(false);
                updateCustomerList();
                updateSalesChannelCustomerToggleButtonStatus();
            }
        });
    }

    private void updateSalesChannelCustomerToggleButtonStatus() {
        showAllCustomers = true;
        salesChannelCustomerToggle.setTitle(R.string.all_customers_text);
    }

    private void initialiseCustomerList() {
        customerListAdapter = new CustomerAccountArrayAdapter(getApplicationContext(), filteredCustomerList);
        customerListView.setAdapter(customerListAdapter);
        customerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CustomerAccount tappedCustomerAccount = filteredCustomerList.get(position);
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

    private void updateFilteredCustomerList() {
        filteredCustomerList.clear();
        for (CustomerAccount account : allCustomerList) {
            account.unSelect();
            if (account.canBeServedByChannel(selectedSalesChannel.name())) {
                filteredCustomerList.add(account);
            }
        }
        selectedCustomerAccount = null;
    }

    private void updateCustomerList() {
        selectedCustomerAccount = null;
        updateFilteredCustomerList();
        customerListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_sales_channel_and_customer_activity, menu);

        salesChannelCustomerToggle = menu.findItem(R.id.sales_channel_customer_toggle);

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

    public void onShowAllCustomersButtonClick(MenuItem item) {
        if (showAllCustomers) {
            item.setTitle(selectedSalesChannel.name());
            filteredCustomerList.clear();
            filteredCustomerList.addAll(allCustomerList);
        } else {
            item.setTitle(R.string.all_customers_text);
            updateCustomerList();
        }

        showAllCustomers = !showAllCustomers;
        customerListAdapter.notifyDataSetChanged();
    }
}
