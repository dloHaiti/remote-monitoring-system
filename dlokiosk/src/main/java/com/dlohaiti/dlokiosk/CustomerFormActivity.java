package com.dlohaiti.dlokiosk;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.SalesChannelRepository;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.dlohaiti.dlokiosk.domain.SalesChannel;
import com.google.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class CustomerFormActivity extends RoboActivity {
    @Inject
    private SalesChannelRepository salesChannelRepository;

    @Inject
    private CustomerAccountRepository customerAccountRepository;

    @InjectView(R.id.sales_channel)
    protected Spinner salesChannel;

    @InjectView(R.id.customer_name)
    protected EditText customerName;

    @InjectView(R.id.customer_phone)
    protected EditText customerPhone;

    @InjectView(R.id.customer_address)
    protected EditText customerAddress;

    @InjectView(R.id.organisation)
    protected EditText organization;
    private CustomerAccount account;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_form);
        ArrayAdapter<String> salesChannelAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,getSalesChannelNames());
        salesChannel.setAdapter(salesChannelAdapter);
        fillCustomerDetails();
    }

    private void fillCustomerDetails() {
        String accountId = getIntent().getStringExtra("account_id");
        if(StringUtils.isEmpty(accountId)){
            return;
        }
        account = customerAccountRepository.findById(Long.valueOf(accountId));
        if(account==null) {
            return;
        }
        customerName.setText(account.contactName());
        customerPhone.setText(account.phoneNumber());
        customerAddress.setText(account.address());
        organization.setText(account.name());
//        account.channelIds();
    }

    private  List<String> getSalesChannelNames() {
        List<String> names=new ArrayList<String>();
        for(SalesChannel sc: salesChannelRepository.findAll()){
            names.add(sc.name());
        }
        return names;
    }

    public void onCancel(View view) {
        Intent intent = new Intent(CustomerFormActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onBack(View view) {
        finish();
    }

}
