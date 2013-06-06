import com.dlohaiti.dloserver.*

class BootStrap {

    def init = { servletContext ->
        new Kiosk(name: "kiosk01", apiKey: 'pw').save()
        new Kiosk(name: "kiosk02", apiKey: 'pw').save()

        new Product(sku: '2GALLON', price: new Money(amount: 5), description: "2 Gallon Jug", gallons: 2, reportingCategory: "NEWJUG").save()
        new Product(sku: '5GALLON', price: new Money(amount: 7.5), description: "5 Gallon Jug", gallons: 5, reportingCategory: "NEWJUG").save()
        new Product(sku: '10GALLON', price: new Money(amount: 5), description: "10 Gallon Jug", gallons: 10, reportingCategory: "NEWJUG").save()

        if (Measurement.count() == 0) {
            new Parameter(name: "Temperature", unit: "°C", min: 10, max: 30).save()
            new Parameter(name: "Gallons distributed", unit: "gallons", min: 0, max: 10000).save()
            new Parameter(name: "pH", unit: "", min: 5, max: 9).save()
            new Parameter(name: "Turbidity", unit: "NTU", min: 0, max: 10).save()
            new Parameter(name: "TDS Feed", unit: "mg/L", min: 0, max: 800).save()
            new Parameter(name: "TDS RO", unit: "mg/L", min: 0, max: 800).save()
            new Parameter(name: "TDS RO/UF", unit: "mg/L", min: 0, max: 800).save()
            new Parameter(name: "TDS (Feed, RO, RO/UF)", unit: "mg/L", min: 0, max: 800).save()
            new Parameter(name: "Free Chlorine Concentration", unit: "mg/L Cl2", min: 5000, max: 9000).save()
            new Parameter(name: "Total Chlorine Concentration", unit: "mg/L Cl2", min: 5000, max: 9000).save()
            new Parameter(name: "Free Chlorine Residual", unit: "mg/L Cl2", min: 0, max: 1).save()
            new Parameter(name: "Total Chlorine Residual", unit: "mg/L Cl2", min: 0, max: 1).save()
            new Parameter(name: "Alkalinity", unit: "mg/L CaCO3", min: 100, max: 500).save()
            new Parameter(name: "Hardness", unit: "mg/L CaCO3", min: 100, max: 700).save()
            new Parameter(name: "Color").save()
            new Parameter(name: "Odor").save()
            new Parameter(name: "Taste").save()

        }
        if (Location.count() == 0) {
            new Location(name: 'Borehole').save()
            new Location(name: 'WTU Eff').save()
            new Location(name: 'WTU Feed').save()
        }
    }
    def destroy = {
    }
}
