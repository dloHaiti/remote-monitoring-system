package com.dlohaiti.dlokiosk.domain;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ParameterTest {

    @Test
    public void shouldHaveNaturalAlphabeticalSortOrder() {
        Parameter a = new Parameter("a", "", "", "");
        Parameter b = new Parameter("b", "", "", "");

        assertThat(a.compareTo(b), is(-1));
    }

    @Test
    public void shouldHaveNaturalAlphabeticalSortOrderIgnoringCase() {
        Parameter a = new Parameter("a", "", "", "");
        Parameter b = new Parameter("B", "", "", "");

        assertThat(a.compareTo(b), is(-1));
    }

    @Test
    public void shouldHandleNullParameterNamesGracefully() {
        Parameter a = new Parameter("a", "", "", "");
        Parameter n = new Parameter(null, "", "", "");

        assertThat(a.compareTo(n), is(1));
        assertThat(n.compareTo(a), is(-1));
    }

}
