package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.endpoint.DeliveriesController
import org.junit.Before
import org.junit.Test;

class DeliveriesControllerIntegrationTests {

  def controller

  @Before void setUp() {
    controller = new DeliveriesController()
    new Kiosk(name: "k1").save()
  }

  @Test void shouldRespondWithCreatedWhenSuccessful() {
    controller.request.json = '{"kioskId":"k1","quantity":24,"type":"RETURN","createdDate":"2013-04-24 12:00:01 EDT"}'
    controller.save()
    assert 201 == controller.response.status
    assert 1 == Delivery.count()
  }

  @Test void shouldRespondWithUnprocessableWhenMalformed() {
    controller.request.json = '{"kioskId":"0293i4","quantity":24,"type":"RETURN","createdDate":"2013-04-24 12:00:01 EDT"}'
    controller.save()
    assert 422 == controller.response.status
    assert 0 == Delivery.count()
  }
}
