package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Kiosk {

    String name
    String apiKey

    static constraints = {
        name(blank: false, unique: true)
        apiKey(blank: false)
    }


    @Override
    public String toString() {
        name
    }
}
