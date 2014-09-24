package com.dlohaiti.dlokiosk.client;

import java.util.List;

public class ConfigurationJson {
    private String unitOfMeasure;
    private String currency;
    private String locale;
    private String dateformat;
    private List<String> paymentModes;
    private List<String> paymentTypes;

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDateformat() {
        return dateformat;
    }

    public void setDateformat(String dateformat) {
        this.dateformat = dateformat;
    }

    public List<String> getPaymentModes() {
        return paymentModes;
    }

    public void setPaymentModes(List<String> paymentModes) {
        this.paymentModes = paymentModes;
    }

    public List<String> getPaymentTypes() {
        return paymentTypes;
    }

    public void setPaymentTypes(List<String> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }
}
