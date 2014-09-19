package com.dlohaiti.dlokiosk.domain;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.*;

public class PaymentTypes extends ArrayList<String> {

    public static final String SEPARATOR = ",";

    public PaymentTypes(Collection<String> paymentTypes) {
        super(paymentTypes);
    }

    public PaymentTypes(String concatenatedString) {
        super();
        for (String type : asList(split(concatenatedString, SEPARATOR))) {
            add(capitalize(type));
        }
    }

    public String concatenatedString() {
        return join(this, SEPARATOR);
    }
}
