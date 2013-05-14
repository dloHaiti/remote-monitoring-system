package com.dlohaiti.dloserver

class ReportController {
  def index() {
    [measurements: Measurement.getAll()]
  }
}
