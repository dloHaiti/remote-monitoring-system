package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode(includes = ['name'])
class Country {

    String name


    static constraints = {
        name(blank: false, unique: true)
    }


}
