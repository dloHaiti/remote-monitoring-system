import com.dlohaiti.dloserver.*

import grails.util.Holders

def config = Holders.config

beans = {
    localeResolver(org.springframework.web.servlet.i18n.SessionLocaleResolver) {
        defaultLocale = new Locale(config.dloserver.locale.language.toString(),config.dloserver.locale.country.toString())
        java.util.Locale.setDefault(defaultLocale)
    }
}
