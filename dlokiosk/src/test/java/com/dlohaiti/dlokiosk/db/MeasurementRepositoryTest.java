package com.dlohaiti.dlokiosk.db;

import android.content.Context;
import com.dlohaiti.dlokiosk.AbstractUnitTest;
import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.MeasurementLocation;
import com.dlohaiti.dlokiosk.domain.MeasurementType;
import com.dlohaiti.dlokiosk.domain.Reading;
import com.dlohaiti.dlokiosk.domain.validation.MeasurementsValidator;
import com.dlohaiti.dlokiosk.domain.validation.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


public class MeasurementRepositoryTest extends AbstractUnitTest {

    private MeasurementRepository repository;
    private Context context;
    private MeasurementsValidator validator;
    private FileOutputStream outputStream;
    private FileInputStream inputStream;

    @Before
    public void setup() throws FileNotFoundException {
        context = mock(Context.class);
        validator = mock(MeasurementsValidator.class);
        outputStream = mock(FileOutputStream.class);
        inputStream = mock(FileInputStream.class);
        given(context.openFileOutput(anyString(), anyInt())).willReturn(outputStream);
        given(context.openFileInput(anyString())).willReturn(inputStream);
        given(validator.validate(anyList())).willReturn(new ValidationResult(new HashSet<MeasurementType>()));
        repository = new MeasurementRepository(context, validator, new ObjectMapper());
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z").format(date);
    }

    @Test
    public void shouldWriteToLogFile() throws IOException {
        Date now = new Date();

        Measurement measurement = new Measurement(MeasurementType.PH, "5", MeasurementLocation.BOREHOLE);
        repository.add(Arrays.asList(measurement), now);

        String expected = "{\"kiosk\":\"HARDCODED K1\",\"timestamp\":\"" + formatDate(now) + "\",\"measurements\":[{\"parameter\":\"PH\",\"value\":\"5\",\"location\":\"BOREHOLE\"}]}";

        verify(outputStream).write(expected.getBytes());
        verify(outputStream).write('\n');
    }

    @Test
    public void shouldCloseLogFileEvenOnExceptions() throws IOException {
        Measurement measurement = new Measurement(MeasurementType.PH, "5", MeasurementLocation.BOREHOLE);

        doThrow(new IOException()).when(outputStream).write(Matchers.<byte[]>any());

        repository.add(Arrays.asList(measurement), new Date());

        verify(outputStream).close();
    }

    @Test
    public void shouldRunValidationsBeforeSaving() {
        List<Measurement> measurements = Arrays.asList(new Measurement(MeasurementType.TASTE, "OK", MeasurementLocation.WTU_EFF));
        repository.add(measurements, new Date());
        verify(validator).validate(measurements);
    }

    @Test
    public void shouldReturnValidationFailedSaveResultWhenValidationFails() {
        List<Measurement> measurements = Arrays.asList(new Measurement(MeasurementType.TDS, "15000", MeasurementLocation.BOREHOLE));
        Set<MeasurementType> invalid = new HashSet<MeasurementType>();
        invalid.add(MeasurementType.TDS);
        given(validator.validate(measurements)).willReturn(new ValidationResult(invalid));

        SaveResult saveResult = repository.add(measurements, new Date());

        assertThat(saveResult.passedValidation(), is(false));
        assertThat(saveResult.getValidationFailures(), is(invalid));
    }

    @Test
    public void shouldReturnValidationSuccessfulWhenPasses() {
        List<Measurement> measurements = Arrays.asList(new Measurement(MeasurementType.TDS, "150", MeasurementLocation.BOREHOLE));
        SaveResult saveResult = repository.add(measurements, new Date());
        assertThat(saveResult.passedValidation(), is(true));
    }

    @Test
    public void shouldReturnEmptyStringWhenLogFileIsMissing() throws IOException {
        given(context.openFileInput(anyString())).willThrow(new FileNotFoundException());

        Collection<Reading> result = repository.getReadings();

        assertThat(result.size(), is(0));
    }


    @Test
    public void shouldRemoveLogFileOnPurgeCall() throws IOException {
        given(context.deleteFile(anyString())).willReturn(true);

        assertThat(repository.purge(), is(true));
    }


}
