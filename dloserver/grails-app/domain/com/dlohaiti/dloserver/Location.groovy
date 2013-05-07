package com.dlohaiti.dloserver

class Location {
    String name

    static constraints = {
        name(blank: false, unique: true)
    }
}
