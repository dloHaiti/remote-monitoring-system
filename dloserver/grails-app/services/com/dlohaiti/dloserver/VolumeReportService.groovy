package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.utils.DateUtil
import org.joda.time.LocalDate

import java.math.RoundingMode

class VolumeReportService {

    def volumeReportData(String kioskName, String filterType, String filterParam,LocalDate fromDate,LocalDate toDate,def totalMessage,def differenceMessage) {
        Kiosk kiosk = Kiosk.findByName(kioskName)

        def model = [:]
        if (filterType.equalsIgnoreCase('region')) {
            model = volumeByRegionForKiosk(kiosk, filterParam,fromDate,toDate,totalMessage,differenceMessage)
        } else {
            model = volumeByDay(kiosk, filterParam,fromDate,toDate,totalMessage,differenceMessage)
        }
        return model
    }

    private volumeByRegionForKiosk(Kiosk kiosk, String filterParam,LocalDate fromDate,LocalDate toDate,def totalMessage,def differenceMessage) {
        Region region = kiosk.region
        List<Receipt> receipts = receiptsForAllKiosksInRegion(region,fromDate,toDate)
        List<Reading> readings = readingsForAllKiosksInRegion(region,fromDate,toDate)
        List<Product> products = Product.findAll()

        def tableData = buildTableData(DateUtil.getWeekDataByFromDate(fromDate,toDate), products, receipts, readings, filterParam,totalMessage,differenceMessage)
        [kioskName: kiosk.name, chartData: new TableToChart().convertWithoutRowsTitled(tableData, [totalMessage, differenceMessage]), tableData: tableData, skusPresent: getProductCount(products)]
    }

    private volumeByDay(Kiosk kiosk, String filterParam,LocalDate fromDate,LocalDate toDate,def totalMessage,def differenceMessage) {
        List<Receipt> receipts = Receipt.findAllByKioskAndCreatedDateGreaterThanEqualsAndCreatedDateLessThan(kiosk, fromDate.toDate(),toDate.toDate())
        List<Reading> readings = Reading.findAllByKioskAndCreatedDateGreaterThanEqualsAndCreatedDateLessThan(kiosk, fromDate.toDate(),toDate.toDate())
        List<Product> products = Product.findAll()
        def tableData = buildTableData(DateUtil.getWeekDataByFromDate(fromDate,toDate), products, receipts, readings, filterParam,totalMessage,differenceMessage)

        [kioskName: kiosk.name, chartData: new TableToChart().convertWithoutRowsTitled(tableData, [totalMessage, differenceMessage]), tableData: tableData, skusPresent: getProductCount(products)]
    }

    private buildTableData(List<LocalDate> previousWeek, List<Product> products, List<Receipt> receipts, List<Reading> readings, String filterParam,def totalMessage,def differenceMessage) {
        def tableData = [['']]
        if("sku".equalsIgnoreCase(filterParam)) {
            tableData = tableDataFilteredBySKU(previousWeek, products, receipts, readings,totalMessage,differenceMessage)
        }
        if("salesChannel".equalsIgnoreCase(filterParam)) {
            tableData = tableDataFilteredBySalesChannel(previousWeek, receipts)
        }
        return tableData
    }

    private int getProductCount(List<Product> products){
        int i =0
        for (product in products) {
            // Not including the product in the report data if the Volume (Gallons) of product is 0
            if (product.getGallons() != null && product.getGallons() != 0.0) {
                i=i+1
            }
        }
        return i
    }

    private ArrayList<String> tableDataFilteredBySKU(List<LocalDate> previousWeek, List<Product> products, List<Receipt> receipts, List<Reading> readings,def totalMessage,def differenceMessage) {
        def tableHeader = ['']
        for (day in previousWeek) {
            tableHeader.add(day.toString('dd-MMM-yy'))
        }
        def tableData = [tableHeader]
        for (product in products) {
            def row = [product.description]
            // Not including the product in the report data if the Volume (Gallons) of product is 0
            if (product.getGallons() != null && product.getGallons() != 0.0) {
                for (day in previousWeek) {
                    def relevantReceipts = receipts.findAll({ r -> r.isOnDate(day) })
                    def totalForSku = relevantReceipts.inject(0, { acc, val -> acc + val.totalGallonsForSku(product.sku) })
                    row.add(totalForSku)
                }
                tableData.add(row)
            }
        }

        def totalRow = [totalMessage]
        for (LocalDate day in previousWeek) {
            def sales = receipts.findAll({ r -> r.isOnDate(day) }).inject(0, { acc, val -> acc + val.totalGallons })
            totalRow.add(sales)
        }
        tableData.add(totalRow)

        def totalizerRow = ['TOTALIZER']
        Readings readingsObj = new Readings(readings: readings)
        for (LocalDate day in previousWeek) {
            def gallonsFor = readingsObj.totalizeGallonsFor(day)
            totalizerRow.add(gallonsFor)
        }
        tableData.add(totalizerRow)

        def percentDiffRow = [differenceMessage]
        for (int i = 1; i < totalizerRow.size(); i++) {
            def total = new BigDecimal(totalizerRow[i])
            def difference
            if (total > 0) {
                def subtract = total.subtract(new BigDecimal(totalRow[i]))
                difference = subtract.divide(total, 2, RoundingMode.HALF_UP)
            } else {
                difference = 0
            }
            percentDiffRow.add(difference.multiply(new BigDecimal(100)).setScale(0))
        }
        tableData.add(percentDiffRow)
        tableData
    }

    private ArrayList<String> tableDataFilteredBySalesChannel(List<LocalDate> previousWeek, List<Receipt> receipts) {
        def tableHeader = ['']
        for (day in previousWeek) {
            tableHeader.add(day.toString('dd-MMM-yy'))
        }
        def tableData = [tableHeader]
        def salesChannels = SalesChannel.findAll()
        for (salesChannel in salesChannels) {
            def row = [salesChannel.name]
            for (day in previousWeek) {
                def relevantReceipts = receipts.findAll({ r -> r.isOnDate(day) && r.salesChannel.name == salesChannel.name })

                def totalForSku = relevantReceipts.inject(0, { acc, val -> acc + val.totalGallons })
                row.add(totalForSku)
            }
            tableData.add(row)
        }

        def totalRow = ['TOTAL']
        for(def i = 0; i < tableData.size(); i++){
            def total = 0;
            for(def j = 1; j < tableData[i].size(); j++){
                if (i==0){
                    totalRow[j]=0
                    continue
                }

                totalRow[j]=new BigDecimal(totalRow[j]) + new BigDecimal(tableData[i][j])
            }
        }
        tableData.add(totalRow)
        tableData
    }

    List<Receipt> receiptsForAllKiosksInRegion(Region region,LocalDate fromDate,LocalDate toDate) {
        def receipts = [];
        def kiosks = Kiosk.findAllByRegion(region)
        kiosks.each {kiosk -> receipts.addAll(Receipt.findAllByKioskAndCreatedDateGreaterThanEqualsAndCreatedDateLessThan(kiosk, fromDate.toDate(),toDate.toDate()))}
        receipts
    }

    List<Reading> readingsForAllKiosksInRegion(Region region,LocalDate fromDate,LocalDate toDate) {
        def readings = [];
        def kiosks = Kiosk.findAllByRegion(region)
        kiosks.each {kiosk -> readings.addAll(Reading.findAllByKioskAndCreatedDateGreaterThanEqualsAndCreatedDateLessThan(kiosk, fromDate.toDate(),toDate.toDate()))}
        readings
    }

}
