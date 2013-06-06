package com.dlohaiti.dloserver

import org.joda.time.LocalDate

class ReportController {

    def index() {
        [reports: Report.all]
    }

    def volumeByDay() {
      Kiosk kiosk = Kiosk.findByName(params.kioskName)
      Date oneWeekAgoMidnight = new LocalDate().minusWeeks(1).toDateMidnight().toDate()
      List<Delivery> deliveries = Delivery.findAllByKioskAndTimestampGreaterThanEquals(kiosk, oneWeekAgoMidnight)
      List<Receipt> receipts = Receipt.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, oneWeekAgoMidnight)
      List<Product> products = Product.findAll()


      def days = []
      for(int i = 7; i >= 0; i--) {
        days.add(new LocalDate().minusDays(i))
      }

      println days

      def rows = []
      for(Product product in products) {
        println "==============="
        println product.sku
        def row = []
        row.add(product.sku)
        for(LocalDate day in days) {
          println "----------------"
          println day
          println receipts
          def relevantReceipts = receipts.findAll { r -> r.isOnDate(day) }
          println relevantReceipts
          def dayTotal = relevantReceipts.inject 0, { acc, val -> val.totalGallons + acc }

          row.add(dayTotal)

        }
        rows.add(row)
      }
      println rows

      [days: days.collect { d -> d.toDateMidnight().toDate()}, rows: rows]
    }
}
