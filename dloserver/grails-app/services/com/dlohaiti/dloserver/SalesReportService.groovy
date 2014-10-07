package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.utils.DateUtil
import org.joda.time.LocalDate

/**
 * Created by kumaranv on 29/09/14.
 */
class SalesReportService {

    def salesData(String kioskName, String filterType, String filterParameter) {
        Kiosk kiosk = Kiosk.findByName(kioskName)

        def model = [:]
        if (filterType.equalsIgnoreCase("region")) {
            model = salesByRegionForKiosk(kiosk, filterParameter)
        } else {
            model = salesByDay(kiosk, filterParameter)
        }
        model
    }

    private salesByRegionForKiosk(Kiosk currentKiosk, String filterParameter) {
        Region region = currentKiosk.region;
        List<Product> products = Product.findAllByActive(true)
        List<Receipt> receipts = receiptsForAllKiosksInRegion(region)
        List<Delivery> deliveries = deliveriesForAllKiosksInRegion(region)
        def tableHeader = ['']

        def previousWeek = DateUtil.previousWeek()
        for (day in previousWeek) {
            tableHeader.add(day.toString('dd-MMM-yy'))
        }
        def tableData = [['']]
        if ("sku".equalsIgnoreCase(filterParameter)) {
            tableData = buildTableDataFilteredBySKU(products, previousWeek, receipts, tableHeader, deliveries)
        }
        if ("salesChannel".equalsIgnoreCase(filterParameter)) {
            tableData = buildTableDataFilteredBySalesChannel(previousWeek, receipts, tableHeader, deliveries)
        }
        if ("productCategory".equalsIgnoreCase(filterParameter)) {
            tableData = buildTableDataFilteredByProductCategory(previousWeek, receipts, tableHeader, deliveries)
        }
        [kioskName: currentKiosk.name, tableData: tableData, chartData: new TableToChart().convertWithoutRowsTitled(tableData, ['TOTAL'])]
    }

    private List<List<String>> buildTableDataFilteredBySKU(List<Product> products, List<LocalDate> previousWeek, List<Receipt> receipts, List<String> tableHeader, List<Delivery> deliveries) {
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
        tableData
    }

    private List<List<String>> buildTableDataFilteredBySalesChannel(List<LocalDate> previousWeek, List<Receipt> receipts, List<String> tableHeader, List<Delivery> deliveries) {
        def tableData = [tableHeader]
        def salesChannels = SalesChannel.findAll();
        for (salesChannel in salesChannels) {
            def row = [salesChannel.name]
            for (day in previousWeek) {
                def dayReceipts = receipts.findAll({r -> r.isOnDate(day) && r.salesChannel.name == salesChannel.name})
                def lineItemsForSalesChannel = dayReceipts.receiptLineItems.flatten().findAll();
                def total = lineItemsForSalesChannel.inject (0, { BigDecimal acc, ReceiptLineItem val -> acc + val.price })
                row.add(total)
            }
            tableData.add(row)
        }
        tableData
    }

    private List<List<String>> buildTableDataFilteredByProductCategory(List<LocalDate> previousWeek, List<Receipt> receipts, List<String> tableHeader, List<Delivery> deliveries) {
        def tableData = [tableHeader]
        def productCategories = ProductCategory.findAll();
        print (productCategories)
        for (productCategory in productCategories) {
            def row = [productCategory.name]
            print(productCategory.name)
            for (day in previousWeek) {
                def dayReceipts = receipts.findAll({r -> r.isOnDate(day)  })
                def lineItemsForSalesChannel = dayReceipts.receiptLineItems.flatten().findAll({ ReceiptLineItem item -> item.type.equals('PRODUCT') && Product.findBySku(item.sku).category.name.equals(productCategory.name)});
                def total = lineItemsForSalesChannel.inject (0, { BigDecimal acc, ReceiptLineItem val -> acc + val.price })
                row.add(total)
            }
            tableData.add(row)
        }
        tableData
    }

    private salesByDay(Kiosk kiosk, String filterParameter) {
        List<Product> products = Product.findAllByActive(true)
        List<Receipt> receipts = Receipt.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, DateUtil.oneWeekAgoMidnight())
        List<Delivery> deliveries = Delivery.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, DateUtil.oneWeekAgoMidnight())

        def previousWeek = DateUtil.previousWeek()
        def tableHeader = ['']

        for (day in previousWeek) {
            tableHeader.add(day.toString('dd-MMM-yy'))
        }
        def tableData = [['']]
        if ("sku".equalsIgnoreCase(filterParameter)) {
            tableData = buildTableDataFilteredBySKU(products, previousWeek, receipts, tableHeader, deliveries)
        }
        if ("salesChannel".equalsIgnoreCase(filterParameter)) {
            print ("Filter: " + filterParameter)
            tableData = buildTableDataFilteredBySalesChannel(previousWeek, receipts, tableHeader, deliveries)
        }
        if ("productCategory".equalsIgnoreCase(filterParameter)) {
            tableData = buildTableDataFilteredByProductCategory(previousWeek, receipts, tableHeader, deliveries)
        }
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
