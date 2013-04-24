package com.dlohaiti.dloserver

import org.apache.commons.lang.builder.ToStringBuilder

class Measurement {
    MeasurementType parameter
    MeasurementLocation location
    String value

    static belongsTo = Reading
}

