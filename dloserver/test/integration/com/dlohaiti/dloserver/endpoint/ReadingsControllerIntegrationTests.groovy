package com.dlohaiti.dloserver.endpoint

import com.dlohaiti.dloserver.*
import groovy.json.JsonBuilder
import org.junit.Before
import org.junit.Test

class ReadingsControllerIntegrationTests {

  def controller = new ReadingsController()
  def kiosk
  def samplingSite
  def p1
  def p2

  @Before void setUp() {
      Country country=new Country(name: "haiti").save(failOnError: true)
      Region region = new Region(name: "Region1",country: country).save(failOnError: true)

      kiosk = new Kiosk(name: 'testkiosk', apiKey: 'pw',region:region).save(failOnError: true)
    samplingSite = new SamplingSite(name: 'Borehole - Late Day').save(failOnError: true)
    p1 = new Parameter(name: 'param1', isOkNotOk: false, isUsedInTotalizer: false, manual: true, active: true, samplingSites: [samplingSite]).save(failOnError: true)
    p2 = new Parameter(name: 'param2', isOkNotOk: false, isUsedInTotalizer: false, manual: true, active: true, samplingSites: [samplingSite]).save(failOnError: true)
    controller.request.contentType = 'application/json'
    controller.request.kiosk = kiosk
  }

  @Test void shouldRespondWithCreatedForSuccess() {
    def req = [
        createdDate: '2013-04-24 12:00:01 EDT',
        samplingSiteName: samplingSite.name,
        measurements: [
            [parameterName: p1.name, value: 12],
            [parameterName: p2.name, value: 39]
        ]
    ]
    controller.request.json = new JsonBuilder(req).toString()

    controller.save()

    assert 201 == controller.response.status
    assert 'application/json;charset=utf-8' == controller.response.contentType
    assert '{"errors":[]}' == controller.response.contentAsString
    assert 1 == Reading.count()
    assert 2 == Measurement.count()
  }

  @Test void shouldRespondWithUnprocessableForMissingSamplingSite() {
    def req = [
        createdDate: '2013-04-24 12:00:01 EDT',
        samplingSiteName: 'missingSite',
        measurements: [
            [parameterName: p1.name, value: 50]
        ]
    ]
    controller.request.json = new JsonBuilder(req).toString()

    controller.save()

    assert 422 == controller.response.status
    assert 'application/json;charset=utf-8' == controller.response.contentType
    assert '{"errors":["MISSING_SAMPLING_SITE"]}' == controller.response.contentAsString
    assert 0 == Reading.count()
    assert 0 == Measurement.count()
  }

  @Test void shouldRespondWithUnprocessableForConstraintViolation() {
    def req = [
        createdDate: null,
        samplingSiteName: samplingSite.name,
        measurements: [
            [parameterName: p1.name, value: 50]
        ]
    ]
    controller.request.json = new JsonBuilder(req).toString()

    controller.save()

    assert 422 == controller.response.status
    assert 'application/json;charset=utf-8' == controller.response.contentType
    assert '{"errors":["CREATEDDATE_NULLABLE"]}' == controller.response.contentAsString
    assert 0 == Reading.count()
    assert 0 == Measurement.count()
  }

  @Test void shouldRespondWithInternalServerErrorWhenSomethingGoesAwry() {
    controller.readingsService = [saveReading: {-> throw new RuntimeException() }] as ReadingsService

    controller.save()

    assert 500 == controller.response.status
    assert 'application/json;charset=utf-8' == controller.response.contentType
    assert '{"errors":["SERVER_ERROR"]}' == controller.response.contentAsString
    assert 0 == Reading.count()
  }
}
