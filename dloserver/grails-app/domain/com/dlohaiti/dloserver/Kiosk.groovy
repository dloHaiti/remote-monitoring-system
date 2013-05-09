package com.dlohaiti.dloserver

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
