package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import com.dlohaiti.dlokiosk.db.MeasurementRepository;
import com.dlohaiti.dlokiosk.db.SaveResult;
import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.MeasurementLocation;
import com.dlohaiti.dlokiosk.domain.MeasurementType;
import com.dlohaiti.dlokiosk.domain.Reading;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.*;

import static com.dlohaiti.dlokiosk.domain.MeasurementLocation.WTU_EFF;
import static com.dlohaiti.dlokiosk.domain.MeasurementLocation.WTU_FEED;
import static com.dlohaiti.dlokiosk.domain.MeasurementType.*;

public class EnterReadingActivity extends RoboActivity {

    private Map<MeasurementType, LinearLayout> typeInputMap;
    @Inject private MeasurementRepository repository;
    @InjectView(R.id.temperature_row) private LinearLayout temperatureRow;
    @InjectView(R.id.ph_row) private LinearLayout phRow;
    @InjectView(R.id.turbidity_row) private LinearLayout turbidityRow;
    @InjectView(R.id.tds_row) private LinearLayout tdsRow;
    @InjectView(R.id.free_chlorine_concentration_row) private LinearLayout freeChlorineConcentrationRow;
    @InjectView(R.id.total_chlorine_concentration_row) private LinearLayout totalChlorineConcentrationRow;
    @InjectView(R.id.free_chlorine_residual_row) private LinearLayout freeChlorineResidualRow;
    @InjectView(R.id.total_chlorine_residual_row) private LinearLayout totalChlorineResidualRow;
    @InjectView(R.id.alkalinity_row) private LinearLayout alkalinityRow;
    @InjectView(R.id.hardness_row) private LinearLayout hardnessRow;
    @InjectView(R.id.color_row) private LinearLayout colorRow;
    @InjectView(R.id.odor_row) private LinearLayout odorRow;
    @InjectView(R.id.taste_row) private LinearLayout tasteRow;

    @InjectView(R.id.input_temperature) private EditText temperature;
    @InjectView(R.id.input_pH) private EditText pH;
    @InjectView(R.id.input_turbidity) private EditText turbidity;
    @InjectView(R.id.input_tds) private EditText tds;
    @InjectView(R.id.input_free_chlorine_concentration) private EditText freeChlorineConcentration;
    @InjectView(R.id.input_total_chlorine_concentration) private EditText totalChlorineConcentration;
    @InjectView(R.id.input_free_chlorine_residual) private EditText freeChlorineResidual;
    @InjectView(R.id.input_total_chlorine_residual) private EditText totalChlorineResidual;
    @InjectView(R.id.input_alkalinity) private EditText alkalinity;
    @InjectView(R.id.input_hardness) private EditText hardness;

