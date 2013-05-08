package com.dlohaiti.dloserver

class Kioski {

    String name

    static constraints = {
        name(blank: false, unique: true)
    }
}
