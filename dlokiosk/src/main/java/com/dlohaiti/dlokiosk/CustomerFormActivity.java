package com.dlohaiti.dlokiosk;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.CustomerTypeRepository;
import com.dlohaiti.dlokiosk.db.SalesChannelRepository;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.dlohaiti.dlokiosk.domain.CustomerTypes;
import com.dlohaiti.dlokiosk.domain.SalesChannels;
import com.google.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class CustomerFormActivity extends RoboActivity {
    @Inject
    private SalesChannelRepository salesChannelRepository;

    @Inject
    private CustomerAccountRepository customerAccountRepository;

    @Inject
    private CustomerTypeRepository customerTypeRepository;


    protected MultiSelectSpinner salesChannel;


    @InjectView(R.id.customer_type)
    protected Spinner customerType;

    @InjectView(R.id.customer_name)
    protected EditText customerName;

    @InjectView(R.id.customer_phone)
    protected EditText customerPhone;

    @InjectView(R.id.customer_address)
    protected EditText customerAddress;

    @InjectView(R.id.organisation)
    protected EditText organization;
    private CustomerAccount account;
    CustomerTypes customerTypes;

    @InjectResource(R.string.saved_message)
    private String savedMessage;
    @InjectResource(R.string.error_not_saved_message)
    private String errorNotSavedMessage;
    private SalesChannels salesChannels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_form);
        loadSalesChannels();
        loadCustomerTypes();
        fillCustomerDetails();
    }

    private void loadSalesChannels() {
        salesChannels = new SalesChannels(salesChannelRepository.findAll());
        salesChannel = (MultiSelectSpinner) findViewById(R.id.sales_channel);
        salesChannel.setItems(salesChannels.getSalesChannelNames());
    }

    private void loadCustomerTypes() {
        customerTypes = new CustomerTypes(customerTypeRepository.findAll());
        ArrayAdapter<String> customerTypeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item, customerTypes.getCustomerTypeNames());
        customerType.setAdapter(customerTypeAdapter);
    }

    private void fillCustomerDetails() {
        String accountId = getIntent().getStringExtra("account_id");
        if (StringUtils.isEmpty(accountId)) {
            account = null;
            return;
        }

        account = customerAccountRepository.findById(Long.valueOf(accountId));
        if (account == null) {
            return;
        }
        customerName.setText(account.getContactName());
        customerPhone.setText(account.getPhoneNumber());
        customerAddress.setText(account.getAddress());
        organization.setText(account.getName());
        SalesChannels channels = new SalesChannels(salesChannelRepository.findByCustomerId(account.getId()));
        salesChannel.setSelection(channels.getSalesChannelNames());
        String customerTypeName = customerTypes.getCustomerTypeNameById(account.getCustomerTypeId());
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) customerType.getAdapter();
        int position = adapter.getPosition(customerTypeName);
        customerType.setSelection(position);
    }


    public void onCancel(View view) {
        Intent intent = new Intent(CustomerFormActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onBack(View view) {
        finish();
    }

    public void onSave(View view) {
        boolean successful = (account == null) ? createNewAccount() : updateExistingAccount();
        if (successful) {
            Toast.makeText(this, savedMessage, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, errorNotSavedMessage, Toast.LENGTH_LONG).show();
        }
    }

    private boolean updateExistingAccount() {
        account.setName(organization.getText().toString());
        account.setContactName(customerName.getText().toString());
        account.setPhoneNumber(customerPhone.getText().toString());
        account.setCustomerTypeId(customerTypes.getCustomerTypeId(customerType.getSelectedItem().toString()));
        account.withChannels(salesChannels.getSalesChannelsFromName(salesChannel.getSelectedStrings()));
        return customerAccountRepository.save(account);
    }

    private boolean createNewAccount() {
        return false;
    }
}
