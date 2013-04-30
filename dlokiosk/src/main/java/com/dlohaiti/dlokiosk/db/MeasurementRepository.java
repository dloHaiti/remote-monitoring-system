package com.dlohaiti.dlokiosk.db;

import android.content.Context;
import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.validation.MeasurementsValidator;
import com.dlohaiti.dlokiosk.domain.validation.ValidationResult;
import com.google.inject.Inject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class MeasurementRepository {

    private static final String FIELD_NAME_VALUE = "value";
    private static final String FIELD_NAME_TIMESTAMP = "timestamp";
    private static final String FIELD_NAME_MEASUREMENTS = "measurements";
    private static final String FIELD_NAME_PARAMETER = "parameter";
    private static final String FIELD_NAME_LOCATION = "location";
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss z";
    private static final String MEASUREMENTS_FILE = "dlokiosk.dat";

    private static final Logger logger = LoggerFactory.getLogger(MeasurementRepository.class);

    private MeasurementsValidator validator;
    private Context context;

    @Inject
    public MeasurementRepository(Context context, MeasurementsValidator validator) {
        this.context = context;
        this.validator = validator;
    }

    public String getAll() {
        try {
            byte[] buffer;
            FileInputStream inputStream = context.openFileInput(MEASUREMENTS_FILE);
            try {
                buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
            } finally {
                inputStream.close();
            }
            return new String(buffer);
        } catch (IOException e) {
            logger.error("Error while reading " + MEASUREMENTS_FILE, e);
            return EMPTY;
        }
    }

    public SaveResult add(List<Measurement> measurements) {
        return add(measurements, new Date());
    }

    public SaveResult add(List<Measurement> measurements, Date timestamp) {
        ValidationResult result = validator.validate(measurements);
        if (!result.passed()) {
            return new SaveResult(result);
        }
        boolean saveSuccessful = true;
        try {
            JSONObject data = formatData(measurements, timestamp);
            appendData(data);
        } catch (JSONException e) {
            //TODO: test this
            logger.error("Error while formatting json data", e);
            saveSuccessful = false;
        } catch (IOException e) {
            //TODO: test this
            logger.error("Error while writing to " + MEASUREMENTS_FILE, e);
            saveSuccessful = false;
        }
        return new SaveResult(result, saveSuccessful);
    }

    private JSONObject formatData(List<Measurement> measurements, Date timestamp) throws JSONException {

        JSONArray values = formatMeasurements(measurements);

        JSONObject data = new JSONObject();
        data.put(FIELD_NAME_TIMESTAMP, new SimpleDateFormat(DATE_FORMAT).format(timestamp));
        data.put(FIELD_NAME_MEASUREMENTS, values);

        JSONObject returnValue = new JSONObject();
        returnValue.put("reading", data);
        return returnValue;
    }

    private JSONArray formatMeasurements(List<Measurement> measurements) throws JSONException {
        JSONArray values = new JSONArray();

        for (Measurement measurement : measurements) {
            JSONObject value = new JSONObject();
            value.put(FIELD_NAME_PARAMETER, measurement.getName());
            value.put(FIELD_NAME_VALUE, measurement.getValue());
            value.put(FIELD_NAME_LOCATION, measurement.getSamplingSite());

            values.put(value);
        }

        return values;
    }

    private void appendData(JSONObject reading) throws IOException {
        FileOutputStream outputStream;

        outputStream = context.openFileOutput(MEASUREMENTS_FILE, Context.MODE_APPEND);
        try {
            outputStream.write(reading.toString().getBytes());
            outputStream.write('\n');
        } finally {
            outputStream.close();
        }
    }

    public boolean purge() {
        return context.deleteFile(MEASUREMENTS_FILE);
    }
}
