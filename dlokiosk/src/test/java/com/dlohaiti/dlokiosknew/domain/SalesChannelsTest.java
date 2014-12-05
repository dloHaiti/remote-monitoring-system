package com.dlohaiti.dlokiosknew.domain;

import com.dlohaiti.dlokiosknew.exception.NoSalesChannelWithGivenIdException;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class SalesChannelsTest {

    private SalesChannelBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new SalesChannelBuilder();
    }

    @Test
    public void shouldFindSalesChannelById() throws Exception {
        SalesChannel channel1 = builder.build();
        SalesChannel channel2 = builder
                .withId(2)
                .withName("SalesChannel2")
                .withDescription("Sales Channel 2")
                .build();
        SalesChannels salesChannels = new SalesChannels(asList(channel1, channel2));

        assertEquals(channel1, salesChannels.findSalesChannelById(channel1.getId()));
        assertEquals(channel2, salesChannels.findSalesChannelById(channel2.getId()));
    }

    @Test(expected = NoSalesChannelWithGivenIdException.class)
    public void shouldThrowExceptionWhenNoSalesChannelIsPresentWithGivenId() throws Exception {
        SalesChannel channel1 = builder.build();
        SalesChannels salesChannels = new SalesChannels(asList(channel1));

        assertEquals(channel1, salesChannels.findSalesChannelById(3));
    }
}