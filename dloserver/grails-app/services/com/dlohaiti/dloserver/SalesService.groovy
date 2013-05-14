package com.dlohaiti.dloserver

class SalesService {

    def grailsApplication

    public saveSale(params) {
        Date timestamp = params.date("timestamp", grailsApplication.config.dloserver.measurement.timeformat.toString())
        Sale sale = Sale.findByTimestamp(timestamp)
        if (sale) {
            //TODO: return a 409 conflict and log a message instead of deleting
        }
        sale = new Sale(timestamp: timestamp, sku: params.sku, quantity: params.quantity)
        sale.kiosk = Kiosk.findByName(params.kiosk)
        sale.save(flush: true)
        return sale
    }

}
