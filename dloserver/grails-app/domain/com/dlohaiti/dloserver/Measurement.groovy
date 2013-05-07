package com.dlohaiti.dloserver

class Measurement {
    MeasurementType parameter
    Location location
    String value

    static belongsTo = [reading: Reading]

    static constraints = {
        parameter(nullable: false)
        location(nullable: false)
        value(blank: false)
    }
}

