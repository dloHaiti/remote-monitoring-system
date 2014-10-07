package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.utils.DateUtil
import org.joda.time.LocalDate
import java.math.RoundingMode

class VolumeReportService {

    def volumeReportData(String kioskName, String filterType, String filterParam) {
        Kiosk kiosk = Kiosk.findByName(kioskName)

        def model = [:]
        if (filterType.equalsIgnoreCase('region')) {
            model = volumeByRegionForKiosk(kiosk, filterParam)
        } else {
            model = volumeByDay(kiosk, filterParam)
        }
        return model
    }

    private volumeByRegionForKiosk(Kiosk kiosk, String filterParam) {
        Region region = kiosk.region
        List<Receipt> receipts = receiptsForAllKiosksInRegion(region)
        List<Reading> readings = readingsForAllKiosksInRegion(region)
        List<Product> products = Product.findAll()

        def tableData = buildTableData(DateUtil.previousWeek(), products, receipts, readings, filterParam)
        [kioskName: kiosk.name, chartData: new TableToChart().convertWithoutRowsTitled(tableData, ['TOTAL', 'DIFFERENCE %']), tableData: tableData, skusPresent: products.size()]
    }

    private volumeByDay(Kiosk kiosk, String filterParam) {
        def oneWeekAgoMidnight = DateUtil.oneWeekAgoMidnight()
        List<Receipt> receipts = Receipt.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, oneWeekAgoMidnight)
        List<Reading> readings = Reading.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, oneWeekAgoMidnight)
        List<Product> products = Product.findAll()

        def tableData = buildTableData(DateUtil.previousWeek(), products, receipts, readings, filterParam)

        [kioskName: kiosk.name, chartData: new TableToChart().convertWithoutRowsTitled(tableData, ['TOTAL', 'DIFFERENCE %']), tableData: tableData, skusPresent: products.size()]
    }

    private buildTableData(List<LocalDate> previousWeek, List<Product> products, List<Receipt> receipts, List<Reading> readings, String filterParam) {
        def tableData = [['']]
        if("sku".equalsIgnoreCase(filterParam)) {
            tableData = tableDataFilteredBySKU(previousWeek, products, receipts, readings)
        }
        if("salesChannel".equalsIgnoreCase(filterParam)) {
            tableData = tableDataFilteredBySalesChannel(previousWeek, receipts)
        }
        return tableData
    }

    private ArrayList<String> tableDataFilteredBySKU(List<LocalDate> previousWeek, List<Product> products, List<Receipt> receipts, List<Reading> readings) {
        def tableHeader = ['']
        for (day in previousWeek) {
            tableHeader.add(day.toString('dd-MMM-yy'))
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

        def totalRow = ['TOTAL']
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

        def percentDiffRow = ['DIFFERENCE %']
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
        tableData
    }

    List<Receipt> receiptsForAllKiosksInRegion(Region region) {
        def receipts = [];
        def weekAgoMidnight = DateUtil.oneWeekAgoMidnight()
        def kiosks = Kiosk.findAllByRegion(region)
        kiosks.each {kiosk -> receipts.addAll(Receipt.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, weekAgoMidnight))}
        receipts
    }

    List<Reading> readingsForAllKiosksInRegion(Region region) {
        def readings = [];
        def weekAgoMidnight = DateUtil.oneWeekAgoMidnight()
        def kiosks = Kiosk.findAllByRegion(region)
        kiosks.each {kiosk -> readings.addAll(Reading.findAllByKioskAndCreatedDateGreaterThanEquals(kiosk, weekAgoMidnight))}
        readings
    }

}