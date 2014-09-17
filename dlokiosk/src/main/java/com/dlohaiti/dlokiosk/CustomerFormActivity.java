package com.dlohaiti.dlokiosk;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.dlohaiti.dlokiosk.db.SalesChannelRepository;
import com.dlohaiti.dlokiosk.domain.SalesChannel;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class CustomerFormActivity extends RoboActivity {
    @Inject
    private SalesChannelRepository salesChannelRepository;

    @InjectView(R.id.sales_channel)
    protected Spinner salesChannel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_form);

        ArrayAdapter<String> salesChannelAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,getSalesChannelNames());
        salesChannel.setAdapter(salesChannelAdapter);
    }

    private  List<String> getSalesChannelNames() {
        List<String> names=new ArrayList<String>();
        for(SalesChannel sc: salesChannelRepository.findAll()){
            names.add(sc.name());
        }
        return names;
    }

}
