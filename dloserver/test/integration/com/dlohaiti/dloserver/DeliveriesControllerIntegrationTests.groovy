package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.endpoint.DeliveriesController
import groovy.json.JsonBuilder
import org.junit.Before
import org.junit.Test;

class DeliveriesControllerIntegrationTests {

  DeliveriesController controller
  DeliveryAgent agent

  @Before void setUp() {
    def kiosk = new Kiosk(name: 'k1', apiKey: 'pw').save(failOnError: true)
    controller = new DeliveriesController()
    controller.request.contentType = 'application/json'
    controller.request.kiosk = kiosk
    agent = new DeliveryAgent(name: 'agent 1', kiosk: kiosk, active: true).save(failOnError: true)
  }

  @Test void shouldRespondWithCreatedWhenSuccessful() {
    def req = [
      quantity: 24,
      type: 'RETURN',
      createdDate: '2013-04-24 12:00:01 EDT',
      agentName: agent.getName()
    ]
    controller.request.json = new JsonBuilder(req).toString()

    controller.save()

    assert 201 == controller.response.status
    assert 'application/json;charset=utf-8' == controller.response.contentType
    assert '{"errors":[]}' == controller.response.contentAsString
    assert 1 == Delivery.count()
  }

  @Test void shouldRespondWithUnprocessableWhenDeliveryAgentDoesNotExist() {
    def req = [
      quantity: 24,
      type: 'RETURN',
      createdDate: '2013-04-24 12:00:01 EDT',
      agentName: 'tobias'
    ]
    controller.request.json = new JsonBuilder(req).toString()

    controller.save()

    assert 422 == controller.response.status
    assert 'application/json;charset=utf-8' == controller.response.contentType
    assert '{"errors":["DELIVERY_AGENT_MISSING"]}' == controller.response.contentAsString
    assert 0 == Delivery.count()
  }

  @Test void shouldRespondWithUnprocessableWhenConstraintViolated() {
    def req = [
        quantity: null, //quantity is required
        type: 'RETURN',
        createdDate: '2013-04-24 12:00:01 EDT',
        agentName: agent.getName()
    ]
    controller.request.json = new JsonBuilder(req).toString()

    controller.save()

    assert 422 == controller.response.status
    assert 'application/json;charset=utf-8' == controller.response.contentType
    assert '{"errors":["QUANTITY_NULLABLE"]}' == controller.response.contentAsString
    assert 0 == Delivery.count()
  }

  @Test void shouldResponseWithInternalServerErrorIfException() {
    controller.deliveriesService = [saveDelivery: {-> throw new RuntimeException() }] as DeliveriesService

    controller.save()

    assert 500 == controller.response.status
    assert 'application/json;charset=utf-8' == controller.response.contentType
    assert '{"errors":["SERVER_ERROR"]}' == controller.response.contentAsString
    assert 0 == Delivery.count()
  }
}
