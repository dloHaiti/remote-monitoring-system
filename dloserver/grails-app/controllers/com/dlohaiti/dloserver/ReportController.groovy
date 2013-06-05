package com.dlohaiti.dloserver

class ReportController {

    def index() {
        [reports: Report.all]
    }

    def show(Long id) {
        def reportInstance = Report.get(id)
        if (!reportInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'report.label', default: 'Report'), id])
            redirect(action: "list")
            return
        }

        [reportInstance: reportInstance]
    }

}
