package com.dlohaiti.dloserver

class Measurement {
    Parameter parameter
    BigDecimal value

    static belongsTo = [reading: Reading]

    static constraints = {
        parameter(nullable: false)
        value(nullable: false, validator: {
            val, obj ->
                ((obj.parameter?.min == null) || (val >= obj.parameter?.min)) &&
                        ((obj.parameter?.max == null) || (val <= obj.parameter?.max))
        })
    }
}

