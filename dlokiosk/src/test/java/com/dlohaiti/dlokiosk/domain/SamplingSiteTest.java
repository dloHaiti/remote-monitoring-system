package com.dlohaiti.dlokiosk.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SamplingSiteTest {
    @Test
    public void shouldHaveNaturalSortOrderOfAlphabeticalByName() {
        SamplingSite a = new SamplingSite("a");
        SamplingSite b = new SamplingSite("b");

        assertThat(a.compareTo(b), is(-1));
    }

    @Test
    public void shouldHaveNaturalAlphabeticalSortOrderIgnoringCase() {
        SamplingSite a = new SamplingSite("a");
        SamplingSite b = new SamplingSite("B");

        assertThat(a.compareTo(b), is(-1));
    }

    @Test
    public void shouldHandleNullsGracefullyWhenSorting() {
        SamplingSite a = new SamplingSite("a");
        SamplingSite n = new SamplingSite(null);

        assertThat(a.compareTo(n), is(1));
        assertThat(n.compareTo(a), is(-1));
    }
}
