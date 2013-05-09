package com.dlohaiti.dloserver

class Sensor {

    String sensorId
    MeasurementType measurementType
    String displayName
    Location location

    static belongsTo = [kiosk: Kiosk]

    static constraints = {
        kiosk(nullable: false)
        sensorId(blank: false, unique: true)
        measurementType(nullable: false)
        location(nullable: false)
        displayName(blank: false)
    }
}
