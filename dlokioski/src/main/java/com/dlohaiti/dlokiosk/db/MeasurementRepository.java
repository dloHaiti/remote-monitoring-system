package com.dlohaiti.dlokiosk.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.dlohaiti.dlokiosk.Reading;

public class MeasurementRepository {

	private static final String FIELD_NAME_TIMESTAMP = "timestamp";
	private static final String FIELD_NAME_MEASUREMENTS = "measurements";
	private static final String DATE_FORMAT = "yyyy-mm-dd hh:MM:ss";
	private static String MEASUREMENTS_FILE = "dlokioski.dat";

	private Context context;

	public MeasurementRepository(Context context) {
		this.context = context;
	}

	public void add(List<Reading> readings) throws IOException {
		JSONObject data;
		try {
			data = formatData(readings, new Date());
		} catch (JSONException e) {
			throw new IOException("Error formating data.", e);
		}

		appendData(data);
	}

	private JSONObject formatData(List<Reading> readings, Date date) throws JSONException {
		JSONArray values = new JSONArray(readings);
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
