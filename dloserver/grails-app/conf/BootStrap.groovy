import com.dlohaiti.dloserver.Kiosk
import com.dlohaiti.dloserver.Location
import com.dlohaiti.dloserver.Measurement
import com.dlohaiti.dloserver.MeasurementType

class BootStrap {

    def init = { servletContext ->
        new Kiosk(name: "HARDCODED K1", apiKey: 'pw').save()

        if (Measurement.count() == 0) {
            new MeasurementType(name: "Temperature", unit: "°C", min: 10, max: 30).save()
            new MeasurementType(name: "pH", unit: "", min: 5, max: 9).save()
            new MeasurementType(name: "Turbidity", unit: "NTU", min: 0, max: 10).save()
            new MeasurementType(name: "TDS", unit: "mg/L", min: 100, max: 800).save()
            new MeasurementType(name: "Free Chlorine Concentration", unit: "mg/L Cl2", min: 5000, max: 9000).save()
            new MeasurementType(name: "Total Chlorine Concentration", unit: "mg/L Cl2", min: 5000, max: 9000).save()
            new MeasurementType(name: "Free Chlorine Residual", unit: "mg/L Cl2", min: 0, max: 1).save()
            new MeasurementType(name: "Total Chlorine Residual", unit: "mg/L Cl2", min: 0, max: 1).save()
            new MeasurementType(name: "Alkalinity", unit: "mg/L CaCO3", min: 100, max: 500).save()
            new MeasurementType(name: "Hardness", unit: "mg/L CaCO3", min: 100, max: 700).save()
            new MeasurementType(name: "Color").save()
            new MeasurementType(name: "Odor").save()
            new MeasurementType(name: "Taste").save()

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
