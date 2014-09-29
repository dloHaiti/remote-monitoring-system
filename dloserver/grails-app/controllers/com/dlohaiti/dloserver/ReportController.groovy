package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.utils.DateUtil

class ReportController {

    def configurationService
    def outstandingPaymentService
    def salesReportService
    def readingsReportService
    def volumeReportService

    def index() {
        [kioskName: request.kiosk.name]
    }

    def menu() {
        [kioskName: request.kiosk.name]
    }

    def outstandingPayment() {
        [kioskName: request.kiosk.name]
        Kiosk kiosk = Kiosk.findByName(params.kioskName)
        def currencyCode = configurationService.currencyCode
        def tableData = outstandingPaymentService.outStandingPaymentsAtKiosk(kiosk, currencyCode)
        [kioskName: kiosk.name, tableData: tableData]
    }

    def readings() {
        Kiosk kiosk = Kiosk.findByName(params.kioskName)
        def readings = readingsReportService.readingsForKioskAndCreatedDateGreaterThanOrEqualTo(kiosk, DateUtil.oneWeekAgoMidnight())
        def parameters = kiosk.getParameters()
        def paramMap = readingsReportService.parameterMapForReadings(readings, parameters, DateUtil.previousWeek())
        def model = [kioskName: request.kiosk.name, parameters: parameters, lastWeek: DateUtil.previousWeek()
                .collect({ d -> d.toDate() }), readings: readings, paramMap: paramMap]

        render(view: 'readings', model: model)
    }

    def sales() {
        String filterType = params.filterType != null ? params.filterType : '';
        String filterParam = params.filterParam != null ? params.filterParam : '';

        def model = salesReportService.salesData(params.kioskName, filterType, filterParam)
        render(view: 'sales', model: model)
    }

    def volume() {
        String filterType = params.filterType != null ? params.filterType : 'kiosk';
        String filterParam = params.filterParam != null ? params.filterParam : 'sku';

        def model = volumeReportService.volumeReportData(params.kioskName, filterType, filterParam)

        render(view: 'volume', model: model)
    }
}
