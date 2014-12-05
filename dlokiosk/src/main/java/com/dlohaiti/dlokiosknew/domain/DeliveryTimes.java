package com.dlohaiti.dlokiosknew.domain;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.*;

public class DeliveryTimes extends ArrayList<String> {

    public static final String SEPARATOR = ",";

    public DeliveryTimes(Collection<String> deliveryTimes) {
        super(deliveryTimes);
    }

    public DeliveryTimes(String concatenatedString) {
        super();
        for (String type : asList(split(concatenatedString, SEPARATOR))) {
            add(capitalize(type));
        }
    }

    public String concatenatedString() {
        return join(this, SEPARATOR);
    }
}
