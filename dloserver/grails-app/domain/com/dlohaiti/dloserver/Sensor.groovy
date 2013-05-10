package com.dlohaiti.dloserver

class Sensor {

    String sensorId
    MeasurementType measurementType
    String displayName
    Location location

    static belongsTo = [kiosk: Kiosk]

    static constraints = {
        sensorId(blank: false, unique: true)
        displayName(blank: false)
        kiosk(nullable: false)
        measurementType(nullable: false)
        location(nullable: false)
    }

    public String toString() {
        "$sensorId: $displayName"
    }
}
