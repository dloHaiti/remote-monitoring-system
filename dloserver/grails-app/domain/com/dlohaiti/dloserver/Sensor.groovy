package com.dlohaiti.dloserver

class Sensor {

    String sensorId
    String displayName

    static belongsTo = [kiosk: Kiosk, parameter: Parameter, samplingSite: SamplingSite]

    static constraints = {
        sensorId(blank: false, unique: true)
        displayName(blank: false)
    }

    public String toString() {
        "$sensorId: $displayName"
    }
}
