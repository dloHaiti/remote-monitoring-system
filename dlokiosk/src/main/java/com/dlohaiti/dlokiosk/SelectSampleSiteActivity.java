package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.dlohaiti.dlokiosk.db.SampleSiteRepository;
import com.dlohaiti.dlokiosk.domain.SampleSite;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

public class SelectSampleSiteActivity extends RoboActivity {

    @InjectView(R.id.sample_site_spinner) private Spinner sampleSiteSpinner;
    @Inject private SampleSiteRepository repository;
    private boolean isRealSelection = false;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sample_site);
        final List<CharSequence> sites = new ArrayList<CharSequence>();
        sites.add("Select a Location");
        for(SampleSite site : repository.list()) {
            sites.add(site.getName());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(sites);
        sampleSiteSpinner.setAdapter(adapter);
        sampleSiteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //HACK: populating the spinner fires onItemSelected for the first value in the spinner
                if(!isRealSelection) {
                    isRealSelection = true;
                } else {
                    Toast.makeText(SelectSampleSiteActivity.this, String.format("selected %s", sites.get(position)), Toast.LENGTH_SHORT).show();
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {
                // intentionally left blank
            }
        });
    }
}
