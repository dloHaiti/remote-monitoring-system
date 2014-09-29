package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.utils.DateUtil
import org.joda.time.LocalDate

/**
 * Created by kumaranv on 29/09/14.
 */
class SalesReportService {

    def salesData(String kioskName, String filterType, String filterParam) {
        Kiosk kiosk = Kiosk.findByName(kioskName)

        def model = [:]
        if (filterType.equalsIgnoreCase("region")) {
            model = salesByRegionForKiosk(kiosk)
        } else {
            model = salesByDay(kiosk)
        }
        model
    }

    private salesByRegionForKiosk(Kiosk currentKiosk) {
        Region region = currentKiosk.region;
        List<Product> products = Product.findAllByActive(true)
        List<Receipt> receipts = receiptsForAllKiosksInRegion(region)
        List<Delivery> deliveries = deliveriesForAllKiosksInRegion(region)
        def tableHeader = ['']

        def previousWeek = DateUtil.previousWeek()
        for (day in previousWeek) {
            tableHeader.add(day.toString('dd-MMM-yy'))
        }
        def tableData = [tableHeader]
        for (product in products) {
            def row = [product.sku]
            for (day in previousWeek) {
                def dayReceipts = receipts.findAll({ r -> r.isOnDate(day) })
                def lineItemsWithSku = dayReceipts.receiptLineItems.flatten().findAll({ ReceiptLineItem item -> item.sku == product.sku })
                def skuTotal = lineItemsWithSku.inject(0, { BigDecimal acc, ReceiptLineItem val -> acc + val.price })
                row.add(skuTotal)
            }
            tableData.add(row)
        }

        def deliveriesRow = ['DELIVERY']
        for (day in previousWeek) {
            def dayDeliveries = deliveries.findAll({ d -> d.isOnDate(day) })
            def positiveDeliveries = dayDeliveries.findAll({ Delivery d -> d.isOutForDelivery() }).inject(0, { acc, val -> acc + (val.quantity * val.price.amount) })
            def totalDeliveries = dayDeliveries.findAll({ Delivery d -> d.isReturned() }).inject(positiveDeliveries, { acc, val -> acc - (val.quantity * val.price.amount) })
            deliveriesRow.add(totalDeliveries)
        }
        tableData.add(deliveriesRow)

        def totalRow = ['TOTAL']
        previousWeek.eachWithIndex { LocalDate day, int i ->
            def total = receipts.findAll({ r -> r.isOnDate(day) }).inject(0, { acc, val -> acc + val.total })
            totalRow.add(total + deliveriesRow[i + 1])
        }
        tableData.add(totalRow)

        [kioskName: currentKiosk.name, tableData: tableData, chartData: new TableToChart().convertWithoutRowsTitled(tableData, ['TOTAL'])]
    }

    private salesByDay(Kiosk kiosk) {
        List<Product> products = Product.findAllByActive(true)
        List<Receipt> receipts = Receipt.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, DateUtil.oneWeekAgoMidnight())
        List<Delivery> deliveries = Delivery.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, DateUtil.oneWeekAgoMidnight())

        def tableHeader = ['']
        def previousWeek = DateUtil.previousWeek()
        for (day in previousWeek) {
            tableHeader.add(day.toString('dd-MMM-yy'))
        }
        def tableData = [tableHeader]
        for (product in products) {
            def row = [product.sku]
            for (day in previousWeek) {
                def dayReceipts = receipts.findAll({ r -> r.isOnDate(day) })
                def lineItemsWithSku = dayReceipts.receiptLineItems.flatten().findAll({ ReceiptLineItem item -> item.sku == product.sku })
                def skuTotal = lineItemsWithSku.inject(0, { BigDecimal acc, ReceiptLineItem val -> acc + val.price })
                row.add(skuTotal)
            }
            tableData.add(row)
        }

        def deliveriesRow = ['DELIVERY']
        for (day in previousWeek) {
            def dayDeliveries = deliveries.findAll({ d -> d.isOnDate(day) })
            def positiveDeliveries = dayDeliveries.findAll({ Delivery d -> d.isOutForDelivery() }).inject(0, { acc, val -> acc + (val.quantity * val.price.amount) })
            def totalDeliveries = dayDeliveries.findAll({ Delivery d -> d.isReturned() }).inject(positiveDeliveries, { acc, val -> acc - (val.quantity * val.price.amount) })
            deliveriesRow.add(totalDeliveries)
        }
        tableData.add(deliveriesRow)

        def totalRow = ['TOTAL']
        previousWeek.eachWithIndex { LocalDate day, int i ->
            def total = receipts.findAll({ r -> r.isOnDate(day) }).inject(0, { acc, val -> acc + val.total })
            totalRow.add(total + deliveriesRow[i + 1])
        }
        tableData.add(totalRow)

        [kioskName: kiosk.name, tableData: tableData, chartData: new TableToChart().convertWithoutRowsTitled(tableData, ['TOTAL'])]
    }

    List<Delivery> deliveriesForAllKiosksInRegion(Region region) {
        List<Delivery> deliveries = new ArrayList<>();
        def weekAgoMidnight = DateUtil.oneWeekAgoMidnight()
        def kiosks = Kiosk.findAllByRegion(region)
        kiosks.each { kiosk -> deliveries.addAll(Delivery.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, weekAgoMidnight)) }
        deliveries
    }

    List<Receipt> receiptsForAllKiosksInRegion(Region region) {
        def receipts = [];
        def weekAgoMidnight = DateUtil.oneWeekAgoMidnight()
        def kiosks = Kiosk.findAllByRegion(region)
        kiosks.each { kiosk -> receipts.addAll(Receipt.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, weekAgoMidnight)) }
        receipts
    }
}
