package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.dlohaiti.dlokiosk.db.SamplingSiteParametersRepository;
import com.dlohaiti.dlokiosk.domain.Parameter;
import com.dlohaiti.dlokiosk.domain.SampleSite;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.SortedSet;

public class NewEnterReadingActivity extends RoboActivity {

    @InjectView(R.id.heading) private TextView heading;
    @InjectView(R.id.parameters) private ListView parametersList;
    @Inject private SamplingSiteParametersRepository repository;
    private ParameterAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_enter_reading);
        Bundle extras = this.getIntent().getExtras();
        String sampleSiteName = extras.getString("sampleSiteName");
        heading.setText(sampleSiteName);
        SortedSet<Parameter> parameters = repository.findBySampleSite(new SampleSite(sampleSiteName));
        adapter = new ParameterAdapter(this, R.layout.parameter_row, new ArrayList<Parameter>(parameters));
        parametersList.setAdapter(adapter);
    }

    public void saveParameters(View v) {
        boolean anyInError = adapter.hasInvalidParameters();
        if(anyInError) {
            Toast.makeText(this, "Please make corrections", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Submitted soon! (Not Yet Implemented)", Toast.LENGTH_SHORT).show();
        }
    }
}
