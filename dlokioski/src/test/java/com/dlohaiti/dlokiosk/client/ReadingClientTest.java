package com.dlohaiti.dlokiosk.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoggerFactory.class)
public class ReadingClientTest {
    private ReadingClient readingClient;
    private RestClient restClient;

    @Before
    public void setup() {
        mockStatic(LoggerFactory.class);
        Logger logger = mock(Logger.class);
        when(LoggerFactory.getLogger(any(Class.class))).thenReturn(logger);

        restClient = mock(RestClient.class);
        readingClient = new ReadingClient(restClient);
    }

    @Test
    public void shouldIgnoreEmptyReadingList() {
//        readingClient
    }
}
