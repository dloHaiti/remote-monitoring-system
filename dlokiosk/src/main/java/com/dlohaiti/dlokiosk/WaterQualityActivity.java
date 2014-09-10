package com.dlohaiti.dlokiosk;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dlohaiti.dlokiosk.adapter.ParameterAdapter;
import com.dlohaiti.dlokiosk.adapter.SamplingSiteAdapter;
import com.dlohaiti.dlokiosk.db.ReadingsRepository;
import com.dlohaiti.dlokiosk.db.SamplingSiteParametersRepository;
import com.dlohaiti.dlokiosk.db.SamplingSiteRepository;
import com.dlohaiti.dlokiosk.domain.Clock;
import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.Parameter;
import com.dlohaiti.dlokiosk.domain.Reading;
import com.dlohaiti.dlokiosk.domain.SamplingSite;
import com.dlohaiti.dlokiosk.domain.SamplingSites;
import com.dlohaiti.dlokiosk.view_holder.LeftPaneListViewHolder;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class WaterQualityActivity extends RoboActivity implements ActionBar.TabListener {

    @InjectView(R.id.site_list)
    private ListView samplingSiteListView;

    @InjectView(R.id.parameter_list)
    private ListView parameterListView;

    @Inject
    private SamplingSiteRepository repository;

    @Inject
    private ReadingsRepository readingsRepository;

    @Inject
    private SamplingSiteParametersRepository samplingSiteParametersRepository;

    private SamplingSites samplingSites;
    private SamplingSite selectedSamplingSite;
    private SamplingSiteAdapter samplingSiteAdapter;

    @Inject
    private Clock clock;
    @InjectResource(R.string.saved_message)
    private String savedMessage;
    @InjectResource(R.string.error_not_saved_message)
    private String errorNotSavedMessage;

    private boolean isToday = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waterquality);
        createActionBarTabs();
        loadSamplingSites();
    }

    private void createActionBarTabs() {
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("Today")
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Yesterday")
                .setTabListener(this));
    }

    private void loadSamplingSites() {
        samplingSites = new SamplingSites(repository.listAllWaterQualityChannel());
        if (samplingSites.isEmpty()) {
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
                            }
                    )
                    .show();
        } else {
            samplingSites.get(0).select();
            selectedSamplingSite = samplingSites.get(0);
            initialiseSamplingSiteListView();
            updateParameterList();
        }
    }

    private void initialiseSamplingSiteListView() {
        samplingSiteAdapter = new SamplingSiteAdapter(this.getApplicationContext(), samplingSites);
        samplingSiteListView.setAdapter(samplingSiteAdapter);
        samplingSiteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SamplingSite tappedSamplingSite = samplingSites
                        .findSamplingSiteById(((LeftPaneListViewHolder) view.getTag()).id);

                if (selectedSamplingSite != null) {
                    selectedSamplingSite.unSelect();
                }
                tappedSamplingSite.select();
                selectedSamplingSite = tappedSamplingSite;
                samplingSiteAdapter.notifyDataSetChanged();
                updateParameterList();
            }


        });
    }

    private void updateParameterList() {
        List<Parameter> parameters = (List<Parameter>) samplingSiteParametersRepository.findBySamplingSite(selectedSamplingSite);
        ParameterAdapter parameterAdapter = new ParameterAdapter(getApplicationContext(), parameters);
        parameterListView.setAdapter(parameterAdapter);
        fillParameterValues();
    }

    private void fillParameterValues() {
        cleanCurrentValues();
        Date date = getSelectedDate();
        if (selectedSamplingSite == null) return;
        Reading reading = readingsRepository.getReadingsWithDateAndSamplingSite(date, selectedSamplingSite.getName());
        ParameterAdapter parameterAdapter = (ParameterAdapter) parameterListView.getAdapter();
        if (reading == null || parameterAdapter == null) return;
        for (int i = 0; i < parameterAdapter.getCount(); i++) {
            Parameter p = parameterAdapter.getItem(i);
            Measurement measurement = reading.getMeasurement(p.getName());
            if (measurement != null) {
                p.setValue(String.valueOf(measurement.getValue()));
            }else{
                p.setValue("");
            }
        }
        parameterAdapter.notifyDataSetChanged();
    }

    private void cleanCurrentValues() {
        ParameterAdapter parameterAdapter = (ParameterAdapter) parameterListView.getAdapter();
        if(parameterAdapter==null) return;
        parameterAdapter.setDisplayError(false);
        for (int i = 0; i < parameterAdapter.getCount(); i++) {
            Parameter p = parameterAdapter.getItem(i);
            p.setValue(String.valueOf(""));
        }
        parameterAdapter.notifyDataSetChanged();
    }

    public void onBack(View view) {
        finish();
    }

    public void onCancel(View view) {
    }

    public void onSave(View view) {
        Set<Measurement> measurements;
        Date date = getSelectedDate();
        Reading reading = readingsRepository.getReadingsWithDateAndSamplingSite(date, selectedSamplingSite.getName());
        if (reading == null) {
            reading = new Reading(null, selectedSamplingSite.getName(), new HashSet<Measurement>(), date);
        }

        measurements = reading.getMeasurements();
        ParameterAdapter parameterAdapter = (ParameterAdapter) parameterListView.getAdapter();

        for (int i = 0; i < parameterAdapter.getCount(); i++) {
            Parameter parameter = parameterAdapter.getItem(i);
            if (parameter.getValue().isEmpty()) {
                continue;
            }
            Measurement measurement = reading.getMeasurement(parameter.getName());
            if (measurement == null) {
                measurements.add(new Measurement(parameter.getName(), new BigDecimal(parameter.getValue())));
            } else {
                measurement.setValue(new BigDecimal(parameter.getValue()));
            }
        }
        boolean successful = readingsRepository.save(reading);
        if (successful) {
            Toast.makeText(this, savedMessage, Toast.LENGTH_SHORT).show();
//            finish();
        } else {
            Toast.makeText(this, errorNotSavedMessage, Toast.LENGTH_LONG).show();
        }
    }

    private Date getSelectedDate() {
        return isToday ? clock.today() : clock.yesterday();
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        isToday = (tab.getPosition() == 0);
        fillParameterValues();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}