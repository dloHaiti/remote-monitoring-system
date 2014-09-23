package com.dlohaiti.dlokiosk.domain;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.*;

public class PaymentModes extends ArrayList<String> {

    public static final String SEPARATOR = ",";

    public PaymentModes(Collection<String> paymentModes) {
        super(paymentModes);
    }

    public PaymentModes(String concatenatedString) {
        super();
        for (String type : asList(split(concatenatedString, SEPARATOR))) {
            add(capitalize(type));
        }
    }

    public String concatenatedString() {
        return join(this, SEPARATOR);
    }
}
