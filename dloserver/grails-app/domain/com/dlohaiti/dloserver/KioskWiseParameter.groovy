package com.dlohaiti.dloserver

class KioskWiseParameter {
    Kiosk kiosk
    SamplingSite samplingSite
    Parameter parameter

    static constraints = {
        id (composite: ['kiosk','samplingSite','parameter'])
        parameter(unique: ['kiosk', 'samplingSite'])
    }

    static mapping = {
        version false
    }
    def getActiveAndManualParameter(){
        if(parameter.manual && parameter.active){
            parameter.samplingSites = kiosk.getSamplingSites(parameter)
            return parameter
        }else{
            return null
        }
    }

}

