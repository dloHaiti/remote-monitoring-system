package com.dlohaiti.dlokiosknew.client;

import java.math.BigDecimal;

public class MoneyJson {
    private BigDecimal amount;
    private String currencyCode;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
