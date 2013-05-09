package com.dlohaiti.dloserver

class Reading {
    Date timestamp
    Kiosk kiosk
    String username

    static hasMany = [measurements: Measurement]

    static belongsTo = [kiosk: Kiosk]

    static constraints = {
        username(nullable: true, blank: true)
    }
}
