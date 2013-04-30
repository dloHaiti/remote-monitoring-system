package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


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

        assertThat(readingClient.send("line1\n"), is(1));
    }

    @Test
    public void shouldSendMultipleReading() {
        when(restClient.post(anyString(), anyString())).thenReturn(true);

        assertThat(readingClient.send("line1\nline2\n"), is(2));

        verify(restClient, times(2)).post(anyString(), anyString());
    }

    @Test
    public void shouldAbortOnRestError() {
        when(restClient.post(anyString(), anyString())).thenReturn(true);
        when(restClient.post(anyString(), eq("line2"))).thenReturn(false);

        assertThat(readingClient.send("line1\nline2\nline3\n"), is(0));

        verify(restClient, times(2)).post(anyString(), anyString());
    }
}
