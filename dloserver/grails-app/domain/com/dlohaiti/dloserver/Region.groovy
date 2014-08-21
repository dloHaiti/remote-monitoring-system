package com.dlohaiti.dloserver

class Region {
    String name
    String description

    static belongsTo = [country: Country]
    static hasMany = [promotions: Promotion,kiosk: Kiosk,rebates: Rebate]

    static constraints = {
        name(blank: false, unique: true)
        description( nullable: true)
    }
}
