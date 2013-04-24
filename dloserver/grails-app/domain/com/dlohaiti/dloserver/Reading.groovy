package com.dlohaiti.dloserver

class Reading {
    Date timestamp

    static hasMany = [measurements: Measurement]
}
