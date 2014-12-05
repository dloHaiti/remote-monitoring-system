package com.dlohaiti.dlokiosknew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.dlohaiti.dlokiosknew.db.SamplingSiteRepository;
import com.dlohaiti.dlokiosknew.domain.SamplingSite;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

public class SelectSamplingSiteActivity extends RoboActivity {

    @InjectView(R.id.sampling_site_spinner)
    private Spinner samplingSiteSpinner;
    @InjectResource(R.string.select_sampling_site_caption)
    private String selectSamplingSiteCaption;
    @Inject
    private SamplingSiteRepository repository;
    private boolean isRealSelection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sample_site);
        final List<String> sites = new ArrayList<String>();
        sites.add(selectSamplingSiteCaption);
        for (SamplingSite site : repository.list()) {
            sites.add(site.getName());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(sites);
        samplingSiteSpinner.setAdapter(adapter);
        samplingSiteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //HACK: populating the spinner fires onItemSelected for the first value in the spinner
                if (!isRealSelection) {
                    isRealSelection = true;
                } else {
                    Intent intent = new Intent(SelectSamplingSiteActivity.this, NewEnterReadingActivity.class);
                    intent.putExtra("samplingSiteName", sites.get(position));
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // intentionally left blank
            }
        });
    }
}
