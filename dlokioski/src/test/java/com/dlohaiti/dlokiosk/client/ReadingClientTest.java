package com.dlohaiti.dlokiosk.client;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReadingClientTest {
    ReadingClient readingClient;
    RestClient restClient;

    @Before
    public void setup() {
        restClient = mock(RestClient.class);
        readingClient = new ReadingClient(restClient);
    }

    @Test
    public void shouldReturnTrueWhenHasConnectionToServer() {
        HealthcheckStatus okStatus = getHealthcheckStatus(true);
        when(restClient.get("/healthcheck", HealthcheckStatus.class)).thenReturn(okStatus);

        assertThat(readingClient.getServerStatus(), is(true));
    }

    @Test
    public void shouldReturnFalseWhenHasNoConnectionToServer() {
        HealthcheckStatus failStatus = getHealthcheckStatus(false);
        when(restClient.get("/healthcheck", HealthcheckStatus.class)).thenReturn(failStatus);

        assertThat(readingClient.getServerStatus(), is(false));
    }

    private HealthcheckStatus getHealthcheckStatus(boolean dbStatus) {
        HealthcheckStatus okStatus = new HealthcheckStatus();
        okStatus.db = dbStatus;
        return okStatus;
    }
}
