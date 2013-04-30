package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HealthcheckClientTest extends AbstractUnitTest {
    private HealthcheckClient healthcheckClient;
    private RestClient restClient;

    @Before
    public void setup() {
        restClient = mock(RestClient.class);
        healthcheckClient = new HealthcheckClient(restClient);
    }

    @Test
    public void shouldReturnTrueWhenHasConnectionToServer() {
        HealthcheckStatus okStatus = getHealthcheckStatus(true);
        when(restClient.get("/healthcheck", HealthcheckStatus.class)).thenReturn(okStatus);

        assertThat(healthcheckClient.getServerStatus(), is(true));
    }

    @Test
    public void shouldReturnFalseWhenHasNoConnectionToServer() {
        HealthcheckStatus failStatus = getHealthcheckStatus(false);
        when(restClient.get("/healthcheck", HealthcheckStatus.class)).thenReturn(failStatus);

        assertThat(healthcheckClient.getServerStatus(), is(false));
    }

    private HealthcheckStatus getHealthcheckStatus(boolean dbStatus) {
        HealthcheckStatus okStatus = new HealthcheckStatus();
        okStatus.setDb(dbStatus);
        return okStatus;
    }
}
