package com.dlohaiti.dloserver

import org.joda.time.LocalDate

class ReportController {

    def index() {
        [kioskName: request.kioskName]
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

      def daysAsDates = days.collect { d -> d.toDateMidnight().toDate()}

      def rows = []
      for(Product product in products) {
        def row = []
        row.add(product.sku)
        for(LocalDate day in days) {
          def relevantReceipts = receipts.findAll { r -> r.isOnDate(day) }
          def dayTotal = relevantReceipts.inject 0, { acc, val -> val.totalGallons + acc }
          row.add(dayTotal)
        }
        rows.add(row)
      }

      def tableHeader = ['']
      for(day in days) {
        tableHeader.add(day.toString('MM/dd/yy'))
      }
      def tableData = [tableHeader]
      for(product in products) {
        def row = [product.sku]
        for(day in days) {
          def relevantReceipts = receipts.findAll({ r -> r.isOnDate(day) })
          def totalForSku = relevantReceipts.inject(0, { acc, val -> acc + val.totalGallonsForSku(product.sku)})
          row.add(totalForSku)
        }
        tableData.add(row)
      }

      def chartHeader = ['Date']
      for(product in products) {
        chartHeader.add(product.sku)
      }
      def chartData = [chartHeader]
      for(LocalDate day in days) {
        def row = [day.toString('MM/dd/yy')]
        for(product in products) {
          def relevantReceipts = receipts.findAll({ r -> r.isOnDate(day) })
          def totalForSku = relevantReceipts.inject(0, { acc, val -> acc + val.totalGallonsForSku(product.sku)})
          row.add(totalForSku)
        }
        chartData.add(row)
      }



      [days: daysAsDates, rows: rows, chartData: chartData, tableData: tableData]
    }
}
