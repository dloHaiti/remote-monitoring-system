package com.dlohaiti.dloserver

class ReportController {

    def index() {
        [reports: Report.all]
    }

    def moose() {
      Kiosk kiosk = Kiosk.findByName(params.kioskName)
      Calendar now = Calendar.getInstance()
      now.add(-7, Calendar.DATE)
      new Date()
      List<Delivery> deliveries = Delivery.findAllByKioskAndTimestamp(kiosk)
      List<Receipt> receipts = Receipt.findAllByKiosk(kiosk)
    }
}
