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

    private static final String FIELD_NAME_TIMESTAMP = "timestamp";
    private static final String FIELD_NAME_MEASUREMENTS = "measurements";
    private static final String DATE_FORMAT = "yyyy-mm-dd hh:MM:ss";
    private static String MEASUREMENTS_FILE = "dlokioski.dat";

    private Context context;

    public MeasurementRepository(Context context) {
        this.context = context;
    }

    public SaveResult add(List<Measurement> measurements) {
        JSONObject data;
        try {
            data = formatData(measurements, new Date());
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

    private JSONObject formatData(List<Measurement> measurements, Date date) throws JSONException {
        JSONArray values = new JSONArray(measurements);
        JSONObject data = new JSONObject();
        data.put(FIELD_NAME_TIMESTAMP, new SimpleDateFormat(DATE_FORMAT).format(date));
        data.put(FIELD_NAME_MEASUREMENTS, values);
        return data;
    }

    private void appendData(JSONObject reading) throws IOException {
        FileOutputStream outputStream;

        outputStream = context.openFileOutput(MEASUREMENTS_FILE, Context.MODE_APPEND);
        outputStream.write(reading.toString().getBytes());
        outputStream.close();
    }

}
