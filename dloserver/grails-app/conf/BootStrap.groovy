import com.dlohaiti.dloserver.*
import grails.converters.JSON

import java.text.SimpleDateFormat

class BootStrap {
  def grailsApplication

  def init = { servletContext ->

    def dateFormatter = new SimpleDateFormat(grailsApplication.config.dloserver.measurement.timeformat.toString())

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
          hasRange: p.hasRange()
      ]
    }

    if (Kiosk.count() == 0) {
      new Kiosk(name: "kiosk01", apiKey: 'pw').save()
      new Kiosk(name: "kiosk02", apiKey: 'pw').save()
    }

    if (Promotion.count() == 0) {
      new Promotion(appliesTo: "BASKET", productSku: '', startDate: new Date(0), endDate: new Date(Long.MAX_VALUE), amount: 10G, type: 'PERCENT', sku: '10P_OFF_BASKET').save()
      new Promotion(appliesTo: "SKU", productSku: '2GALLON', startDate: new Date(0), endDate: new Date(Long.MAX_VALUE), amount: 1G, type: 'AMOUNT', sku: '1HTG_OFF_2GAL').save()
      new Promotion(appliesTo: "SKU", productSku: '10GALLON', startDate: new Date(0), endDate: new Date(Long.MAX_VALUE), amount: 50G, type: 'PERCENT', sku: '50P_OFF_10GAL').save()
    }

    if (Product.count() == 0) {
      new Product(sku: '2GALLON', price: new Money(amount: 5), description: "2 Gallon Jug", gallons: 2, base64EncodedImage: "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").save()
      new Product(sku: '5GALLON', price: new Money(amount: 7.5), description: "5 Gallon Jug", gallons: 5, base64EncodedImage: "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").save()
      new Product(sku: '10GALLON', price: new Money(amount: 5), description: "10 Gallon Jug", gallons: 10, base64EncodedImage: "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").save()
    }

    if (Parameter.count() == 0) {
      def boreholeEarly = new SamplingSite(name: 'Borehole - Early Day', isUsedForTotalizer: false, followupToSite: null).save()
      def boreholeLate = new SamplingSite(name: 'Borehole - Late Day', isUsedForTotalizer: false, followupToSite: boreholeEarly).save()
      def fillstationEarly = new SamplingSite(name: 'Fill Station - Early Day', isUsedForTotalizer: true, followupToSite: null).save()
      def fillstationLate = new SamplingSite(name: 'Fill Station - Late Day', isUsedForTotalizer: true, followupToSite: fillstationEarly).save()
      def kioskCounterEarly = new SamplingSite(name: 'Kiosk Counter - Early Day', isUsedForTotalizer: true, followupToSite: null).save()
      def kioskCounterLate = new SamplingSite(name: 'Kiosk Counter - Late Day', isUsedForTotalizer: true, followupToSite: kioskCounterEarly).save()
      def bulkFillEarly = new SamplingSite(name: 'Bulk Fill - Early Day', isUsedForTotalizer: false, followupToSite: null).save()
      def bulkFillLate = new SamplingSite(name: 'Bulk Fill - Late Day', isUsedForTotalizer: false, followupToSite: bulkFillEarly).save()
      def cleaningStationEarly = new SamplingSite(name: 'Cleaning Station - Early Day', isUsedForTotalizer: false, followupToSite: null).save()
      def cleaningStationLate = new SamplingSite(name: 'Cleaning Station - Late Day', isUsedForTotalizer: false, followupToSite: cleaningStationEarly).save()
      def wtu = new SamplingSite(name: 'WTU', isUsedForTotalizer: false, followupToSite: null).save()

      new Parameter(name: "Temperature", unit: "°C", minimum: 10, maximum: 30, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          boreholeEarly, wtu
      ]).save()
      new Parameter(name: "pH", unit: "", minimum: 5, maximum: 9, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          boreholeEarly, fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "Turbidity", unit: "NTU", minimum: 0, maximum: 10, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          boreholeEarly, kioskCounterEarly
      ]).save()
      new Parameter(name: "Alkalinity", unit: "mg/L CaCO3 (ppm)", minimum: 100, maximum: 500, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          boreholeEarly, fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "Hardness", unit: "mg/L CaCO3 (ppm)", minimum: 100, maximum: 500, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          boreholeEarly, fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "Free Chlorine Residual", unit: "mg/L Cl2 (ppm)", minimum: 0, maximum: 1, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "Total Chlorine Residual", unit: "mg/L Cl2 (ppm)", minimum: 0, maximum: 1, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "TDS (Feed, RO, RO/UF)", unit: "mg/L (ppm)", minimum: 0, maximum: 800, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          boreholeEarly, fillstationEarly, kioskCounterEarly
      ]).save()
      new Parameter(name: "TDS Feed", unit: "mg/L (ppm)", minimum: 0, maximum: 800, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "TDS RO", unit: "mg/L (ppm)", minimum: 0, maximum: 800, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "TDS RO/UF", unit: "mg/L (ppm)", minimum: 0, maximum: 800, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Pressure Pre-Filter", unit: "PSI", minimum: 0, maximum: 100, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Pressure Post-Filter", unit: "PSI", minimum: 0, maximum: 100, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Pressure Pre-RO", unit: "PSI", minimum: 0, maximum: 300, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Pressure Post-RO", unit: "PSI", minimum: 0, maximum: 300, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Feed Flow Rate", unit: "gpm", minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Product Flow Rate", unit: "gpm", minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "R/O Product Flow Rate", unit: "gallons", minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Gallons distributed", unit: "gallons", minimum: null, maximum: null, isUsedInTotalizer: true, isOkNotOk: false, samplingSites: [
          boreholeEarly, boreholeLate, fillstationEarly, fillstationLate, kioskCounterEarly, kioskCounterLate, bulkFillEarly, bulkFillLate,
          cleaningStationEarly, cleaningStationLate
      ]).save()
      new Parameter(name: "Color", unit: '', minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: true, samplingSites: [
          fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "Odor", unit: '', minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: true, samplingSites: [
          fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "Taste", unit: '', minimum: null, maximum: null, isUsedInTotalizer: false, isOkNotOk: true, samplingSites: [
          fillstationEarly, kioskCounterEarly, wtu
      ]).save()
    }
  }
  def destroy = {
  }
}
