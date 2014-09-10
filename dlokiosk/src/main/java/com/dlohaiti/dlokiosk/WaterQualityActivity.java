package com.dlohaiti.dlokiosk;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dlohaiti.dlokiosk.adapter.ParameterAdapter;
import com.dlohaiti.dlokiosk.adapter.SamplingSiteAdapter;
import com.dlohaiti.dlokiosk.db.ReadingsRepository;
import com.dlohaiti.dlokiosk.db.SamplingSiteParametersRepository;
import com.dlohaiti.dlokiosk.db.SamplingSiteRepository;
import com.dlohaiti.dlokiosk.domain.Parameter;
import com.dlohaiti.dlokiosk.domain.SamplingSite;
import com.dlohaiti.dlokiosk.domain.SamplingSites;
import com.dlohaiti.dlokiosk.view_holder.LeftPaneListViewHolder;
import com.google.inject.Inject;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class WaterQualityActivity extends RoboActivity implements ActionBar.TabListener{

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

                if (selectedSamplingSite!= null) {
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
    }

    public void onBack(View view) {
        finish();
    }

    public void onCancel(View view) {
    }

    public void onSave(View view) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}