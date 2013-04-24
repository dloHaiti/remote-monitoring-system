package com.dlohaiti.dlokiosk.db;

import android.content.Context;
import com.dlohaiti.dlokiosk.Measurement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MeasurementRepository {

    private static final String FIELD_NAME_VALUE = "value";
    private static final String FIELD_NAME_TIMESTAMP = "timestamp";
    private static final String FIELD_NAME_MEASUREMENTS = "measurements";
    private static final String FIELD_NAME_PARAMETER = "parameter";
    private static final String FIELD_NAME_LOCATION = "location";
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss z";
    private static final String MEASUREMENTS_FILE = "dlokioski.dat";

    private Context context;

    public MeasurementRepository(Context context) {
        this.context = context;
    }

    public SaveResult add(List<Measurement> measurements) {
        return add(measurements, new Date());
    }

    public SaveResult add(List<Measurement> measurements, Date timestamp) {
        JSONObject data;
        try {
            data = formatData(measurements, timestamp);
        } catch (JSONException e) {
            return SaveResult.FAILURE;
        }
        try {
            appendData(data);
        } catch (IOException e) {
            return SaveResult.FAILURE;
        }
        return SaveResult.SUCCESSFUL;
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

        for (Measurement measurement: measurements) {
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
        outputStream.write(reading.toString().getBytes());
        outputStream.close();
    }

}
