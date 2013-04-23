package com.dlohaiti.dlokiosk;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

public class EnterReadingActivity extends Activity {

    private MeasurementRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_reading);
        setupActionBar();
        repository = new MeasurementRepository(this);
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
        boolean colorIsOK = isChecked(R.id.button_color_ok);
        boolean odorIsOK = isChecked(R.id.button_odor_ok);
        boolean tasteIsOK = isChecked(R.id.button_taste_ok);

        MeasurementLocation temperatureLocation = null;
        MeasurementLocation pHLocation = null;
        MeasurementLocation turbidityLocation = null;
        MeasurementLocation tdsLocation = null;
        MeasurementLocation alkalinityLocation = null;
        MeasurementLocation hardnessLocation = null;
        MeasurementLocation freeChlorineConcentrationLocation = MeasurementLocation.WTU_FEED;
        MeasurementLocation totalChlorineConcentrationLocation = MeasurementLocation.WTU_FEED;
        MeasurementLocation freeChlorineResidualLocation = MeasurementLocation.WTU_EFF;
        MeasurementLocation totalChlorineResidualLocation = MeasurementLocation.WTU_EFF;
        MeasurementLocation colorLocation = MeasurementLocation.WTU_EFF;
        MeasurementLocation odorLocation = MeasurementLocation.WTU_EFF;
        MeasurementLocation tasteLocation = MeasurementLocation.WTU_EFF;

        if (isChecked(R.id.button_temperature_location_borehole)) {
            temperatureLocation = MeasurementLocation.BOREHOLE;
        } else if (isChecked(R.id.button_temperature_location_wtu_eff)) {
            temperatureLocation = MeasurementLocation.WTU_EFF;
        } else {
            // do something when site not selected
        }

        if (isChecked(R.id.button_ph_location_borehole)) {
            pHLocation = MeasurementLocation.BOREHOLE;
        } else if (isChecked(R.id.button_ph_location_wtu_eff)) {
            pHLocation = MeasurementLocation.WTU_EFF;
        } else {
            // do something when site not selected
        }

        if (isChecked(R.id.button_turbidity_location_borehole)) {
            turbidityLocation = MeasurementLocation.BOREHOLE;
        } else if (isChecked(R.id.button_turbidity_location_wtu_eff)) {
            turbidityLocation = MeasurementLocation.WTU_EFF;
        } else {
            // do something when site not selected
        }

        if (isChecked(R.id.button_tds_location_borehole)) {
            tdsLocation = MeasurementLocation.BOREHOLE;
        } else if (isChecked(R.id.button_tds_location_wtu_eff)) {
            tdsLocation = MeasurementLocation.WTU_EFF;
        } else {
            // do something when site not selected
        }

        if (isChecked(R.id.button_alkalinity_location_borehole)) {
            alkalinityLocation = MeasurementLocation.BOREHOLE;
        } else if (isChecked(R.id.button_alkalinity_location_wtu_eff)) {
            alkalinityLocation = MeasurementLocation.WTU_EFF;
        } else {
            // do something when site not selected
        }

        if (isChecked(R.id.button_hardness_location_borehole)) {
            hardnessLocation = MeasurementLocation.BOREHOLE;
        } else if (isChecked(R.id.button_hardness_location_wtu_eff)) {
            hardnessLocation = MeasurementLocation.WTU_EFF;
        } else {
            // do something when site not selected (don't let submit form?)
        }

        List<Reading> readings = new ArrayList<Reading>();
        readings.add(new Reading(Measurements.TEMPERATURE, temperature, temperatureLocation));
        readings.add(new Reading(Measurements.PH, pH, pHLocation));
        readings.add(new Reading(Measurements.TURBIDITY, turbidity, turbidityLocation));
        readings.add(new Reading(Measurements.TDS, tds, tdsLocation));
        readings.add(new Reading(Measurements.FREE_CHLORINE_CONCENTRATION, freeChlorineConcentration, freeChlorineConcentrationLocation));
        readings.add(new Reading(Measurements.TOTAL_CHLORINE_CONCENTRATION, totalChlorineConcentration, totalChlorineConcentrationLocation));
        readings.add(new Reading(Measurements.FREE_CHLORINE_RESIDUAL, freeChlorineResidual, freeChlorineResidualLocation));
        readings.add(new Reading(Measurements.TOTAL_CHLORINE_RESIDUAL, totalChlorineResidual, totalChlorineResidualLocation));
        readings.add(new Reading(Measurements.ALKALINITY, alkalinity, alkalinityLocation));
        readings.add(new Reading(Measurements.HARDNESS, hardness, hardnessLocation));
        readings.add(new Reading(Measurements.COLOR, okToString(colorIsOK), colorLocation));
        readings.add(new Reading(Measurements.ODOR, okToString(odorIsOK), odorLocation));
        readings.add(new Reading(Measurements.TASTE, okToString(tasteIsOK), tasteLocation));

        SaveResult saveResult = repository.add(readings);
        switch(saveResult) {
            case SUCCESSFUL:
                Toast.makeText(this, "saved readings :)", Toast.LENGTH_LONG).show();
                break;
            case FAILURE:
                Toast.makeText(this, "failed to save readings :(", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "unknown result.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private String okToString(boolean ok) {
        return ok ? "OK" : "NOT OK";
    }

    private boolean isChecked(int buttonId) {
        return ((RadioButton) findViewById(buttonId)).isChecked();
    }

    private String getStringValueOfEditText(int inputId) {
        return ((TextView) findViewById(inputId)).getText().toString();
    }
}
