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
          name:  s.name,
          followupToSite: s.followupToSite?.name
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

    if(Promotion.count() == 0) {
      new Promotion(appliesTo: "BASKET", productSku: '', startDate: new Date(0), endDate: new Date(Long.MAX_VALUE), amount: 10G, type: 'PERCENT', sku: '10P_OFF_BASKET').save()
      new Promotion(appliesTo: "SKU", productSku: '2GALLON', startDate: new Date(0), endDate: new Date(Long.MAX_VALUE), amount: 1G, type: 'AMOUNT', sku: '1HTG_OFF_2GAL').save()
      new Promotion(appliesTo: "SKU", productSku: '10GALLON', startDate: new Date(0), endDate: new Date(Long.MAX_VALUE), amount: 50G, type: 'PERCENT', sku: '50P_OFF_10GAL').save()
    }

    if (Product.count() == 0) {
      new Product(sku: '2GALLON', price: new Money(amount: 5), description: "2 Gallon Jug", gallons: 2).save()
      new Product(sku: '5GALLON', price: new Money(amount: 7.5), description: "5 Gallon Jug", gallons: 5).save()
      new Product(sku: '10GALLON', price: new Money(amount: 5), description: "10 Gallon Jug", gallons: 10).save()
    }

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
  def destroy = {
  }
}
