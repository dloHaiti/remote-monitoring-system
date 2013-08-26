package com.dlohaiti.dlokiosk.domain;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ParameterTest {

    private static Parameter buildParameter() {
        return new Parameter("a", "", "", "", false, 1);
    }

    @Test
    public void shouldConsiderValidPositiveNumberWithoutRange() {
        Parameter p = buildParameter();
        boolean invalid = p.considersInvalid("9.0");
        assertThat(invalid, is(false));
    }

    @Test
    public void shouldConsiderInvalidBlankValueWithoutRange() {
        Parameter p = buildParameter();
        boolean invalid = p.considersInvalid("");
        assertThat(invalid, is(true));
    }

    @Test
    public void shouldConsiderInvalidNegativeValueWithoutRange() {
        Parameter p = buildParameter();
        boolean invalid = p.considersInvalid("-5");
        assertThat(invalid, is(true));
    }

    @Test
    public void shouldConsiderInvalidNonNumericValueWithoutRange() {
        Parameter p = buildParameter();
        boolean invalid = p.considersInvalid("r");
        assertThat(invalid, is(true));
    }

    @Test
    public void shouldConsiderInvalidNumberBelowMinimumWithRange() {
        Parameter p = new Parameter("a", "", "5.0", "10.0", false, 1);
        boolean invalid = p.considersInvalid("4.5");
        assertThat(invalid, is(true));
    }

    @Test
    public void shouldConsiderInvalidNumberAboveMaximumWithRange() {
        Parameter p = new Parameter("a", "", "5.0", "10.0", false, 1);
        boolean invalid = p.considersInvalid("10.1");
        assertThat(invalid, is(true));
    }

    @Test
    public void shouldConsiderValidNumberAtMaximumWithRange() {
        Parameter p = new Parameter("a", "", "5.0", "10.0", false, 1);
        boolean invalid = p.considersInvalid("10.0");
        assertThat(invalid, is(false));
    }

    @Test
    public void shouldConsiderValidNumberAtMinimumWithRange() {
        Parameter p = new Parameter("a", "", "5.0", "10.0", false, 1);
        boolean invalid = p.considersInvalid("5.0");
        assertThat(invalid, is(false));
    }

    @Test
    public void shouldConsiderValidNumberInMiddleOfRangeWithRange() {
        Parameter p = new Parameter("a", "", "5.0", "10.0", false, 1);
        boolean invalid = p.considersInvalid("7.5");
        assertThat(invalid, is(false));
    }

    @Test
    public void shouldHaveNaturalSortOrderByPriority() {
        Parameter a = new Parameter("a", "", "", "", false, 100);
        Parameter b = new Parameter("b", "", "", "", false, 1);

        assertThat(a.compareTo(b), is(1));
    }

    @Test
    public void shouldSortAnyPriorityAheadOfNullPriority() {
        Parameter a = new Parameter("a", "", "", "", false, null);
        Parameter b = new Parameter("a", "", "", "", false, 1);

        assertThat(a.compareTo(b), is(1));
    }

    @Test
    public void shouldHaveNaturalAlphabeticalSortOrderWhenSamePriority() {
        Parameter a = new Parameter("a", "", "", "", false, 1);
        Parameter b = new Parameter("b", "", "", "", false, 1);

        assertThat(a.compareTo(b), is(-1));
    }

    @Test
    public void shouldHaveNaturalAlphabeticalSortOrderIgnoringCaseWhenSamePriority() {
        Parameter a = new Parameter("a", "", "", "", false, 1);
        Parameter b = new Parameter("B", "", "", "", false, 1);

        assertThat(a.compareTo(b), is(-1));
    }

    @Test
    public void shouldHandleNullParameterNamesGracefully() {
        Parameter a = new Parameter("a", "", "", "", false, 1);
        Parameter n = new Parameter(null, "", "", "", false, 1);

        assertThat(a.compareTo(n), is(1));
        assertThat(n.compareTo(a), is(-1));
    }

}
