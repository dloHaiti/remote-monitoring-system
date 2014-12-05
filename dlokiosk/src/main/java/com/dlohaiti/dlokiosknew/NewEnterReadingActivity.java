package com.dlohaiti.dlokiosknew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.dlohaiti.dlokiosknew.db.ReadingsRepository;
import com.dlohaiti.dlokiosknew.db.SamplingSiteParametersRepository;
import com.dlohaiti.dlokiosknew.db.SamplingSiteRepository;
import com.dlohaiti.dlokiosknew.domain.*;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.math.BigDecimal;
import java.util.*;

public class NewEnterReadingActivity extends RoboActivity {

    @InjectView(R.id.heading)
    private TextView heading;
    @InjectView(R.id.parameters)
    private ViewFlipper parametersList;
    @InjectView(R.id.save_parameters)
    private Button saveParametersButton;
    @InjectView(R.id.previous_parameter)
    private Button previousParameterButton;
    @InjectView(R.id.next_parameter)
    private Button nextParameterButton;
    @InjectResource(R.string.please_correct_message)
    private String pleaseCorrectMessage;
    @InjectResource(R.string.saved_message)
    private String savedMessage;
    @InjectResource(R.string.error_not_saved_message)
    private String errorNotSavedMessage;
    @Inject
    private SamplingSiteParametersRepository repository;
    @Inject
    private ReadingsRepository readingsRepository;
    @Inject
    private SamplingSiteRepository samplingSiteRepository;
    @Inject
    private Clock clock;
    private final Map<Parameter, EditText> values = new HashMap<Parameter, EditText>();
    private SamplingSite samplingSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        values.clear();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_enter_reading);
        Bundle extras = this.getIntent().getExtras();
        samplingSite = samplingSiteRepository.findByName(extras.getString("samplingSiteName"));
        heading.setText(samplingSite.getName());
//        SortedSet<Parameter> parameters = repository.findBySamplingSite(samplingSite);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        for (Parameter p : parameters) {
//            View row = inflater.inflate(R.layout.parameter_row, null);
//            TextView label = (TextView) row.findViewById(R.id.parameter_label);
//            TextView units = (TextView) row.findViewById(R.id.parameter_units);
//            TextView range = (TextView) row.findViewById(R.id.parameter_range);
//            final EditText input = (EditText) row.findViewById(R.id.parameter_input);
//            values.put(p, input);
//
//            if (p.isOkNotOk()) {
//                input.setVisibility(View.GONE);
//                RadioGroup okGroup = (RadioGroup) row.findViewById(R.id.ok_group);
//                okGroup.setVisibility(View.VISIBLE);
//                okGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup group, int checkedId) {
//                        if (R.id.radio_ok == checkedId) {
//                            input.setText("1");
//                        } else if (R.id.radio_not_ok == checkedId) {
//                            input.setText("0");
//                        } else {
//                            input.setText("");
//                        }
//                    }
//                });
//            }
//
//            label.setText(p.getName());
//            units.setText(p.getUnitOfMeasure());
//            if (p.hasRange() && !p.isOkNotOk()) {
//                range.setText(p.getRange());
//            }
//            parametersList.addView(row);
//        }
//        boolean hasSeveralParameters = parametersList.getChildCount() > 1;
//        nextParameterButton.setEnabled(hasSeveralParameters);
//        previousParameterButton.setEnabled(hasSeveralParameters);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SelectSamplingSiteActivity.class));
        finish();
    }

    public void nextParameter(View v) {
        if (currentViewIsValid()) {
            parametersList.showNext();
        }
    }

    public void previousParameter(View v) {
        if (currentViewIsValid()) {
            parametersList.showPrevious();
        }
    }

    public void saveParameters(View v) {
        if (allViewsAreValid()) {
            Set<Measurement> measurements = new HashSet<Measurement>();
            for (Map.Entry<Parameter, EditText> entry : values.entrySet()) {
                EditText input = entry.getValue();
                Parameter parameter = entry.getKey();
                String value = input.getText().toString();
                measurements.add(new Measurement(parameter, new BigDecimal(value)));
            }
            boolean successful = readingsRepository.save(new Reading(samplingSite, measurements, clock.now()));
            if (successful) {
                Toast.makeText(this, savedMessage, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, errorNotSavedMessage, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, pleaseCorrectMessage, Toast.LENGTH_LONG).show();
        }
    }

    private boolean allViewsAreValid() {
        boolean isValid = true;
        for (Map.Entry<Parameter, EditText> entry : values.entrySet()) {
            EditText input = entry.getValue();
            Parameter parameter = entry.getKey();
            String value = input.getText().toString();
            if (parameter.considersInvalid(value)) {
                isValid = false;
                CharSequence message = parameter.hasRange() ? parameter.getRange() : "Cannot be blank";
                input.setError(message);
            }
        }
        return isValid;
    }

    private boolean currentViewIsValid() {
        boolean isValid = true;
        EditText candidate = (EditText) parametersList.getCurrentView().findViewById(R.id.parameter_input);
        for (Map.Entry<Parameter, EditText> entry : values.entrySet()) {
            if (candidate == entry.getValue()) { // this is the one we care about
                EditText input = entry.getValue();
                Parameter parameter = entry.getKey();
                String value = input.getText().toString();
                if (parameter.considersInvalid(value)) {
                    isValid = false;
                    CharSequence message = parameter.hasRange() ? parameter.getRange() : "Cannot be blank";
                    input.setError(message);
                }
            }
        }
        return isValid;
    }
}
