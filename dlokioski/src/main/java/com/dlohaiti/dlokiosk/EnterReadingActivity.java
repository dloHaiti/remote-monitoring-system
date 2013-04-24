package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.dlohaiti.dlokiosk.db.MeasurementRepository;
import com.dlohaiti.dlokiosk.db.SaveResult;
import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.MeasurementLocation;
import com.dlohaiti.dlokiosk.domain.MeasurementType;
import com.dlohaiti.dlokiosk.domain.validation.MeasurementsValidator;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;

import java.util.ArrayList;
import java.util.List;

public class EnterReadingActivity extends RoboActivity {

    private MeasurementRepository repository;
    @Inject private MeasurementsValidator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_reading);
        setupActionBar();
        repository = new MeasurementRepository(this, validator);
    }

    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.enter_reading, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveReadings(View v) {
        String temperature = getStringValueOfEditText(R.id.input_temperature);
        String pH = getStringValueOfEditText(R.id.input_pH);
        String turbidity = getStringValueOfEditText(R.id.input_turbidity);
        String tds = getStringValueOfEditText(R.id.input_tds);
        String freeChlorineConcentration = getStringValueOfEditText(R.id.input_free_chlorine_concentration);
        String totalChlorineConcentration = getStringValueOfEditText(R.id.input_total_chlorine_concentration);
        String freeChlorineResidual = getStringValueOfEditText(R.id.input_free_chlorine_residual);
        String totalChlorineResidual = getStringValueOfEditText(R.id.input_total_chlorine_residual);
        String alkalinity = getStringValueOfEditText(R.id.input_alkalinity);
        String hardness = getStringValueOfEditText(R.id.input_hardness);
        SelectionValue color = getSelectionValueOfRadioButtons(R.id.button_color_ok, R.id.button_color_not_ok);
        SelectionValue odor = getSelectionValueOfRadioButtons(R.id.button_odor_ok, R.id.button_odor_not_ok);
        SelectionValue taste = getSelectionValueOfRadioButtons(R.id.button_taste_ok, R.id.button_taste_not_ok);

        MeasurementLocation temperatureLocation = getMeasurementLocation(R.id.button_temperature_location_borehole, R.id.button_temperature_location_wtu_eff);
        MeasurementLocation pHLocation = getMeasurementLocation(R.id.button_ph_location_borehole, R.id.button_ph_location_wtu_eff);
        MeasurementLocation turbidityLocation = getMeasurementLocation(R.id.button_turbidity_location_borehole, R.id.button_turbidity_location_wtu_eff);
        MeasurementLocation tdsLocation = getMeasurementLocation(R.id.button_tds_location_borehole, R.id.button_tds_location_wtu_eff);
        MeasurementLocation alkalinityLocation = getMeasurementLocation(R.id.button_alkalinity_location_borehole, R.id.button_alkalinity_location_wtu_eff);
        MeasurementLocation hardnessLocation = getMeasurementLocation(R.id.button_hardness_location_borehole, R.id.button_hardness_location_wtu_eff);

        List<Measurement> measurements = new ArrayList<Measurement>();
        measurements.add(new Measurement(MeasurementType.TEMPERATURE, temperature, temperatureLocation));
        measurements.add(new Measurement(MeasurementType.PH, pH, pHLocation));
        measurements.add(new Measurement(MeasurementType.TURBIDITY, turbidity, turbidityLocation));
        measurements.add(new Measurement(MeasurementType.TDS, tds, tdsLocation));
        measurements.add(new Measurement(MeasurementType.FREE_CHLORINE_CONCENTRATION, freeChlorineConcentration, MeasurementLocation.WTU_FEED));
        measurements.add(new Measurement(MeasurementType.TOTAL_CHLORINE_CONCENTRATION, totalChlorineConcentration, MeasurementLocation.WTU_FEED));
        measurements.add(new Measurement(MeasurementType.FREE_CHLORINE_RESIDUAL, freeChlorineResidual, MeasurementLocation.WTU_EFF));
        measurements.add(new Measurement(MeasurementType.TOTAL_CHLORINE_RESIDUAL, totalChlorineResidual, MeasurementLocation.WTU_EFF));
        measurements.add(new Measurement(MeasurementType.ALKALINITY, alkalinity, alkalinityLocation));
        measurements.add(new Measurement(MeasurementType.HARDNESS, hardness, hardnessLocation));
        measurements.add(new Measurement(MeasurementType.COLOR, color.name(), MeasurementLocation.WTU_EFF));
        measurements.add(new Measurement(MeasurementType.ODOR, odor.name(), MeasurementLocation.WTU_EFF));
        measurements.add(new Measurement(MeasurementType.TASTE, taste.name(), MeasurementLocation.WTU_EFF));

        SaveResult saveResult = repository.add(measurements);
        if(saveResult.successful()) {
            Toast.makeText(this, "saved measurements :)", Toast.LENGTH_LONG).show();
        } else if(!saveResult.passedValidation()) {
            Toast.makeText(this, "did not save! :( please correct invalid data", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "failed to save measurements :(", Toast.LENGTH_LONG).show();
        }
    }

    private MeasurementLocation getMeasurementLocation(int boreholeButtonId, int wtuEffButtonId) {
        if (isChecked(boreholeButtonId)) {
            return MeasurementLocation.BOREHOLE;
        }
        if (isChecked(wtuEffButtonId)) {
            return MeasurementLocation.WTU_EFF;
        }
        return MeasurementLocation.UNSELECTED;
    }

    private SelectionValue getSelectionValueOfRadioButtons(int okButtonId, int notOkButtonId) {
        if (isChecked(okButtonId)) {
            return SelectionValue.OK;
        }
        if (isChecked(notOkButtonId)) {
            return SelectionValue.NOT_OK;
        }
        return SelectionValue.UNSELECTED;
    }

    private boolean isChecked(int buttonId) {
        return ((RadioButton) findViewById(buttonId)).isChecked();
    }

    private String getStringValueOfEditText(int inputId) {
        return ((TextView) findViewById(inputId)).getText().toString();
    }
}
