package com.dlohaiti.dlokiosk.domain;

import java.math.BigDecimal;
import java.util.Currency;

public class Money {
    private final BigDecimal amount;
    // Money without currency feels wrong, but this app is only used with HTG
    private final Currency currency = Currency.getInstance("HTG");

    public Money(BigDecimal amount) {
        this.amount = amount.setScale(2);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currency.getCurrencyCode();
    }

    public Money times(int quantity) {
        return new Money(amount.multiply(new BigDecimal(quantity)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        if (amount != null ? !amount.equals(money.amount) : money.amount != null) return false;
        if (!currency.equals(money.currency)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = amount != null ? amount.hashCode() : 0;
        result = 31 * result + currency.hashCode();
        return result;
    }

    @Override public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", currency=" + currency +
                '}';
    }

    public Money minus(BigDecimal amount) {
        return new Money(this.amount.subtract(amount));
    }
}
