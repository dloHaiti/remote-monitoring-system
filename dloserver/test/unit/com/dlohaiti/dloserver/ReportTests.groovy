package com.dlohaiti.dloserver

import grails.test.mixin.*
import org.junit.*

@TestFor(Report)
class ReportTests {

  void testConstraints() {
    mockForConstraintsTests(Report, [new Report(name: 'volume')])

    def report = new Report()
    assert !report.validate()
    assert 'nullable' == report.errors["name"]

    report = new Report(name: '')
    assert !report.validate()
    assert 'blank' == report.errors["name"]

    report = new Report(name: 'volume')
    assert !report.validate()
    assert 'unique' == report.errors["name"]
  }
}
