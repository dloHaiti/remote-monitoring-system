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
        def tableHeader = ['']

        def previousWeek = DateUtil.previousWeek()
        for (day in previousWeek) {
            tableHeader.add(day.toString('dd-MMM-yy'))
        }
        def tableData = [['']]
        if ("sku".equalsIgnoreCase(filterParameter)) {
            tableData = buildTableDataFilteredBySKU(products, previousWeek, receipts, tableHeader)
        }
        if ("salesChannel".equalsIgnoreCase(filterParameter)) {
            tableData = buildTableDataFilteredBySalesChannel(previousWeek, receipts, tableHeader)
        }
        if ("productCategory".equalsIgnoreCase(filterParameter)) {
            tableData = buildTableDataFilteredByProductCategory(previousWeek, receipts, tableHeader)
        }
        [kioskName: currentKiosk.name, tableData: tableData, chartData: new TableToChart().convertWithoutRowsTitled(tableData, ['TOTAL'])]
    }

    private List<List<String>> buildTableDataFilteredBySKU(List<Product> products, List<LocalDate> previousWeek, List<Receipt> receipts, List<String> tableHeader) {
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

        def totalRow = ['TOTAL']
        previousWeek.eachWithIndex { LocalDate day, int i ->
            def total = receipts.findAll({ r -> r.isOnDate(day) }).inject(0, { acc, val -> acc + val.total })
            totalRow.add(total)
        }
        tableData.add(totalRow)
        tableData
    }

    private List<List<String>> buildTableDataFilteredBySalesChannel(List<LocalDate> previousWeek, List<Receipt> receipts, List<String> tableHeader) {
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

    private List<List<String>> buildTableDataFilteredByProductCategory(List<LocalDate> previousWeek, List<Receipt> receipts, List<String> tableHeader) {
        def tableData = [tableHeader]
        def productCategories = ProductCategory.findAll();
        for (productCategory in productCategories) {
            def row = [productCategory.name]
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

        def previousWeek = DateUtil.previousWeek()
        def tableHeader = ['']

        for (day in previousWeek) {
            tableHeader.add(day.toString('dd-MMM-yy'))
        }
        def tableData = [['']]
        if ("sku".equalsIgnoreCase(filterParameter)) {
            tableData = buildTableDataFilteredBySKU(products, previousWeek, receipts, tableHeader)
        }
        if ("salesChannel".equalsIgnoreCase(filterParameter)) {
            tableData = buildTableDataFilteredBySalesChannel(previousWeek, receipts, tableHeader)
        }
        if ("productCategory".equalsIgnoreCase(filterParameter)) {
            tableData = buildTableDataFilteredByProductCategory(previousWeek, receipts, tableHeader)
        }
        [kioskName: kiosk.name, tableData: tableData, chartData: new TableToChart().convertWithoutRowsTitled(tableData, ['TOTAL'])]
    }

    List<Receipt> receiptsForAllKiosksInRegion(Region region) {
        def receipts = [];
        def weekAgoMidnight = DateUtil.oneWeekAgoMidnight()
        def kiosks = Kiosk.findAllByRegion(region)
        kiosks.each { kiosk -> receipts.addAll(Receipt.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, weekAgoMidnight)) }
        receipts
    }
}
