package com.dlohaiti.dloserver

class Sensor {

    String sensorId
    MeasurementType measurementType
    String displayName

    static belongsTo = [kiosk: Kiosk]

    static constraints = {
        sensorId(blank: false, unique: true)
        measurementType(nullable: false)
        displayName(blank: false)
    }
}
