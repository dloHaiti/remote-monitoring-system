package com.dlohaiti.dloserver

class Reading {
    Date dateCreated

    static hasMany = [measurements: Measurement]
}
