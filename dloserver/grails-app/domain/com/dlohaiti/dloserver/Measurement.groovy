package com.dlohaiti.dloserver

class Measurement {
    MeasurementType parameter
    Location location
    BigDecimal value
    Date timestamp

    static belongsTo = [reading: Reading]

    static constraints = {
        parameter(nullable: false)
        location(nullable: false)
        timestamp(nullable: false)
        value(nullable: false, validator: {
            val, obj ->
                ((obj.parameter?.min == null) || (val >= obj.parameter?.min)) &&
                        ((obj.parameter?.max == null) || (val <= obj.parameter?.max))
        })
    }
}

