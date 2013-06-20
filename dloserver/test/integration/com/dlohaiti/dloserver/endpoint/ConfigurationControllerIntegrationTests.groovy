package com.dlohaiti.dloserver.endpoint
import com.dlohaiti.dloserver.DeliveryAgent
import com.dlohaiti.dloserver.Kiosk
import com.dlohaiti.dloserver.Money
import com.dlohaiti.dloserver.Parameter
import com.dlohaiti.dloserver.Product
import grails.converters.JSON
import org.junit.Before
import org.junit.Test

class ConfigurationControllerIntegrationTests {

  def controller = new ConfigurationController()
  def kiosk

  @Before void setUp() {
    kiosk = new Kiosk(name: 'testkiosk', apiKey: 'pw').save(failOnError: true)
    controller.request.contentType = 'application/json'
    controller.request.kiosk = kiosk
  }

  @Test void shouldOnlyReturnActiveDeliveryAgentsForKiosk() {
    def differentKiosk = new Kiosk(name: 'nonmatch', apiKey: 'pw').save(failOnError: true)
    def one = new DeliveryAgent(name: 'matching-active-agent1', active: true, kiosk: kiosk).save(failOnError: true)
    def two = new DeliveryAgent(name: 'matching-active-agent2', active: true, kiosk: kiosk).save(failOnError: true)
    new DeliveryAgent(name: 'matching-inactive-agent3', active: false, kiosk: kiosk).save(failOnError: true)
    new DeliveryAgent(name: 'nonmatching-inactive-agent4', active: false, kiosk: differentKiosk).save(failOnError: true)
    new DeliveryAgent(name: 'nonmatching-active-agent2', active: true, kiosk: differentKiosk).save(failOnError: true)

    controller.index()

    def agents = JSON.parse(controller.response.contentAsString).delivery.agents
    assert 2 == agents.size()
    assert [[name: one.name], [name: two.name]] == agents
  }

  @Test void shouldOnlyReturnActiveProducts() {
    def one = new Product(sku: 'ACTIVE-ABC', active: true, description: 'abc', gallons: 1, price: new Money(amount: 10G), base64EncodedImage: 'abc').save(failOnError: true)
    def two = new Product(sku: 'ACTIVE-XYZ', active: true, description: 'xyz', gallons: 5, price: new Money(amount: 1G), base64EncodedImage: 'xyz').save(failOnError: true)
    new Product(sku: 'INACTIVE-LLL', active: false, description: 'lll', gallons: 10, price: new Money(amount: 5G), base64EncodedImage: 'lll').save(failOnError: true)

    controller.index()

    def products = JSON.parse(controller.response.contentAsString).products
    assert 2 == products.size()
    assert [[sku: one.sku], [sku: two.sku]] == products.collect({ p -> [sku: p.sku] })
  }

  @Test void shouldOnlyReturnActiveAndManualParameters() {
    def one = new Parameter(name: 'manual-active', manual: true, active: true, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)
    new Parameter(name: 'manual-inactive', manual: true, active: false, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)
    new Parameter(name: 'automatic-active', manual: false, active: true, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)
    new Parameter(name: 'automatic-inactive', manual: false, active: false, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)

    controller.index()

    def parameters = JSON.parse(controller.response.contentAsString).parameters
    assert 1 == parameters.size()
    assert [[name: one.name]] == parameters.collect({p -> [name: p.name]})
  }
}
