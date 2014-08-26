package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.SalesChannelRepository;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.dlohaiti.dlokiosk.widgets.SelectableArrayAdapter;
import com.dlohaiti.dlokiosk.widgets.SelectableListItem;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

public class SelectCustomerActivity extends RoboActivity implements AdapterView.OnItemClickListener {

    private List<CustomerAccount> allAccounts;
    private List<CustomerAccount> listItems = new ArrayList<CustomerAccount>();
    private List<CustomerAccount> salesChannelAccounts;

    @Inject
    private CustomerAccountRepository customerAccountRepository;
    @Inject
    private SalesChannelRepository salesChannelRepository;

    @InjectView(R.id.customer_list)
    private ListView customerAccountsView;

    @InjectView(R.id.continue_button)
    private Button continueButton;

    @InjectView(R.id.all_customers_button)
    private Button allCustomersButton;

    @InjectView(R.id.customers_for_selected_sales_channel_button)
    private Button customersForSelectedSalesChannelButton;

    private SelectableArrayAdapter adapter;
    private SelectableListItem selectedItem;
    private String selectedSalesChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.
                activity_select_customer_channel);
        loadIntentParams();
        loadCustomerAccounts();
        initialiseCustomerList();
        initialiseButtons();
    }

    private void loadIntentParams() {
        selectedSalesChannel = (String) getIntent().getExtras().get(SelectSalesChannelActivity.SALES_CHANNEL_PARAM);
    }

    private void loadCustomerAccounts() {
        allAccounts = new ArrayList<CustomerAccount>(customerAccountRepository.findAll());
        salesChannelAccounts = new ArrayList<CustomerAccount>();
        for (CustomerAccount account : allAccounts) {
            if (account.canBeServedByChannel(Long.parseLong(selectedSalesChannel))) {
                salesChannelAccounts.add(account);
            }
        }
    }

    private void initialiseCustomerList() {
        adapter = new SelectableArrayAdapter(getApplicationContext(), listItems);
        customerAccountsView.setAdapter(adapter);
        customerAccountsView.setOnItemClickListener(this);
    }

    private void initialiseButtons() {
        continueButton.setEnabled(false);
        onAllCustomersButtonClick();
        customersForSelectedSalesChannelButton.setText(selectedSalesChannel);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View listItemView, int position, long id) {
        SelectableListItem clickedSalesChannel = allAccounts.get(position);
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
        listItems.clear();
        listItems.addAll(allAccounts);
        adapter.notifyDataSetChanged();
    }

    public void onCustomersForSelectedSalesChannelButtonClick(View view) {
        allCustomersButton.setSelected(false);
        customersForSelectedSalesChannelButton.setSelected(true);
        listItems.clear();
        listItems.addAll(salesChannelAccounts);
        adapter.notifyDataSetChanged();
    }
}
