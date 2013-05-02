package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.AbstractUnitTest;
import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.MeasurementLocation;
import com.dlohaiti.dlokiosk.domain.MeasurementType;
import com.dlohaiti.dlokiosk.domain.Reading;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ReadingClientTest extends AbstractUnitTest {
    private ReadingClient readingClient;
    private RestClient restClient;

    @Before
    public void setup() {
        restClient = mock(RestClient.class);
        readingClient = new ReadingClient(restClient);
    }

    @Test
    public void shouldSendOneReading() {
        when(restClient.post(anyString(), anyString())).thenReturn(true);

        assertThat(readingClient.send(aReading()), is(true));
    }

    @Test
    public void shouldAbortOnRestError() {
        when(restClient.post(anyString(), anyString())).thenReturn(false);

        assertThat(readingClient.send(aReading()), is(false));
    }

    private Reading aReading() {
        Measurement measurement = new Measurement(MeasurementType.PH, "5", MeasurementLocation.BOREHOLE);
        Reading reading = new Reading();
        reading.setMeasurements(asList(measurement));
        reading.setTimestamp(new Date());

        return reading;
    }
}
