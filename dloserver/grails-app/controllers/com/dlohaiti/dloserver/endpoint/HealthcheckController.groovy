package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.Reading
import grails.converters.JSON
import grails.util.Environment
import groovy.sql.Sql

class HealthcheckController {

    def dataSource

    def index() {
        def result = [
                db: isDatabaseConnectionAvailable()
        ]
        render(result as JSON)
    }

    private boolean isDatabaseConnectionAvailable() {
        try {
            Reading.count()
            log.debug("Checking database connectivity: OK")
            return true
        } catch (Exception e) {
            log.error("Error detected during health check", e)
            return false
        }
    }

    // TODO Remove!! It' here just for manual tests
    def shutdown() {
        if (Environment.currentEnvironment != Environment.PRODUCTION) {
            try {
                def db = new Sql(dataSource)
                db.execute("shutdown")
            } catch (e) {
            }
        }
        render !isDatabaseConnectionAvailable()
    }
}
