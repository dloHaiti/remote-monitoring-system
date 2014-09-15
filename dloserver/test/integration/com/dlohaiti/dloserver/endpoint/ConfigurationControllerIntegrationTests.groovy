package com.dlohaiti.dloserver.endpoint
import com.dlohaiti.dloserver.*
import grails.converters.JSON
import org.junit.Before
import org.junit.Test

class ConfigurationControllerIntegrationTests {

  def controller = new ConfigurationController()
  def kiosk
def country
def region
  @Before void setUp() {
      if(Country.count()==0){
          country =new Country(name: "haiti").save(failOnError: true)
      }else{
          country =Country.first()
      }

      if(Region.count()==0){
          region=  new Region(name: "Region1",country: country).save(failOnError: true)
      }else{
          region=Region.first()
      }

      kiosk = new Kiosk(name: 'testkiosk', apiKey: 'pw',region:region).save(failOnError: true)
    controller.request.contentType = 'application/json'
    controller.request.kiosk = kiosk
  }

  @Test void shouldOnlyReturnActiveDeliveryAgentsForKiosk() {
      def differentKiosk = new Kiosk(name: 'nonmatch', apiKey: 'pw',region:region).save(failOnError: true)
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
    def category = new ProductCategory(name: "Category1").save(failOnError: true)
    def one = new Product(sku: 'ACTIVE-ABC', active: true, description: 'abc', gallons: 1, price: new Money(amount: 10G), base64EncodedImage: 'abc',category: category).save(failOnError: true)
    def two = new Product(sku: 'ACTIVE-XYZ', active: true, description: 'xyz', gallons: 5, price: new Money(amount: 1G), base64EncodedImage: 'xyz',category: category).save(failOnError: true)
    new Product(sku: 'INACTIVE-LLL', active: false, description: 'lll', gallons: 10, price: new Money(amount: 5G), base64EncodedImage: 'lll',category: category).save(failOnError: true)

    controller.index()

    def products = JSON.parse(controller.response.contentAsString).products
    assert 2 == products.size()
    assert [[sku: one.sku], [sku: two.sku]] == products.collect({ p -> [sku: p.sku] })
  }

  @Test void shouldOnlyReturnActiveAndManualParameters() {
      new SamplingSite(name: "site1",isUsedForTotalizer: true).save(failOnError: true)
    def one = new Parameter(name: 'manual-active', manual: true, active: true, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)
    def two = new Parameter(name: 'manual-inactive', manual: true, active: false, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)
    def three = new Parameter(name: 'automatic-active', manual: false, active: true, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)
    def four = new Parameter(name: 'automatic-inactive', manual: false, active: false, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)

    new KioskWiseParameter(kiosk: Kiosk.first(),parameter: one,samplingSite: SamplingSite.first()).save(failOnError: true)
    new KioskWiseParameter(kiosk: Kiosk.first(),parameter: two,samplingSite: SamplingSite.first()).save(failOnError: true)
    new KioskWiseParameter(kiosk: Kiosk.first(),parameter: three,samplingSite: SamplingSite.first()).save(failOnError: true)
    new KioskWiseParameter(kiosk: Kiosk.first(),parameter: four,samplingSite: SamplingSite.first()).save(failOnError: true)

    controller.index()

    def parameters = JSON.parse(controller.response.contentAsString).parameters
    assert 1 == parameters.size()
    assert [[name: one.name]] == parameters.collect({p -> [name: p.name]})
  }
    @Test void shouldOnlyReturnRebatesBasedtoKioskRegion(){

        def category = new ProductCategory(name: "Category1").save(failOnError: true)
        def one = new Product(sku: 'ACTIVE-ABC', active: true, description: 'abc', gallons: 1, price: new Money(amount: 10G), base64EncodedImage: 'abc',category: category).save(failOnError: true)

        def differentRegion = new Region(name: "region2",country: country).save(failOnError: true)

       def  rebate = new Rebate(name:"Rebate1", noOfSkus: 100, transactionType: "Monthly", noOfFreeSkus: 10, product: one).addToRegions(region).save(failOnError: true)
        new Rebate(name:"Rebate2",noOfSkus: 100, transactionType: "Monthly", noOfFreeSkus: 10, product: one).addToRegions(differentRegion).save(failOnError: true)

        controller.index()

        def rebates = JSON.parse(controller.response.contentAsString).rebates
        assert 1 == rebates.size()
        assert [[name: rebate.name]] == rebates.collect({r -> [name: r.name]})
    }
}
