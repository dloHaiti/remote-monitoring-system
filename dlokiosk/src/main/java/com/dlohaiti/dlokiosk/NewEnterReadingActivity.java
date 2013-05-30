package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.db.SamplingSiteParametersRepository;
import com.dlohaiti.dlokiosk.domain.Parameter;
import com.dlohaiti.dlokiosk.domain.SampleSite;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.SortedSet;

public class NewEnterReadingActivity extends RoboActivity {

    @InjectView(R.id.heading) private TextView heading;
    @Inject private SamplingSiteParametersRepository repository;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_enter_reading);
        Bundle extras = this.getIntent().getExtras();
        String sampleSiteName = extras.getString("sampleSiteName");
        heading.setText(sampleSiteName);
        SortedSet<Parameter> parameters = repository.findBySampleSite(new SampleSite(sampleSiteName));
        //TODO: stick these parameters into a list
    }
}
