package com.dlohaiti.dlokiosknew;

import android.text.InputFilter;
import android.text.Spanned;
import com.dlohaiti.dlokiosknew.domain.Money;

import java.math.BigDecimal;

public class RangeFilter implements InputFilter {
    private Money minimum;
    private Money maximum;

    public RangeFilter(Money minimum, Money maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned destination,
                               int destinationStart, int destinationEnd) {
        try {
            Money input = new Money(new BigDecimal(destination.toString() + source.toString()));
            return input.isInRange(minimum, maximum) ? null : "";

        } catch (NumberFormatException exception) {
            return "";
        }
    }
}
