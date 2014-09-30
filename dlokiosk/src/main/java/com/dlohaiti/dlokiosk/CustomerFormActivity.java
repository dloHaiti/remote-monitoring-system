package com.dlohaiti.dlokiosk;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.CustomerTypeRepository;
import com.dlohaiti.dlokiosk.db.SalesChannelRepository;
import com.dlohaiti.dlokiosk.db.SponsorRepository;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.dlohaiti.dlokiosk.domain.CustomerTypes;
import com.dlohaiti.dlokiosk.domain.SalesChannels;
import com.dlohaiti.dlokiosk.domain.Sponsors;
import com.google.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

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

    @Inject
    private ConfigurationRepository config;

    @Inject
    private SponsorRepository sponsorRepository;

    protected MultiSelectSpinner salesChannel;


    @InjectView(R.id.customer_type)
    protected Spinner customerType;

    @InjectView(R.id.customer_name)
    protected EditText customerName;

    @InjectView(R.id.customer_phone)
    protected EditText customerPhone;

    @InjectView(R.id.customer_address)
    protected EditText customerAddress;

    @InjectView(R.id.latitude_degres)
    protected EditText latitudeDegree;



    @InjectView(R.id.longitude_degres)
    protected EditText longitudeDegree;


    @InjectView(R.id.organisation)
    protected EditText organization;

    @InjectView(R.id.sales_channel_error)
    protected EditText salesChannelError;

    private CustomerAccount account;
    CustomerTypes customerTypes;

    @InjectResource(R.string.saved_message)
    private String savedMessage;
    @InjectResource(R.string.error_not_saved_message)
    private String errorNotSavedMessage;

    @InjectResource(R.string.mandatory_field)
    private String mandatoryFieldMessage;

    private SalesChannels salesChannels;
    private Sponsors sponsors;

    private MultiSelectSpinner sponsor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_form);
        loadSalesChannels();
        loadCustomerTypes();
        loadSponsors();
        fillCustomerDetails();
    }

    private void loadSponsors() {
        sponsors = sponsorRepository.findAll();
        sponsor = (MultiSelectSpinner) findViewById(R.id.sponsor);
        sponsor.setItems(sponsors.getSponsorNames());
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

        account = customerAccountRepository.findById(accountId);
        if (account == null) {
            return;
        }
        customerName.setText(account.getContactName());
        customerPhone.setText(account.getPhoneNumber());
        customerAddress.setText(account.getAddress());
        organization.setText(account.getName());
        fillGPSCoordinates(account.getGpsCoordinates());
        SalesChannels channels = new SalesChannels(salesChannelRepository.findByCustomerId(account.getId()));
        salesChannel.setSelection(channels.getSalesChannelNames());
        Sponsors mappedSponsors = sponsorRepository.findByCustomerId(account.getId());
        sponsor.setSelection(mappedSponsors.getSponsorNames());
        String customerTypeName = customerTypes.getCustomerTypeNameById(account.getCustomerTypeId());
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) customerType.getAdapter();
        int position = adapter.getPosition(customerTypeName);
        customerType.setSelection(position);
    }

    private void fillGPSCoordinates(String gpsCoordinates) {
        if (TextUtils.isEmpty(gpsCoordinates)) return;
        String[] coordinates = gpsCoordinates.trim().split(" ");
        String latitude = coordinates[0];
        String longitude = coordinates[1];
        latitudeDegree.setText(latitude);
        longitudeDegree.setText(longitude);
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
        if (!validateCustomerForm()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Save")
                    .setMessage("There are validation errors")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }

                    })
                    .show();
            return;
        }
        boolean successful = (account == null) ? createNewAccount() : updateAccount(account);
        if (successful) {
            Toast.makeText(this, savedMessage, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, errorNotSavedMessage, Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateCustomerForm() {
        boolean isValid = true;
        if (customerName.getText().toString().isEmpty()) {
            customerName.setError(mandatoryFieldMessage);
            isValid = false;
        } else {
            customerName.setError(null);
            List<CustomerAccount> existingAccounts = customerAccountRepository.findByContactName(customerName.getText().toString());
            CustomerAccount matchingAccount = getMatchingAccount(existingAccounts, organization.getText().toString());
            if (matchingAccount != null) {
                if(account==null || !account.getId().equalsIgnoreCase(matchingAccount.getId())){
                    organization.setError("Already Exist");
                    customerName.setError("Already Exist");
                    isValid = false;
                }
            } else {
                organization.setError(null);
                customerName.setError(null);
            }
        }

        if (salesChannel.getSelectedStrings().isEmpty()) {
            salesChannelError.setError("Atleast one sales channel is mandatory");
            isValid = false;
        } else {
            salesChannelError.setError(null);
        }
        return isValid;
    }

    private CustomerAccount getMatchingAccount(List<CustomerAccount> existingAccounts, String organizationName) {
        for(CustomerAccount ca:existingAccounts){
            if(ca.getName().equalsIgnoreCase(organizationName)){
                return ca;
            }
        }
        return null;
    }


    private boolean updateAccount(CustomerAccount a) {
        a.setName(organization.getText().toString());
        a.setContactName(customerName.getText().toString());
        a.setPhoneNumber(customerPhone.getText().toString());
        a.setAddress(customerAddress.getText().toString());
        a.setCustomerTypeId(customerTypes.getCustomerTypeId(customerType.getSelectedItem().toString()));
        a.withChannels(salesChannels.getSalesChannelsFromName(salesChannel.getSelectedStrings()));
        a.withSponsors(sponsors.getSponsorsFromName(sponsor.getSelectedStrings()));
        a.setGpsCoordinates(buildGpsCoordinate());
        return customerAccountRepository.save(a);
    }

    private String buildGpsCoordinate() {
        if (isLatitudeEmpty() || isLongitudeEmpty()) {
            return "";
        } else {
            return String.format("%s %s",
                    latitudeDegree.getText(),
                    longitudeDegree.getText()
            );
        }
    }

    private boolean isLatitudeEmpty() {
        return isEmptyField(latitudeDegree);
    }


    private boolean isLongitudeEmpty() {
        return isEmptyField(longitudeDegree);
    }

    private boolean isEmptyField(EditText editable) {
        return editable.getText().toString().isEmpty();
    }

    private boolean createNewAccount() {
        CustomerAccount newAccount = new CustomerAccount();
        return updateAccount(newAccount);
    }
}
