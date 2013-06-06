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
      for(int i = 6; i >= 0; i--) {
        days.add(new LocalDate().minusDays(i))
      }

      def tableData = buildTableData(days, products, receipts, deliveries)

      def chartData = buildChartData(products, days, receipts, deliveries)


      [chartData: chartData, tableData: tableData]
    }

  private buildChartData(List<Product> products, ArrayList days, List<Receipt> receipts, List<Delivery> deliveries) {
    def chartHeader = ['Date']
    for (product in products) {
      chartHeader.add(product.sku)
    }
    chartHeader.add('Delivery')
    def chartData = [chartHeader]
    for (LocalDate day in days) {
      def row = [day.toString('MM/dd/yy')]
      for (product in products) {
        def relevantReceipts = receipts.findAll({ r -> r.isOnDate(day) })
        def totalForSku = relevantReceipts.inject(0, { acc, val -> acc + val.totalGallonsForSku(product.sku) })
        row.add(totalForSku)
      }
      def positiveDeliveries = deliveries.findAll({d -> d.isOnDate(day) && d.isOutForDelivery() }).inject(0, { acc, val -> acc + val.quantity })
      def totalDeliveries = deliveries.findAll({d -> d.isOnDate(day) && d.isReturned() }).inject(positiveDeliveries, { acc, val -> acc - val.quantity })
      row.add(totalDeliveries)
      chartData.add(row)
    }
    return chartData
  }

  private buildTableData(ArrayList days, List<Product> products, List<Receipt> receipts, List<Delivery> deliveries) {
    def tableHeader = ['']
    for (day in days) {
      tableHeader.add(day.toString('MM/dd/yy'))
    }
    def tableData = [tableHeader]
    for (product in products) {
      def row = [product.sku]
      for (day in days) {
        def relevantReceipts = receipts.findAll({ r -> r.isOnDate(day) })
        def totalForSku = relevantReceipts.inject(0, { acc, val -> acc + val.totalGallonsForSku(product.sku) })
        row.add(totalForSku)
      }
      tableData.add(row)
    }
    def deliveryRow = ['Delivery']
    for (day in days) {
      def positiveDeliveries = deliveries.findAll({ d -> d.isOnDate(day) && d.isOutForDelivery() })
      def positiveDeliveryCount = positiveDeliveries.inject(0, { acc, val -> acc + val.quantity })
      def negativeDeliveries = deliveries.findAll({ d -> d.isOnDate(day) && d.isReturned() })
      def deliveryTotal = negativeDeliveries.inject(positiveDeliveryCount, { acc, val -> acc - val.quantity })
      deliveryRow.add(deliveryTotal)
    }
    tableData.add(deliveryRow)
    return tableData
  }
}
