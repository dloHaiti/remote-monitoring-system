package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;


public class ReadingClientTest extends AbstractUnitTest {
    private ReadingClient readingClient;
    private RestClient restClient;

    @Before
    public void setup() {
        restClient = mock(RestClient.class);
        readingClient = new ReadingClient(restClient);
    }

    @Test
    public void shouldIgnoreEmptyReadingList() {
//        readingClient
    }
}
