package com.dlohaiti.dloserver

class User {
    String userName
    String password
    boolean isActive = true

    static constraints = {
        userName blank: false, unique: true
        password blank: false
    }
}
