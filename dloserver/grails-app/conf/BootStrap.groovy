import com.dlohaiti.dloserver.*
import grails.converters.JSON

class BootStrap {

  def init = { servletContext ->


    JSON.registerObjectMarshaller(Parameter) { Parameter p ->
      return [
          isOkNotOk: p.isOkNotOk,
          isUsedInTotalizer: p.isUsedInTotalizer,
          minimum: p.min,
          maximum: p.max,
          name: p.name,
          unit: p.unit,
          samplingSites: p.samplingSites
      ]
    }

    JSON.registerObjectMarshaller(SamplingSite) { SamplingSite s ->
      return [
          isUsedInTotalizer: s.isUsedForTotalizer,
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
          requiresQuantity: p.requiresQuantity,
          price: [
              amount: p.price.amount,
              currencyCode: p.price.currency.currencyCode
          ]
      ]
    }

    if (Kiosk.count() == 0) {
      new Kiosk(name: "kiosk01", apiKey: 'pw').save()
      new Kiosk(name: "kiosk02", apiKey: 'pw').save()
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

      new Parameter(name: "Temperature", unit: "°C", min: 10, max: 30, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          boreholeEarly, wtu
      ]).save()
      new Parameter(name: "pH", unit: "", min: 5, max: 9, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          boreholeEarly, fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "Turbidity", unit: "NTU", min: 0, max: 10, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          boreholeEarly, kioskCounterEarly
      ]).save()
      new Parameter(name: "Alkalinity", unit: "mg/L CaCO3 (ppm)", min: 100, max: 500, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          boreholeEarly, fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "Hardness", unit: "mg/L CaCO3 (ppm)", min: 100, max: 500, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          boreholeEarly, fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "Free Chlorine Residual", unit: "mg/L Cl2 (ppm)", min: 0, max: 1, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "Total Chlorine Residual", unit: "mg/L Cl2 (ppm)", min: 0, max: 1, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "TDS (Feed, RO, RO/UF)", unit: "mg/L (ppm)", min: 0, max: 800, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          boreholeEarly, fillstationEarly, kioskCounterEarly
      ]).save()
      new Parameter(name: "TDS Feed", unit: "mg/L (ppm)", min: 0, max: 800, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "TDS RO", unit: "mg/L (ppm)", min: 0, max: 800, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "TDS RO/UF", unit: "mg/L (ppm)", min: 0, max: 800, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Pressure Pre-Filter", unit: "PSI", min: 0, max: 100, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Pressure Post-Filter", unit: "PSI", min: 0, max: 100, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Pressure Pre-RO", unit: "PSI", min: 0, max: 300, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Pressure Post-RO", unit: "PSI", min: 0, max: 300, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Feed Flow Rate", unit: "gpm", min: null, max: null, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Product Flow Rate", unit: "gpm", min: null, max: null, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "R/O Product Flow Rate", unit: "gallons", min: null, max: null, isUsedInTotalizer: false, isOkNotOk: false, samplingSites: [
          wtu
      ]).save()
      new Parameter(name: "Gallons distributed", unit: "gallons", min: null, max: null, isUsedInTotalizer: true, isOkNotOk: false, samplingSites: [
          boreholeEarly, boreholeLate, fillstationEarly, fillstationLate, kioskCounterEarly, kioskCounterLate, bulkFillEarly, bulkFillLate,
          cleaningStationEarly, cleaningStationLate
      ]).save()
      new Parameter(name: "Color", unit: '', min: null, max: null, isUsedInTotalizer: false, isOkNotOk: true, samplingSites: [
          fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "Odor", unit: '', min: null, max: null, isUsedInTotalizer: false, isOkNotOk: true, samplingSites: [
          fillstationEarly, kioskCounterEarly, wtu
      ]).save()
      new Parameter(name: "Taste", unit: '', min: null, max: null, isUsedInTotalizer: false, isOkNotOk: true, samplingSites: [
          fillstationEarly, kioskCounterEarly, wtu
      ]).save()
  }
  def destroy = {
  }
}
