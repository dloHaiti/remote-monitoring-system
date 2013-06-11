package com.dlohaiti.dloserver

class Measurement {
    Parameter parameter
    BigDecimal value

    static belongsTo = [reading: Reading]

    static constraints = {
        parameter(nullable: false)
        value(nullable: false, validator: {
            val, obj ->
                ((obj.parameter?.minimum == null) || (val >= obj.parameter?.minimum)) &&
                        ((obj.parameter?.maximum == null) || (val <= obj.parameter?.maximum))
        })
    }
}

