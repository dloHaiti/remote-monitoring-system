package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import com.dlohaiti.dlokiosk.adapter.CustomerAccountArrayAdapter;
import com.dlohaiti.dlokiosk.adapter.SalesChannelArrayAdapter;
import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.SalesChannelRepository;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.dlohaiti.dlokiosk.domain.CustomerAccounts;
import com.dlohaiti.dlokiosk.domain.SalesChannel;
import com.dlohaiti.dlokiosk.domain.SalesChannels;
import com.dlohaiti.dlokiosk.view_holder.LeftPaneListViewHolder;
import com.google.inject.Inject;
import roboguice.inject.InjectView;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SelectSalesChannelAndCustomerActivity extends SaleActivity {

    @InjectView(R.id.sales_channel_list)
    private ListView salesChannelListView;

    @InjectView(R.id.customer_list)
    private ListView customerListView;

    private CustomerAccountArrayAdapter customerListAdapter;
    private SalesChannelArrayAdapter salesChannelAdapter;

    @Inject
    private SalesChannelRepository salesChannelRepository;

    @Inject
    private CustomerAccountRepository customerAccountRepository;

    private SalesChannels salesChannels;
    private CustomerAccounts allCustomerList;
    private CustomerAccounts filteredCustomerList = new CustomerAccounts();
    private boolean showingAllCustomers = false;
    private MenuItem salesChannelCustomerToggle;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sales_channel_and_customer);

        loadSalesChannels();
    }

    private void loadSalesChannels() {
        salesChannels = new SalesChannels(salesChannelRepository.findAll());
        if (salesChannels.isEmpty()) {
            showNoConfigurationAlert();
            return;
        }
        selectSalesChannel();
        loadCustomerAccounts();
        initialiseSalesChannelList();
        initialiseCustomerList();
        continueButton.setEnabled(cart.customerAccount() != null);
    }

    private void selectSalesChannel() {
        SalesChannel salesChannelToSelect = cart.salesChannel() == null
                ? salesChannels.get(0)
                : salesChannels.findSalesChannelById(cart.salesChannel().id());
        salesChannelToSelect.select();
        cart.setSalesChannel(salesChannelToSelect);
    }

    private void loadCustomerAccounts() {
        allCustomerList = new CustomerAccounts(customerAccountRepository.findAll());
        updateFilteredCustomerList();
        if (cart.customerAccount() == null) {
            cart.setCustomerAccount(null);
        } else {
            CustomerAccount customerAccountToSelect = filteredCustomerList.findById(cart.customerAccount().id());
            customerAccountToSelect.select();
            cart.setCustomerAccount(customerAccountToSelect);
        }
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
                if (cart.salesChannel() != null) {
                    cart.salesChannel().unSelect();
                }
                tappedSalesChannel.select();
                cart.setSalesChannel(tappedSalesChannel);
                salesChannelAdapter.notifyDataSetChanged();
                continueButton.setEnabled(false);
                updateCustomerList();
                updateSalesChannelCustomerToggleButtonStatus();
            }
        });
    }

    private void clearCustomerSearch() {
        if (isNotBlank(searchView.getQuery())) {
            searchView.setQuery("", true);
        }
        searchView.setIconified(true);
    }

    private void updateSalesChannelCustomerToggleButtonStatus() {
        showingAllCustomers = false;
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
                    cart.setCustomerAccount(null);
                    continueButton.setEnabled(false);
                } else {
                    tappedCustomerAccount.select();
                    if (cart.customerAccount() != null) {
                        cart.customerAccount().unSelect();
                    }
                    cart.setCustomerAccount(tappedCustomerAccount);
                    continueButton.setEnabled(true);
                }
                customerListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateFilteredCustomerList() {
        filteredCustomerList.clear();
        allCustomerList.unSelectAll();
        filteredCustomerList.addAll(
                allCustomerList.findAccountsThatCanBeServedByChannel(cart.salesChannel().id()));
    }

    private void updateCustomerList() {
        cart.setCustomerAccount(null);
        updateFilteredCustomerList();
        customerListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_sales_channel_and_customer_activity, menu);

        salesChannelCustomerToggle = menu.findItem(R.id.sales_channel_customer_toggle);
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
                List<CustomerAccount> filteredList = !showingAllCustomers
                        ? allCustomerList.filterAccountsBy(text, cart.salesChannel())
                        : allCustomerList.filterAccountsBy(text);
                filteredCustomerList.addAll(filteredList);
                customerListAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

    public void onShowAllCustomersButtonClick(MenuItem item) {
        clearCustomerSearch();

        if (!showingAllCustomers) {
            item.setTitle(cart.salesChannel().name());
            filteredCustomerList.clear();
            filteredCustomerList.addAll(allCustomerList);
        } else {
            item.setTitle(R.string.all_customers_text);
            updateCustomerList();
        }

        showingAllCustomers = !showingAllCustomers;
        customerListAdapter.notifyDataSetChanged();
    }

    @Override
    protected Class<? extends SaleActivity> nextActivity() {
        return AddProductsToSaleActivity.class;
    }
}
