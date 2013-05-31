package com.dlohaiti.dlokiosk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dlohaiti.dlokiosk.db.SamplingSiteParametersRepository;
import com.dlohaiti.dlokiosk.domain.Parameter;
import com.dlohaiti.dlokiosk.domain.SampleSite;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

public class NewEnterReadingActivity extends RoboActivity {

    @InjectView(R.id.heading) private TextView heading;
    @InjectView(R.id.parameters) private LinearLayout parametersList;
    @Inject private SamplingSiteParametersRepository repository;
    private final Map<Parameter, EditText> values = new HashMap<Parameter, EditText>();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_enter_reading);
        Bundle extras = this.getIntent().getExtras();
        String sampleSiteName = extras.getString("sampleSiteName");
        heading.setText(sampleSiteName);
        SortedSet<Parameter> parameters = repository.findBySampleSite(new SampleSite(sampleSiteName));
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row;
        TextView label;
        TextView units;
        TextView range;
        for(final Parameter p : parameters) {
            row = inflater.inflate(R.layout.parameter_row, null);
            label = (TextView)row.findViewById(R.id.parameter_label);
            units = (TextView)row.findViewById(R.id.parameter_units);
            range = (TextView)row.findViewById(R.id.parameter_range);
            final EditText input = (EditText)row.findViewById(R.id.parameter_input);
            values.put(p, input);

            label.setText(p.getName());
            units.setText(p.getUnitOfMeasure());
            if(p.hasRange()) {
                range.setText(p.getRange());
            }
            parametersList.addView(row);
        }
    }

    public void saveParameters(View v) {
        boolean errors = false;
        for(Map.Entry<Parameter, EditText> value : values.entrySet()) {
            EditText input = value.getValue();
            Parameter parameter = value.getKey();
            if(parameter.considersInvalid(input.getText().toString())) {
                errors = true;
                CharSequence message = parameter.hasRange() ? parameter.getRange() : "Cannot be blank";
                input.setError(message);
            }
        }
        if(errors) {
            Toast.makeText(this, "Please correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Submitted soon! (Not Yet Implemented)", Toast.LENGTH_SHORT).show();
        }
    }
}
