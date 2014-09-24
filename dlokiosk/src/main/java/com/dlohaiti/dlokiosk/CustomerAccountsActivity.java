package com.dlohaiti.dlokiosk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.dlohaiti.dlokiosk.adapter.CustomerAccountEditAdapter;
import com.dlohaiti.dlokiosk.adapter.SalesChannelArrayAdapter;
import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.SalesChannelRepository;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.dlohaiti.dlokiosk.domain.CustomerAccounts;
import com.dlohaiti.dlokiosk.domain.SalesChannel;
import com.dlohaiti.dlokiosk.domain.SalesChannels;
import com.dlohaiti.dlokiosk.view_holder.LeftPaneListViewHolder;
import com.google.inject.Inject;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import static org.apache.commons.lang3.StringUtils.isNotBlank;


public class CustomerAccountsActivity extends RoboActivity {
    @InjectView(R.id.sales_channel_list)
    private ListView salesChannelListView;

    @InjectView(R.id.customer_list)
    private ListView customerListView;

    private CustomerAccountEditAdapter customerListAdapter;
    private SalesChannelArrayAdapter salesChannelAdapter;

    @InjectView(R.id.continue_button)
    protected ImageButton continueButton;

    @Inject
    private SalesChannelRepository salesChannelRepository;
    @Inject
    private CustomerAccountRepository customerAccountRepository;

    private SalesChannels salesChannels;
    private SalesChannel selectedSalesChannel;
    private CustomerAccounts allCustomerList;
    private CustomerAccounts filteredCustomerList = new CustomerAccounts();
    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_accounts);

        loadSalesChannels();
    }

    private void loadCustomerAccounts() {
        allCustomerList = new CustomerAccounts(customerAccountRepository.findAll());
        updateFilteredCustomerList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomerAccounts();
        initialiseSalesChannelList();
        initialiseCustomerList();
        continueButton.setEnabled(false);
    }

    private void updateFilteredCustomerList() {
        filteredCustomerList.clear();
        allCustomerList.unSelectAll();
        filteredCustomerList.addAll(
                allCustomerList.findAccountsThatCanBeServedByChannel(selectedSalesChannel.getId()));
    }

    private void loadSalesChannels() {
        salesChannels = new SalesChannels(salesChannelRepository.findAll());
        if (salesChannels.isEmpty()) {
            showNoConfigurationAlert();
        } else {
            selectedSalesChannel=salesChannels.get(0);
            selectedSalesChannel.select();
            loadCustomerAccounts();
            initialiseSalesChannelList();
            initialiseCustomerList();
            continueButton.setEnabled(false);
        }
    }

    private void initialiseCustomerList() {
        customerListAdapter = new CustomerAccountEditAdapter(this, filteredCustomerList);
        customerListView.setAdapter(customerListAdapter);
        customerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CustomerAccount tappedCustomerAccount = filteredCustomerList.get(position);
                if (tappedCustomerAccount.isSelected()) {
                    tappedCustomerAccount.unSelect();
                    continueButton.setEnabled(false);
                } else {
                    tappedCustomerAccount.select();
                    continueButton.setEnabled(true);
                }
                customerListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initialiseSalesChannelList() {
        salesChannelAdapter = new SalesChannelArrayAdapter(getApplicationContext(), salesChannels);
        salesChannelListView.setAdapter(salesChannelAdapter);
        salesChannelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                clearCustomerSearch();
                SalesChannel tappedSalesChannel = salesChannels
                        .findSalesChannelById(((LeftPaneListViewHolder) view.getTag()).id);
                if (selectedSalesChannel != null) {
                    selectedSalesChannel.unSelect();
                }
                tappedSalesChannel.select();
                selectedSalesChannel = tappedSalesChannel;
                salesChannelAdapter.notifyDataSetChanged();
                continueButton.setEnabled(false);
                updateCustomerList();
            }
        });
    }


    private void updateCustomerList() {
        updateFilteredCustomerList();
        customerListAdapter = new CustomerAccountEditAdapter(this, filteredCustomerList);
        customerListView.setAdapter(customerListAdapter);
    }

    private void clearCustomerSearch() {
        if (isNotBlank(searchView.getQuery())) {
            searchView.setQuery("", true);
        }
        searchView.setIconified(true);
    }

    public void onCancel(View view) {
        Intent intent = new Intent(CustomerAccountsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    protected void showNoConfigurationAlert() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.no_configuration_error_message)
                .setTitle(R.string.no_configuration_error_title)
                .setCancelable(false)
                .setNeutralButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                finish();
                            }
                        })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_accounts_activity, menu);

        searchView = ((SearchView) menu.findItem(R.id.search_customer).getActionView());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                filteredCustomerList.clear();
                List<CustomerAccount> filteredList = allCustomerList.filterAccountsByContactName(text, selectedSalesChannel);
                filteredCustomerList.addAll(filteredList);
                customerListAdapter = new CustomerAccountEditAdapter(CustomerAccountsActivity.this, filteredCustomerList);
                customerListView.setAdapter(customerListAdapter);
                return true;
            }
        });
        return true;
    }

    public void onAddCustomerAccount(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), CustomerFormActivity.class);
        startActivity(intent);
    }

    public void onBack(View view) {
        finish();
    }

    public void addBalanceAmount(CustomerAccount account, Double amount) {
        account.setDueAmount(account.getDueAmount()-amount);
        customerAccountRepository.save(account);
        Log.d("BALANCE", String.valueOf(account));
        customerListAdapter = new CustomerAccountEditAdapter(CustomerAccountsActivity.this, filteredCustomerList);
        customerListView.setAdapter(customerListAdapter);
        customerListAdapter.notifyDataSetChanged();
    }
}

