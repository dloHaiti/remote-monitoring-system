package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.utils.DateUtil
import org.joda.time.LocalDate

/**
 * Created by kumaranv on 29/09/14.
 */
class SalesReportService {

    def salesData(String kioskName, String filterType, String filterParameter, LocalDate fromDate, LocalDate toDate) {
        Kiosk kiosk = Kiosk.findByName(kioskName)

        def model = [:]
        if (filterType.equalsIgnoreCase("region")) {
            model = salesByRegionForKiosk(kiosk, filterParameter, fromDate, toDate)
        } else {
            model = salesByDay(kiosk, filterParameter, fromDate, toDate)
        }
        model
    }

    private salesByRegionForKiosk(Kiosk currentKiosk, String filterParameter, LocalDate fromDate, LocalDate toDate) {
        Region region = currentKiosk.region;
        List<Product> products = Product.findAllByActive(true)
        List<Receipt> receipts = receiptsForAllKiosksInRegion(region, fromDate, toDate)
        def tableHeader = ['']

        def previousWeek = DateUtil.getWeekDataByFromDate(fromDate, toDate);
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
            def row = [product.description]
            // Not including the product in the report data if the price of product is 0
            if (product.price.getAmount() != null && product.price.getAmount() !=0.0) {
                for (day in previousWeek) {
                    def dayReceipts = receipts.findAll({ r -> r.isOnDate(day) })
                    def lineItemsWithSku = dayReceipts.receiptLineItems.flatten().findAll({ ReceiptLineItem item -> item.sku == product.sku })
                    def skuTotal = lineItemsWithSku.inject(0, { BigDecimal acc, ReceiptLineItem val -> acc + val.price })
                    row.add(skuTotal)
                }
                tableData.add(row)
            }
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
        def totalRow = ['TOTAL']
        for (salesChannel in salesChannels) {
            def row = [salesChannel.name]
            int index=1
            for (day in previousWeek) {
                def dayReceipts = receipts.findAll({ r -> r.isOnDate(day) && r.salesChannel.name == salesChannel.name })
                def lineItemsForSalesChannel = dayReceipts.receiptLineItems.flatten().findAll();
                def total = lineItemsForSalesChannel.inject(0, { BigDecimal acc, ReceiptLineItem val -> acc + val.price })
                row.add(total)
                if(index>totalRow.size()-1){
                    totalRow.add(total)
                }else{
                    totalRow[index]= totalRow[index]+total
                }
                index++
            }
            tableData.add(row)
        }
        tableData.add(totalRow)
        tableData
    }

    private List<List<String>> buildTableDataFilteredByProductCategory(List<LocalDate> previousWeek, List<Receipt> receipts, List<String> tableHeader) {
        def tableData = [tableHeader]
        def productCategories = ProductCategory.findAll();
        def totalRow = ['TOTAL']
        for (productCategory in productCategories) {
            def row = [productCategory.name]
            int index=1
            for (day in previousWeek) {
                def dayReceipts = receipts.findAll({ r -> r.isOnDate(day) })
                def lineItemsForSalesChannel = dayReceipts.receiptLineItems.flatten().findAll({ ReceiptLineItem item -> item.type.equals('PRODUCT') && Product.findBySku(item.sku).category.name.equals(productCategory.name) });
                def total = lineItemsForSalesChannel.inject(0, { BigDecimal acc, ReceiptLineItem val -> acc + val.price })
                row.add(total)
                if(index>totalRow.size()-1){
                    totalRow.add(total)
                }else{
                    totalRow[index]= totalRow[index]+total
                }
                index++
            }
            tableData.add(row)
        }
        tableData.add(totalRow)
        tableData
    }

    private salesByDay(Kiosk kiosk, String filterParameter, LocalDate fromDate, LocalDate toDate) {
        List<Product> products = Product.findAllByActive(true)
        List<Receipt> receipts = Receipt.findAllByKioskAndCreatedDateGreaterThanEqualsAndCreatedDateLessThan(kiosk, fromDate.toDate(), toDate.toDate())

        def previousWeek = DateUtil.getWeekDataByFromDate(fromDate, toDate);
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

    List<Receipt> receiptsForAllKiosksInRegion(Region region, LocalDate fromDate, LocalDate toDate) {
        def receipts = [];
        def kiosks = Kiosk.findAllByRegion(region)
        kiosks.each { kiosk -> receipts.addAll(Receipt.findAllByKioskAndCreatedDateGreaterThanEqualsAndCreatedDateLessThan(kiosk, fromDate.toDate(), toDate.toDate())) }

        receipts
    }
}
