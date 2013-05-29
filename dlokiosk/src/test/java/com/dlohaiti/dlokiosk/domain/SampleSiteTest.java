package com.dlohaiti.dlokiosk.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SampleSiteTest {
    @Test
    public void shouldHaveNaturalSortOrderOfAlphabeticalByName() {
        SampleSite a = new SampleSite("a");
        SampleSite b = new SampleSite("b");
        List<SampleSite> sites = Arrays.asList(b, a);

        int comparisonResult = a.compareTo(b);
        Collections.sort(sites);

        assertThat(comparisonResult, is(-1));
        assertThat(sites.get(0), is(a));
    }
}
