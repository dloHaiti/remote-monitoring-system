package com.dlohaiti.dloserver

import org.apache.shiro.SecurityUtils

class KioskFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
              if (SecurityUtils.subject.authenticated) {
                request.kioskName = SecurityUtils.subject.principal
              }
            }
        }
    }
}
