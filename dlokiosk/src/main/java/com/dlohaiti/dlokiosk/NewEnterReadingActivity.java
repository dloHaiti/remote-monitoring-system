package com.dlohaiti.dlokiosk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dlohaiti.dlokiosk.db.MeasurementsRepository;
import com.dlohaiti.dlokiosk.db.SamplingSiteParametersRepository;
import com.dlohaiti.dlokiosk.db.SamplingSiteRepository;
import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.Parameter;
import com.dlohaiti.dlokiosk.domain.SamplingSite;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.math.BigDecimal;
import java.util.*;

public class NewEnterReadingActivity extends RoboActivity {

    @InjectView(R.id.heading) private TextView heading;
    @InjectView(R.id.parameters) private LinearLayout parametersList;
    @Inject private SamplingSiteParametersRepository repository;
    @Inject private MeasurementsRepository measurementsRepository;
    @Inject private SamplingSiteRepository samplingSiteRepository;
    private final Map<Parameter, EditText> values = new HashMap<Parameter, EditText>();
    private SamplingSite samplingSite;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_enter_reading);
        Bundle extras = this.getIntent().getExtras();
        samplingSite = samplingSiteRepository.findById(extras.getInt("samplingSiteId"));
        heading.setText(samplingSite.getName());
        SortedSet<Parameter> parameters = repository.findBySamplingSite(samplingSite);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(Parameter p : parameters) {
            View row = inflater.inflate(R.layout.parameter_row, null);
            TextView label = (TextView)row.findViewById(R.id.parameter_label);
            TextView units = (TextView)row.findViewById(R.id.parameter_units);
            TextView range = (TextView)row.findViewById(R.id.parameter_range);
            EditText input = (EditText)row.findViewById(R.id.parameter_input);
            values.put(p, input);

            label.setText(p.getName());
            units.setText(p.getUnitOfMeasure());
            if(p.hasRange()) {
                range.setText(p.getRange());
            }
            parametersList.addView(row);
        }
    }

    @Override public void onBackPressed() {
        startActivity(new Intent(this, SelectSamplingSiteActivity.class));
        finish();
    }

    public void saveParameters(View v) {
        boolean errors = false;
        List<Measurement> measurements = new ArrayList<Measurement>();
        for(Map.Entry<Parameter, EditText> entry : values.entrySet()) {
            EditText input = entry.getValue();
            Parameter parameter = entry.getKey();
            String value = input.getText().toString();
            if(parameter.considersInvalid(value)) {
                errors = true;
                CharSequence message = parameter.hasRange() ? parameter.getRange() : "Cannot be blank";
                input.setError(message);
            } else {
                measurements.add(new Measurement(parameter, new BigDecimal(value)));
            }
        }
        if(errors) {
            Toast.makeText(this, "Please correct!", Toast.LENGTH_SHORT).show();
        } else {
            boolean successful = measurementsRepository.save(measurements, samplingSite);
            if(successful) {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "There was an error. Data not saved!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
