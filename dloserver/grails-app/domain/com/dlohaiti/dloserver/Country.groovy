package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode(includes = ['name','unitOfMeasure'])
class Country {

    String name
    String unitOfMeasure

    static constraints = {
        name(blank: false, unique: true)
        unitOfMeasure(blank: false)
    }
}
