package com.dlohaiti.dloserver

class Operator extends User{

    String contactDetails=""

    static constraints = {
        contactDetails blank:true,  nullable: true
    }
}
