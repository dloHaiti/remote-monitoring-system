package com.dlohaiti.dlokiosk;

import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FailuresTest {

    private Failures failures;

    @Before
    public void setUp() {
        failures = new Failures();
    }

    @Test
    public void shouldKnowIfNotEmpty() {
        assertThat(failures.isNotEmpty(), is(false));

        failures.add(new Failure(FailureKind.DELIVERY, asList("SERVER_ERROR")));

        assertThat(failures.isNotEmpty(), is(true));
    }

    @Test
    public void shouldKnowCountOfFailuresForGivenFailureKind() {
        failures.add(new Failure(FailureKind.DELIVERY, asList("MISSING_DELIVERY_AGENT")));
        failures.add(new Failure(FailureKind.RECEIPT, asList("CREATEDDATE_NULLABLE", "PRODUCT_MISSING")));
        failures.add(new Failure(FailureKind.RECEIPT, asList("PRODUCT_MISSING")));

        assertThat(failures.countFor(FailureKind.DELIVERY), is(1));
        assertThat(failures.countFor(FailureKind.RECEIPT), is(2));
        assertThat(failures.countFor(FailureKind.READING), is(0));
    }
}
