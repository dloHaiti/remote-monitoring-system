package com.dlohaiti.dlokiosk;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dlohaiti.dlokiosk.db.ReadingsRepository;
import com.dlohaiti.dlokiosk.db.SamplingSiteRepository;
import com.dlohaiti.dlokiosk.domain.Clock;
import com.dlohaiti.dlokiosk.domain.FlowMeterReading;
import com.dlohaiti.dlokiosk.domain.FlowMeterReadings;
import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.Reading;
import com.dlohaiti.dlokiosk.widgets.FlowMeterAdapter;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class FlowMeterReadingActivity extends RoboActivity implements ActionBar.TabListener {

    @InjectView(R.id.flow_meter_list)
    private ListView flowMeterList;

    @InjectResource(R.string.saved_message)
    private String savedMessage;
    @InjectResource(R.string.error_not_saved_message)
    private String errorNotSavedMessage;

    @Inject
    private SamplingSiteRepository repository;
    @Inject
    private ReadingsRepository readingsRepository;


    private FlowMeterAdapter flowMeterAdapter;

    @Inject
    private Clock clock;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowmeter);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // for each of the sections in the app, add a tab to the action bar.
        actionBar.addTab(actionBar.newTab().setText("Today")
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Yesterday")
                .setTabListener(this));

        FlowMeterReadings samplingSites = new FlowMeterReadings(repository.ListAllFlowMeterSites());

        flowMeterAdapter = new FlowMeterAdapter(getApplicationContext(), samplingSites);

        flowMeterList.setAdapter(flowMeterAdapter);

        fillQuantity(clock.today());

    }

    public void cancelClick(View view) {
//        SamplingSite item = flowMeterAdapter.getItem(0);
        Intent intent = new Intent(FlowMeterReadingActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if(tab.getPosition()==0){
            fillQuantity(clock.today());
        }else{
            fillQuantity(clock.yesterday());
        }
    }

    private void fillQuantity(Date date) {
        if(flowMeterAdapter==null)return;
        List<Reading> readingWithDate = readingsRepository.getReadingsWithDate(date);
        if (readingWithDate.size()==0){
            flowMeterAdapter.cleanQuantity();
            return;
        }
        for(int i=0;i<flowMeterAdapter.getCount();i++) {
            FlowMeterReading item = flowMeterAdapter.getItem(i);
            Reading readingWithSite = findReadingWithSite(readingWithDate, item.getSamplingName());
            if(readingWithSite!=null) {
                Measurement measurement = readingWithSite.getMeasurement(item.getParameterName());
                if (measurement != null) {
                    item.setQuantity(String.valueOf(measurement.getValue()));
                }
            }
        }
        flowMeterAdapter.notifyDataSetChanged();
    }

    private Reading findReadingWithSite(List<Reading> readingWithDate, String samplingName) {
        for(Reading reading:readingWithDate){
            if(reading.getSamplingSiteName().equalsIgnoreCase(samplingName)){
                return reading;
            }
        }
        return null;
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public void onSaveReadings(View view) {
        if(validateAllFields()){
            saveReadings();
        }
    }

    private void saveReadings() {
        Set<Measurement> measurements = new HashSet<Measurement>();
        boolean successful=true;

        List<Reading> readingsWithDate = readingsRepository.getReadingsWithDate(clock.today());

        for(int i=0;i<flowMeterAdapter.getCount();i++){
            FlowMeterReading flowMeterReading = flowMeterAdapter.getItem(i);
            Reading readingWithSite = findReadingWithSite(readingsWithDate, flowMeterReading.getSamplingName());
            if(readingWithSite==null){
                measurements.add(new Measurement(flowMeterReading.getParameterName(), new BigDecimal(flowMeterReading.getQuantity())));
                readingWithSite= new Reading(null,flowMeterReading.getSamplingName(), measurements, clock.now());
            }else{
                Measurement measurement = readingWithSite.getMeasurement(flowMeterReading.getParameterName());
                if(measurement!=null){
                    measurement.setValue(new BigDecimal(flowMeterReading.getQuantity()));
                }else{
                    measurements.add(new Measurement(flowMeterReading.getParameterName(), new BigDecimal(flowMeterReading.getQuantity())));
                }
            }
            successful = readingsRepository.save(readingWithSite);
            if(!successful){
                break;
            }
            measurements.clear();
        }

        Log.d("SAVE",String.valueOf(successful));
        if (successful) {
            Toast.makeText(this, savedMessage, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, errorNotSavedMessage, Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateAllFields() {
        FlowMeterAdapter adapter = (FlowMeterAdapter) flowMeterList.getAdapter();
        boolean isError=false;

        for(int i=0;i<adapter.getCount();i++){
            FlowMeterReading item = adapter.getItem(i);
            if(item.getQuantity().isEmpty()){
                isError=true;
                break;
              }
        }
        if(isError){
            adapter.setDisplayError(true);
            adapter.notifyDataSetChanged();
        }

        return !isError;
    }

}
