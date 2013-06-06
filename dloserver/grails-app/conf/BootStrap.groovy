import com.dlohaiti.dloserver.*

class BootStrap {

  def init = { servletContext ->
    new Kiosk(name: "kiosk01", apiKey: 'pw').save()
    new Kiosk(name: "kiosk02", apiKey: 'pw').save()

    new Product(sku: '2GALLON', price: new Money(amount: 5), description: "2 Gallon Jug", gallons: 2).save()
    new Product(sku: '5GALLON', price: new Money(amount: 7.5), description: "5 Gallon Jug", gallons: 5).save()
    new Product(sku: '10GALLON', price: new Money(amount: 5), description: "10 Gallon Jug", gallons: 10).save()

    if (Parameter.count() == 0) {
      new Parameter(name: "Temperature",                  unit: "°C",         min: 10,   max: 30,   isUsedInTotalizer: false).save()
      new Parameter(name: "Gallons distributed",          unit: "gallons",    min: null, max: null, isUsedInTotalizer: true).save()
      new Parameter(name: "pH",                           unit: "",           min: 5,    max: 9,    isUsedInTotalizer: false).save()
      new Parameter(name: "Turbidity",                    unit: "NTU",        min: 0,    max: 10,   isUsedInTotalizer: false).save()
      new Parameter(name: "TDS Feed",                     unit: "mg/L",       min: 0,    max: 800,  isUsedInTotalizer: false).save()
      new Parameter(name: "TDS RO",                       unit: "mg/L",       min: 0,    max: 800,  isUsedInTotalizer: false).save()
      new Parameter(name: "TDS RO/UF",                    unit: "mg/L",       min: 0,    max: 800,  isUsedInTotalizer: false).save()
      new Parameter(name: "TDS (Feed, RO, RO/UF)",        unit: "mg/L",       min: 0,    max: 800,  isUsedInTotalizer: false).save()
      new Parameter(name: "Free Chlorine Concentration",  unit: "mg/L Cl2",   min: 5000, max: 9000, isUsedInTotalizer: false).save()
      new Parameter(name: "Total Chlorine Concentration", unit: "mg/L Cl2",   min: 5000, max: 9000, isUsedInTotalizer: false).save()
      new Parameter(name: "Free Chlorine Residual",       unit: "mg/L Cl2",   min: 0,    max: 1,    isUsedInTotalizer: false).save()
      new Parameter(name: "Total Chlorine Residual",      unit: "mg/L Cl2",   min: 0,    max: 1,    isUsedInTotalizer: false).save()
      new Parameter(name: "Alkalinity",                   unit: "mg/L CaCO3", min: 100,  max: 500,  isUsedInTotalizer: false).save()
      new Parameter(name: "Hardness",                     unit: "mg/L CaCO3", min: 100,  max: 700,  isUsedInTotalizer: false).save()
      new Parameter(name: "Color",                        unit: '',           min: null, max: null, isUsedInTotalizer: false).save()
      new Parameter(name: "Odor",                         unit: '',           min: null, max: null, isUsedInTotalizer: false).save()
      new Parameter(name: "Taste",                        unit: '',           min: null, max: null, isUsedInTotalizer: false).save()
    }
    if(SamplingSite.count() == 0) {
      def boreholeEarly = new SamplingSite(name: 'Borehole - Early Day', isUsedForTotalizer: false, followupToSite: null).save()
      new SamplingSite(name: 'Borehole - Late Day', isUsedForTotalizer: false, followupToSite: boreholeEarly).save()
      def fillstationEarly = new SamplingSite(name: 'Fill Station - Early Day', isUsedForTotalizer: true, followupToSite: null).save()
      new SamplingSite(name: 'Fill Station - Late Day', isUsedForTotalizer: true, followupToSite: fillstationEarly).save()
      def kioskCounterEarly = new SamplingSite(name: 'Kiosk Counter - Early Day', isUsedForTotalizer: true, followupToSite: null).save()
      new SamplingSite(name: 'Kiosk Counter - Late Day', isUsedForTotalizer: true, followupToSite: kioskCounterEarly).save()
      def bulkFillEarly = new SamplingSite(name: 'Bulk Fill - Early Day', isUsedForTotalizer: false, followupToSite: null).save()
      new SamplingSite(name: 'Bulk Fill - Late Day', isUsedForTotalizer: false, followupToSite: bulkFillEarly).save()
      def cleaningStationEarly = new SamplingSite(name: 'Cleaning Station - Early Day', isUsedForTotalizer: false, followupToSite: null).save()
      new SamplingSite(name: 'Cleaning Station - Late Day', isUsedForTotalizer: false, followupToSite: cleaningStationEarly).save()
      new SamplingSite(name: 'WTU', isUsedForTotalizer: false, followupToSite: null).save()
    }
  }
  def destroy = {
  }
}