    @InjectView(R.id.button_color_ok) private RadioButton colorOk;
    @InjectView(R.id.button_color_not_ok) private RadioButton colorNotOk;
    @InjectView(R.id.button_odor_ok) private RadioButton odorOk;
    @InjectView(R.id.button_odor_not_ok) private RadioButton odorNotOk;
    @InjectView(R.id.button_taste_ok) private RadioButton tasteOk;
    @InjectView(R.id.button_taste_not_ok) private RadioButton tasteNotOk;
    @InjectView(R.id.button_temperature_location_borehole) private RadioButton temperatureBorehole;
    @InjectView(R.id.button_temperature_location_wtu_eff) private RadioButton temperatureWtuEff;
    @InjectView(R.id.button_ph_location_borehole) private RadioButton phBorehole;
    @InjectView(R.id.button_ph_location_wtu_eff) private RadioButton phWtuEff;
    @InjectView(R.id.button_turbidity_location_borehole) private RadioButton turbidityBorehole;
    @InjectView(R.id.button_turbidity_location_wtu_eff) private RadioButton turbidityWtuEff;
    @InjectView(R.id.button_tds_location_borehole) private RadioButton tdsBorehole;
    @InjectView(R.id.button_tds_location_wtu_eff) private RadioButton tdsWtuEff;
    @InjectView(R.id.button_alkalinity_location_borehole) private RadioButton alkalinityBorehole;
    @InjectView(R.id.button_alkalinity_location_wtu_eff) private RadioButton alkalinityWtuEff;
    @InjectView(R.id.button_hardness_location_borehole) private RadioButton hardnessBorehole;
    @InjectView(R.id.button_hardness_location_wtu_eff) private RadioButton hardnessWtuEff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_reading);
        setupActionBar();
        typeInputMap = new HashMap<MeasurementType, LinearLayout>();
        typeInputMap.put(TEMPERATURE, temperatureRow);
        typeInputMap.put(PH, phRow);
        typeInputMap.put(TURBIDITY, turbidityRow);
        typeInputMap.put(TDS, tdsRow);
        typeInputMap.put(FREE_CHLORINE_CONCENTRATION, freeChlorineConcentrationRow);
        typeInputMap.put(TOTAL_CHLORINE_CONCENTRATION, totalChlorineConcentrationRow);
        typeInputMap.put(FREE_CHLORINE_RESIDUAL, freeChlorineResidualRow);
        typeInputMap.put(TOTAL_CHLORINE_RESIDUAL, totalChlorineResidualRow);
        typeInputMap.put(ALKALINITY, alkalinityRow);
        typeInputMap.put(HARDNESS, hardnessRow);
        typeInputMap.put(COLOR, colorRow);
        typeInputMap.put(ODOR, odorRow);
        typeInputMap.put(TASTE, tasteRow);
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
        clearValidationErrors();

        MeasurementLocation temperatureLocation = getLocation(temperatureBorehole, temperatureWtuEff);
        MeasurementLocation pHLocation = getLocation(phBorehole, phWtuEff);
        MeasurementLocation turbidityLocation = getLocation(turbidityBorehole, turbidityWtuEff);
        MeasurementLocation tdsLocation = getLocation(tdsBorehole, tdsWtuEff);
        MeasurementLocation alkalinityLocation = getLocation(alkalinityBorehole, alkalinityWtuEff);
        MeasurementLocation hardnessLocation = getLocation(hardnessBorehole, hardnessWtuEff);

        List<Measurement> measurements = new ArrayList<Measurement>();
        measurements.add(new Measurement(TEMPERATURE, temperature.getText().toString(), temperatureLocation));
        measurements.add(new Measurement(PH, pH.getText().toString(), pHLocation));
        measurements.add(new Measurement(TURBIDITY, turbidity.getText().toString(), turbidityLocation));
        measurements.add(new Measurement(TDS, tds.getText().toString(), tdsLocation));
        measurements.add(new Measurement(FREE_CHLORINE_CONCENTRATION, freeChlorineConcentration.getText().toString(), WTU_FEED));
        measurements.add(new Measurement(TOTAL_CHLORINE_CONCENTRATION, totalChlorineConcentration.getText().toString(), WTU_FEED));
        measurements.add(new Measurement(FREE_CHLORINE_RESIDUAL, freeChlorineResidual.getText().toString(), WTU_EFF));
        measurements.add(new Measurement(TOTAL_CHLORINE_RESIDUAL, totalChlorineResidual.getText().toString(), WTU_EFF));
        measurements.add(new Measurement(ALKALINITY, alkalinity.getText().toString(), alkalinityLocation));
        measurements.add(new Measurement(HARDNESS, hardness.getText().toString(), hardnessLocation));
        measurements.add(new Measurement(COLOR, getSelected(colorOk, colorNotOk).name(), WTU_EFF));
        measurements.add(new Measurement(ODOR, getSelected(odorOk, odorNotOk).name(), WTU_EFF));
        measurements.add(new Measurement(TASTE, getSelected(tasteOk, tasteNotOk).name(), WTU_EFF));

        Reading reading = new Reading();
        reading.setTimestamp(new Date());
        reading.setMeasurements(measurements);

        SaveResult saveResult = repository.add(reading);
        if (saveResult.successful()) {
            resetForm();
            Toast.makeText(this, "saved measurements :)", Toast.LENGTH_LONG).show();
        } else if (!saveResult.passedValidation()) {
            setValidationErrors(saveResult.getValidationFailures());
            Toast.makeText(this, "did not save! :( please correct invalid data", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "failed to save measurements :(", Toast.LENGTH_LONG).show();
        }
    }

    public Set<View> getInputRows() {
        return new HashSet<View>(typeInputMap.values());
    }

    private MeasurementLocation getLocation(RadioButton borehole, RadioButton wtuEff) {
        if (borehole.isChecked()) {
            return MeasurementLocation.BOREHOLE;
        }
        if (wtuEff.isChecked()) {
            return MeasurementLocation.WTU_EFF;
        }
        return MeasurementLocation.UNSELECTED;
    }

    private SelectionValue getSelected(RadioButton ok, RadioButton notOk) {
        if (ok.isChecked()) {
            return SelectionValue.OK;
        }
        if (notOk.isChecked()) {
            return SelectionValue.NOT_OK;
        }
        return SelectionValue.UNSELECTED;
    }

    private void setValidationErrors(Set<MeasurementType> validationFailures) {
        for (MeasurementType type : validationFailures) {
            View view = typeInputMap.get(type);
            view.setBackgroundColor(0x50FF0000);
        }
    }

    private void clearValidationErrors() {
        for (View row : typeInputMap.values()) {
            row.setBackgroundColor(0x00000000);
        }
    }

    private void resetForm() {
        for (EditText editText : Arrays.asList(temperature, pH, turbidity, tds, freeChlorineConcentration, totalChlorineConcentration, freeChlorineResidual, totalChlorineResidual, alkalinity, hardness)) {
            editText.setText("");
        }
        for (RadioButton radioButton : Arrays.asList(temperatureBorehole, temperatureWtuEff,
                phBorehole, phWtuEff,
                turbidityBorehole, turbidityWtuEff,
                tdsBorehole, tdsWtuEff,
                alkalinityBorehole, alkalinityWtuEff,
                hardnessBorehole, hardnessWtuEff,
                colorOk, colorNotOk,
                odorOk, odorNotOk,
                tasteOk, tasteNotOk)) {
            radioButton.setChecked(false);
        }

    }
}
