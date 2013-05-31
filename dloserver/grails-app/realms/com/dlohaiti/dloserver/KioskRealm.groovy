package com.dlohaiti.dloserver

import org.apache.shiro.authc.*

class KioskRealm {
    static authTokenClass = org.apache.shiro.authc.UsernamePasswordToken

    def authenticate(UsernamePasswordToken authToken) {

        log.info "Attempting to authenticate ${authToken.username} in Kiosk realm..."
        def kioskName = authToken.username

        // Null kioskName is invalid
        if (kioskName == null) {
            throw new AccountException('Null kioskNames are not allowed by this realm.')
        }

        // Get the kiosk with the given kioskName. If the kiosk is not
        // found, then they don't have an account and we throw an
        // exception.
        def kiosk = Kiosk.findByName(kioskName)
        if (!kiosk) {
            throw new UnknownAccountException("No account found for kiosk [${kioskName}]")
        }

        log.info "Found kiosk '${kiosk.name}' in DB"

        if (kiosk.apiKey != (authToken.password as String)) {
          print "PASSWORD=<${authToken.password}>"
          print "APIKEY=<${kiosk.apiKey}>"
            log.info 'Invalid password (Kiosk realm)'
            throw new IncorrectCredentialsException("Invalid password for kiosk '${kioskName}'")
        }

        def account = new SimpleAccount(kioskName, kiosk.apiKey, "KioskRealm")

        return account
    }

}
