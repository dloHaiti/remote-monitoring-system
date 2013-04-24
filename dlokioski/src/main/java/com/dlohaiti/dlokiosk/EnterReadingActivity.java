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
        SelectionValue color = getSelectionValueOfRadioButtons(R.id.button_color_ok, R.id.button_color_not_ok);
        SelectionValue odor = getSelectionValueOfRadioButtons(R.id.button_odor_ok, R.id.button_odor_not_ok);
        SelectionValue taste = getSelectionValueOfRadioButtons(R.id.button_taste_ok, R.id.button_taste_not_ok);

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

        List<Measurement> measurements = new ArrayList<Measurement>();
        measurements.add(new Measurement(MeasurementType.TEMPERATURE, temperature, temperatureLocation));
        measurements.add(new Measurement(MeasurementType.PH, pH, pHLocation));
        measurements.add(new Measurement(MeasurementType.TURBIDITY, turbidity, turbidityLocation));
        measurements.add(new Measurement(MeasurementType.TDS, tds, tdsLocation));
        measurements.add(new Measurement(MeasurementType.FREE_CHLORINE_CONCENTRATION, freeChlorineConcentration, freeChlorineConcentrationLocation));
        measurements.add(new Measurement(MeasurementType.TOTAL_CHLORINE_CONCENTRATION, totalChlorineConcentration, totalChlorineConcentrationLocation));
        measurements.add(new Measurement(MeasurementType.FREE_CHLORINE_RESIDUAL, freeChlorineResidual, freeChlorineResidualLocation));
        measurements.add(new Measurement(MeasurementType.TOTAL_CHLORINE_RESIDUAL, totalChlorineResidual, totalChlorineResidualLocation));
        measurements.add(new Measurement(MeasurementType.ALKALINITY, alkalinity, alkalinityLocation));
        measurements.add(new Measurement(MeasurementType.HARDNESS, hardness, hardnessLocation));
        measurements.add(new Measurement(MeasurementType.COLOR, color.name(), colorLocation));
        measurements.add(new Measurement(MeasurementType.ODOR, odor.name(), odorLocation));
        measurements.add(new Measurement(MeasurementType.TASTE, taste.name(), tasteLocation));

        SaveResult saveResult = repository.add(measurements);
        switch(saveResult) {
            case SUCCESSFUL:
                Toast.makeText(this, "saved measurements :)", Toast.LENGTH_LONG).show();
                break;
            case FAILURE:
                Toast.makeText(this, "failed to save measurements :(", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "unknown result.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private SelectionValue getSelectionValueOfRadioButtons(int okButtonId, int notOkButtonId) {
        if(((RadioButton) findViewById(okButtonId)).isChecked()) {
            return SelectionValue.OK;
        }
        if(((RadioButton) findViewById(notOkButtonId)).isChecked()) {
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
