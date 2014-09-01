package com.dlohaiti.dlokiosk;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dlohaiti.dlokiosk.db.SamplingSiteRepository;
import com.dlohaiti.dlokiosk.domain.FlowMeterReadings;
import com.dlohaiti.dlokiosk.widgets.FlowMeterAdapter;
import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class FlowMeterReadingActivity extends RoboActivity {

    @InjectView(R.id.flow_meter_list)
    private ListView flowMeterList;

    @Inject
    private SamplingSiteRepository repository;
    private FlowMeterAdapter flowMeterAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowmeter);

        FlowMeterReadings samplingSites = new FlowMeterReadings(repository.ListAllFlowMeterSites());


        flowMeterAdapter = new FlowMeterAdapter(getApplicationContext(), samplingSites);

        flowMeterList.setAdapter(flowMeterAdapter);
//        LayoutInflater inflater = getLayoutInflater();
//        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.flow_meter_header, flowMeterList, false);
//
//        flowMeterList.addHeaderView(header);
    }

    public void cancelClick(View view) {
//        SamplingSite item = flowMeterAdapter.getItem(0);
        Intent intent = new Intent(FlowMeterReadingActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
