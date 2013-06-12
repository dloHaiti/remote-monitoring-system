package com.dlohaiti.dloserver

import org.joda.time.LocalDate

import java.math.RoundingMode

class ReportController {

  def index() {
    [kioskName: request.kioskName]
  }

  def readings() {

  }

  def salesByDay() {
    Kiosk kiosk = Kiosk.findByName(params.kioskName)
    List<Product> products = Product.findAll()
    List<Receipt> receipts = Receipt.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, oneWeekAgoMidnight())
    List<Delivery> deliveries = Delivery.findAllByKioskAndTimestampGreaterThanEquals(kiosk, oneWeekAgoMidnight())

    def tableHeader = ['']
    for(day in previousWeek()) {
      tableHeader.add(day.toString('MM/dd/yy'))
    }
    def tableData = [tableHeader]
    for(product in products) {
      def row = [product.sku]
      for(day in previousWeek()) {
        def dayReceipts = receipts.findAll({ r -> r.isOnDate(day) })
        def lineItemsWithSku = dayReceipts.receiptLineItems.flatten().findAll({ ReceiptLineItem item -> item.sku == product.sku })
        def skuTotal = lineItemsWithSku.inject(0, { BigDecimal acc, ReceiptLineItem val -> acc + val.price })
        row.add(skuTotal)
      }
      tableData.add(row)
    }

    def deliveriesRow = ['DELIVERY']
    for(day in previousWeek()) {
      def dayDeliveries = deliveries.findAll({ d -> d.isOnDate(day) })
      def positiveDeliveries = dayDeliveries.findAll({ Delivery d -> d.isOutForDelivery() }).inject(0, { acc, val -> acc + (val.quantity * val.price.amount) })
      def totalDeliveries = dayDeliveries.findAll({ Delivery d -> d.isReturned() }).inject(positiveDeliveries, { acc, val -> acc - (val.quantity * val.price.amount) })
      deliveriesRow.add(totalDeliveries)
    }
    tableData.add(deliveriesRow)

    def totalRow = ['TOTAL']
    previousWeek().eachWithIndex { LocalDate day, int i ->
      def total = receipts.findAll({ r -> r.isOnDate(day) }).inject(0, { acc, val -> acc + val.total })
      totalRow.add(total + deliveriesRow[i+1])
    }
    tableData.add(totalRow)

    [tableData: tableData, chartData: new TableToChart().convertWithoutRowsTitled(tableData, ['TOTAL'])]
  }

  def volumeByDay() {
    Kiosk kiosk = Kiosk.findByName(params.kioskName)
    List<Delivery> deliveries = Delivery.findAllByKioskAndTimestampGreaterThanEquals(kiosk, oneWeekAgoMidnight())
    List<Receipt> receipts = Receipt.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, oneWeekAgoMidnight())
    List<Reading> readings = Reading.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, oneWeekAgoMidnight())
    List<Product> products = Product.findAll()

    def tableData = buildTableData(previousWeek(), products, receipts, deliveries, readings)

    [chartData: new TableToChart().convertWithoutRowsTitled(tableData, ['TOTAL', 'DIFFERENCE %']), tableData: tableData, skusPresent: products.size()]
  }

  private Date oneWeekAgoMidnight() {
    return new LocalDate().minusWeeks(1).toDateMidnight().toDate()
  }

  private List<LocalDate> previousWeek() {
    def days = []
    for (int i = 6; i >= 0; i--) {
      days.add(new LocalDate().minusDays(i))
    }
    return days
  }

  private buildTableData(List<LocalDate> previousWeek, List<Product> products, List<Receipt> receipts, List<Delivery> deliveries, List<Reading> readings) {
    def tableHeader = ['']
    for (day in previousWeek) {
      tableHeader.add(day.toString('MM/dd/yy'))
    }
    def tableData = [tableHeader]
    for (product in products) {
      def row = [product.sku]
      for (day in previousWeek) {
        def relevantReceipts = receipts.findAll({ r -> r.isOnDate(day) })
        def totalForSku = relevantReceipts.inject(0, { acc, val -> acc + val.totalGallonsForSku(product.sku) })
        row.add(totalForSku)
      }
      tableData.add(row)
    }
    def deliveryRow = ['Delivery']
    for (day in previousWeek) {
      def positiveDeliveries = deliveries.findAll({ d -> d.isOnDate(day) && d.isOutForDelivery() })
      def positiveDeliveryCount = positiveDeliveries.inject(0, { acc, val -> acc + (val.quantity * val.gallons) })
      def negativeDeliveries = deliveries.findAll({ d -> d.isOnDate(day) && d.isReturned() })
      def deliveryTotal = negativeDeliveries.inject(positiveDeliveryCount, { acc, val -> acc - (val.quantity * val.gallons) })
      deliveryRow.add(deliveryTotal)
    }
    tableData.add(deliveryRow)

    def totalRow = ['TOTAL']
    for (LocalDate day in previousWeek) {
      def sales = receipts.findAll({ r -> r.isOnDate(day) }).inject(0, { acc, val -> acc + val.totalGallons })
      def deliveryPositives = deliveries.findAll({ d -> d.isOnDate(day) && d.isOutForDelivery() }).inject(0, { acc, val -> acc + (val.quantity * val.gallons) })
      def deliveryTotal = deliveries.findAll({d -> d.isOnDate(day) && d.isReturned() }).inject(deliveryPositives, { acc, val -> acc - (val.quantity * val.gallons) })
      totalRow.add(sales + deliveryTotal)
    }
    tableData.add(totalRow)

    def totalizerRow = ['TOTALIZER']
    Readings readingsObj = new Readings(readings: readings)
    for (LocalDate day in previousWeek) {
      def gallonsFor = readingsObj.totalizeGallonsFor(day)
      totalizerRow.add(gallonsFor)
    }
    tableData.add(totalizerRow)

    def percentDiffRow = ['DIFFERENCE %']
    for(int i = 1; i < totalizerRow.size(); i++) {
      def total = new BigDecimal(totalizerRow[i])
      def difference
      if(total > 0) {
        def subtract = total.subtract(new BigDecimal(totalRow[i]))
        difference = subtract.divide(total, 2, RoundingMode.HALF_UP)
      } else {
        difference = 0
      }
      percentDiffRow.add(difference.multiply(new BigDecimal(100)).setScale(0))
    }
    tableData.add(percentDiffRow)

    return tableData
  }
}
