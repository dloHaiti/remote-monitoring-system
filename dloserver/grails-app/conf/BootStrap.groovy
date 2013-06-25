import com.dlohaiti.dloserver.*
import grails.converters.JSON
import grails.util.Environment
import org.joda.time.LocalDate

import java.text.SimpleDateFormat

class BootStrap {
  def grailsApplication

  def init = { servletContext ->

    def dateFormatter = new SimpleDateFormat(grailsApplication.config.dloserver.measurement.timeformat.toString())

    JSON.registerObjectMarshaller(DeliveryConfiguration) { DeliveryConfiguration c ->
      return [
          minimum: c.minimumValue,
          maximum: c.maximumValue,
          default: c.defaultValue
      ]
    }

    JSON.registerObjectMarshaller(DeliveryAgent) { DeliveryAgent d ->
      return [
          name: d.name
      ]
    }

    JSON.registerObjectMarshaller(Parameter) { Parameter p ->
      return [
          isOkNotOk: p.isOkNotOk,
          minimum: p.minimum,
          maximum: p.maximum,
          name: p.name,
          unit: p.unit,
          samplingSites: p.samplingSites
      ]
    }

    JSON.registerObjectMarshaller(SamplingSite) { SamplingSite s ->
      return [
          name: s.name
      ]
    }

    JSON.registerObjectMarshaller(Product) { Product p ->
      return [
          sku: p.sku,
          description: p.description,
          gallons: p.gallons,
          maximumQuantity: p.maximumQuantity,
          minimumQuantity: p.minimumQuantity,
          requiresQuantity: p.requiresQuantity(),
          base64EncodedImage: p.base64EncodedImage,
          price: [
              amount: p.price.amount,
              currencyCode: p.price.currency.currencyCode
          ]
      ]
    }

    JSON.registerObjectMarshaller(Promotion) { Promotion p ->
      return [
          appliesTo: p.appliesTo,
          startDate: dateFormatter.format(p.startDate),
          endDate: dateFormatter.format(p.endDate),
          productSku: p.productSku,
          amount: p.amount,
          type: p.type,
          sku: p.sku,
          hasRange: p.hasRange(),
          base64EncodedImage: p.base64EncodedImage
      ]
    }

    if (Environment.current != Environment.TEST) {
      if (Kiosk.count() == 0) {
        new Kiosk(name: "kiosk01", apiKey: 'pw').save(failOnError: true)
      }

      if (DeliveryAgent.count() == 0) {
        new DeliveryConfiguration(minimumValue: 0, maximumValue: 24, defaultValue: 24, gallons: 4, price: new Money(amount: 5G)).save(failOnError: true)
      }

      if (DeliveryAgent.count() == 0) {
        new DeliveryAgent(name: "Agent 1", kiosk: Kiosk.findByName("kiosk01"), active: true).save(failOnError: true)
        new DeliveryAgent(name: "Agent 2", kiosk: Kiosk.findByName("kiosk01"), active: true).save(failOnError: true)
      }

      if (Promotion.count() == 0) {
        new Promotion(appliesTo: "SKU", productSku: '5GALLON', startDate: new Date(0), endDate: new LocalDate(2015, 1, 1).toDate(), amount: 10G, type: 'PERCENT', sku: '10P_OFF_5GAL', base64EncodedImage: "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").save(failOnError: true)
      }

      if (Product.count() == 0) {
        new Product(active: true, sku: '5GALLON', price: new Money(amount: 100), description: "5 Gallon Jug", gallons: 5, base64EncodedImage: "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").save(failOnError: true)
      }

      if (Parameter.count() == 0) {
        def boreholeEarly = new SamplingSite(name: 'Borehole - Early Day', isUsedForTotalizer: false, followupToSite: null).save(failOnError: true)
        def boreholeLate = new SamplingSite(name: 'Borehole - Late Day', isUsedForTotalizer: false, followupToSite: boreholeEarly).save(failOnError: true)
        def fillstationEarly = new SamplingSite(name: 'Fill Station - Early Day', isUsedForTotalizer: true, followupToSite: null).save(failOnError: true)
        def fillstationLate = new SamplingSite(name: 'Fill Station - Late Day', isUsedForTotalizer: true, followupToSite: fillstationEarly).save(failOnError: true)
        def kioskCounterEarly = new SamplingSite(name: 'Kiosk Counter - Early Day', isUsedForTotalizer: true, followupToSite: null).save(failOnError: true)
        def kioskCounterLate = new SamplingSite(name: 'Kiosk Counter - Late Day', isUsedForTotalizer: true, followupToSite: kioskCounterEarly).save(failOnError: true)
        def bulkFillEarly = new SamplingSite(name: 'Bulk Fill - Early Day', isUsedForTotalizer: false, followupToSite: null).save(failOnError: true)
        def bulkFillLate = new SamplingSite(name: 'Bulk Fill - Late Day', isUsedForTotalizer: false, followupToSite: bulkFillEarly).save(failOnError: true)
        def cleaningStationEarly = new SamplingSite(name: 'Cleaning Station - Early Day', isUsedForTotalizer: false, followupToSite: null).save(failOnError: true)
        def cleaningStationLate = new SamplingSite(name: 'Cleaning Station - Late Day', isUsedForTotalizer: false, followupToSite: cleaningStationEarly).save(failOnError: true)
        def wtu = new SamplingSite(name: 'WTU', isUsedForTotalizer: false, followupToSite: null).save(failOnError: true)

        new Parameter(active: true, manual: true, name: "Temperature", unit: "°C", minimum: 10, maximum: 30, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            boreholeEarly, wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "pH", unit: "", minimum: 5, maximum: 9, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            boreholeEarly, fillstationEarly, kioskCounterEarly, wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Turbidity", unit: "NTU", minimum: 0, maximum: 10, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            boreholeEarly, kioskCounterEarly
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Alkalinity", unit: "mg/L CaCO3 (ppm)", minimum: 100, maximum: 500, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            boreholeEarly, fillstationEarly, kioskCounterEarly, wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Hardness", unit: "mg/L CaCO3 (ppm)", minimum: 100, maximum: 500, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            boreholeEarly, fillstationEarly, kioskCounterEarly, wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Free Chlorine Residual", unit: "mg/L Cl2 (ppm)", minimum: 0, maximum: 1, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            fillstationEarly, kioskCounterEarly, wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Total Chlorine Residual", unit: "mg/L Cl2 (ppm)", minimum: 0, maximum: 1, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            fillstationEarly, kioskCounterEarly, wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "TDS (Feed, RO, RO/UF)", unit: "mg/L (ppm)", minimum: 0, maximum: 800, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            boreholeEarly, fillstationEarly, kioskCounterEarly
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "TDS Feed", unit: "mg/L (ppm)", minimum: 0, maximum: 800, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "TDS RO", unit: "mg/L (ppm)", minimum: 0, maximum: 800, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "TDS RO/UF", unit: "mg/L (ppm)", minimum: 0, maximum: 800, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Pressure Pre-Filter", unit: "PSI", minimum: 0, maximum: 100, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Pressure Post-Filter", unit: "PSI", minimum: 0, maximum: 100, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Pressure Pre-RO", unit: "PSI", minimum: 0, maximum: 300, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Pressure Post-RO", unit: "PSI", minimum: 0, maximum: 300, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Feed Flow Rate", unit: "gpm", minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Product Flow Rate", unit: "gpm", minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "R/O Product Flow Rate", unit: "gallons", minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
            wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Gallons distributed", unit: "gallons", minimum: null, maximum: null, isUsedInTotalizer: true, isOkNotOk: false, samplingSites: [
            boreholeEarly, boreholeLate, fillstationEarly, fillstationLate, kioskCounterEarly, kioskCounterLate, bulkFillEarly, bulkFillLate,
            cleaningStationEarly, cleaningStationLate
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Color", unit: '', minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: true, samplingSites: [
            fillstationEarly, kioskCounterEarly, wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Odor", unit: '', minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: true, samplingSites: [
            fillstationEarly, kioskCounterEarly, wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: true, name: "Taste", unit: '', minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: true, samplingSites: [
            fillstationEarly, kioskCounterEarly, wtu
        ]).save(failOnError: true)
        new Parameter(active: true, manual: false, name: "kWh", unit: 'kWh', minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)
        new Parameter(active: true, manual: false, name: "Float Time (h:m)", unit: 'minutes', minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)
        new Parameter(active: true, manual: false, name: "High Power (kW)", unit: 'kW', minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)
        new Parameter(active: true, manual: false, name: "High Temp (C)", unit: 'C', minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)
        new Parameter(active: true, manual: false, name: "Vin", unit: '', minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)
        new Parameter(active: true, manual: false, name: "Vbatt", unit: '', minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: false).save(failOnError: true)
      }
    }
  }
  def destroy = {
  }
}
