package com.dlohaiti.dloserver

class Region {
    String name
    String description

    static belongsTo = [country: Country]

    static constraints = {
        name(blank: false, unique: true)
        description( nullable: true)
    }
}
