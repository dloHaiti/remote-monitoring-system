package com.dlohaiti.dlokiosk.domain;

import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class CustomerAccountsTest {

    private CustomerAccountBuilder builder;
    private SalesChannelBuilder channelBuilder;

    @Before
    public void setUp() throws Exception {
        builder = new CustomerAccountBuilder();
        channelBuilder = new SalesChannelBuilder();
    }

    @Test
    public void shouldFindAccountsThatCanBeServedBySalesChannel() throws Exception {
        SalesChannel channel1 = channelBuilder.build();
        SalesChannel channel2 = channelBuilder
                .withId(2)
                .withName("SalesChannel2")
                .withDescription("Sales Channel 2")
                .build();
        CustomerAccount customer1 = builder
                .withChannels(asList(channel1)).build();
        CustomerAccount customer2 = builder
                .withChannels(asList(channel2)).build();
        CustomerAccounts customerAccounts = new CustomerAccounts(asList(customer1, customer2));

        assertEquals(asList(customer1), customerAccounts.findAccountsThatCanBeServedByChannel(channel1.id()));
        assertEquals(asList(customer2), customerAccounts.findAccountsThatCanBeServedByChannel(channel2.id()));
    }

    @Test
    public void shouldUnselectAllCustomerAccounts() throws Exception {
        CustomerAccount selectedCustomerAccount = builder.select().build();
        CustomerAccount unselectedCustomerAccount = builder.build();
        CustomerAccounts customerAccounts = new CustomerAccounts(asList(selectedCustomerAccount, unselectedCustomerAccount));

        customerAccounts.unSelectAll();

        assertFalse(selectedCustomerAccount.isSelected());
        assertFalse(unselectedCustomerAccount.isSelected());
    }
}