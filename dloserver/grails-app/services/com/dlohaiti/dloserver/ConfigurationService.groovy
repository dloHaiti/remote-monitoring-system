package com.dlohaiti.dloserver

import org.springframework.web.servlet.support.RequestContextUtils

class ConfigurationService {
    def grailsApplication

    public String getUnitOfMeasure() {
        return grailsApplication.config.dloserver.measurement.unitOfMeasure.toString()
    }

    public String getLocale() {
        return sprintf("%s:%s",grailsApplication.config.dloserver.locale.language.toString(),grailsApplication.config.dloserver.locale.country.toString())
    }

    public String getCurrencyCode(){
        def locale  = new Locale(grailsApplication.config.dloserver.locale.language.toString(),grailsApplication.config.dloserver.locale.country.toString())
        return Currency.getInstance(locale).getCurrencyCode()
    }

    public String getDateFormat(){
        return grailsApplication.config.dloserver.measurement.timeformat.toString()
    }

}
