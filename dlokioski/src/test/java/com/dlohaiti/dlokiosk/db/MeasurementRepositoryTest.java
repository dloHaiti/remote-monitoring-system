package com.dlohaiti.dlokiosk.db;

import android.content.Context;
import com.dlohaiti.dlokiosk.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


public class MeasurementRepositoryTest {

    private MeasurementRepository repository;
    private MeasurementsValidator validator;
    private Context context;

    @Before
    public void setup() {
        context = mock(Context.class);
        validator = new MeasurementsValidator(new MeasurementValidator());
        repository = new MeasurementRepository(context, validator);
    }

    @Test
    public void shouldWriteToLogFile() throws IOException {
        FileOutputStream outputStream = mock(FileOutputStream.class);
        when(context.openFileOutput(anyString(), anyInt())).thenReturn(outputStream);

        Measurement measurement = new Measurement(MeasurementType.PH, "5", MeasurementLocation.BOREHOLE);
        repository.add(Arrays.asList(measurement));

        verify(outputStream).write(Matchers.<byte[]>anyObject());
    }


}
