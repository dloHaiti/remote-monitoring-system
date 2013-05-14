package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Kiosk {

    String name

    static constraints = {
        name(blank: false, unique: true)
    }


    @Override
    public String toString() {
        name
    }
}
