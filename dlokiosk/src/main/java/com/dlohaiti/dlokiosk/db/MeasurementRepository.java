package com.dlohaiti.dlokiosk.db;

import android.content.Context;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.Reading;
import com.dlohaiti.dlokiosk.domain.validation.MeasurementsValidator;
import com.dlohaiti.dlokiosk.domain.validation.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static java.util.Collections.emptyList;

public class MeasurementRepository {
    private static final String MEASUREMENTS_FILE = "dlokiosk.dat";
    private static final Logger logger = LoggerFactory.getLogger(MeasurementRepository.class);

    private MeasurementsValidator validator;
    private ObjectMapper mapper;
    private Context context;

    @Inject
    public MeasurementRepository(Context context, MeasurementsValidator validator, ObjectMapper mapper, KioskDate kioskDate) {
        this.context = context;
        this.validator = validator;
        this.mapper = mapper;
        this.mapper.setDateFormat(kioskDate.getFormat());
    }

    public Collection<Reading> getReadings() {
        FileInputStream inputStream = null;

        try {
            try {
                Collection<Reading> readings = new ArrayList<Reading>();
                inputStream = context.openFileInput(MEASUREMENTS_FILE);

                Iterator<Reading> it = mapper.reader(Reading.class).readValues(inputStream);
                while (it.hasNext()) {
                    readings.add(it.next());
                }

                return readings;
            } catch (IOException e) {
                logger.warn("Error while reading " + MEASUREMENTS_FILE, e);
                return emptyList();
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public SaveResult add(Reading reading) {
        ValidationResult result = validator.validate(reading.getMeasurements());
        if (!result.passed()) {
            return new SaveResult(result);
        }
        boolean saveSuccessful = true;

        try {
            appendData(mapper.writeValueAsString(reading));
        } catch (IOException e) {
            logger.error("Error while formatting json data", e);
            saveSuccessful = false;
        }
        return new SaveResult(result, saveSuccessful);
    }

    public SaveResult add(List<Measurement> measurements, Date timestamp) {
        Reading reading = new Reading();
        reading.setMeasurements(measurements);
        reading.setTimestamp(timestamp);

        return add(reading);
    }

    private void appendData(String reading) throws IOException {
        FileOutputStream outputStream;

        outputStream = context.openFileOutput(MEASUREMENTS_FILE, Context.MODE_APPEND);
        try {
            outputStream.write(reading.getBytes());
            outputStream.write('\n');
        } finally {
            outputStream.close();
        }
    }

    public boolean purge() {
        return context.deleteFile(MEASUREMENTS_FILE);
    }
}
