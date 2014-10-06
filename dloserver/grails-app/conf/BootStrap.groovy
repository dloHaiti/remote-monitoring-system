import com.dlohaiti.dloserver.*
import grails.converters.JSON
import grails.util.Environment
import org.joda.time.DateTime
import org.joda.time.LocalDate

import java.text.SimpleDateFormat

import static java.util.Arrays.asList

class BootStrap {
    def grailsApplication

    def init = { servletContext ->
        def locale = new Locale(grailsApplication.config.dloserver.locale.language.toString(), grailsApplication.config.dloserver.locale.country.toString())
        def dateFormatter = new SimpleDateFormat(grailsApplication.config.dloserver.measurement.timeformat.toString(), locale)

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
                    isOkNotOk        : p.isOkNotOk,
                    minimum          : p.minimum,
                    maximum          : p.maximum,
                    name             : p.name,
                    unit             : p.unit,
                    priority         : p.priority,
                    isUsedInTotalizer: p.isUsedInTotalizer,
                    samplingSites    : p.samplingSites
            ]
        }

        JSON.registerObjectMarshaller(SamplingSite) { SamplingSite s ->
            return [
                    name: s.name
            ]
        }
        JSON.registerObjectMarshaller(SalesChannel) { SalesChannel sc ->
            return [
                    id             : sc.id,
                    name           : sc.name,
                    description    : sc.description,
                    delayedDelivery: sc.delayedDelivery
            ]
        }
        JSON.registerObjectMarshaller(CustomerType) { CustomerType t ->
            return [
                    id  : t.id,
                    name: t.name
            ]
        }
        JSON.registerObjectMarshaller(CustomerAccount) { CustomerAccount a ->
            return [
                    id            : a.id,
                    name          : a.name,
                    contactName   : a.contactName,
                    address       : a.address,
                    phoneNumber   : a.phoneNumber,
                    kiosk_id      : a.kiosk.id,
                    dueAmount     : a.dueAmount,
                    customerType  : a.customerType.id,
                    gpsCoordinates: a.gpsCoordinates,
                    sponsors      : a.sponsors.collect {
                        Sponsor s -> [id: s.id]
                    },
                    channels      : a.channels.collect {
                        SalesChannel channel -> [id: channel.id]
                    }
            ]
        }
        JSON.registerObjectMarshaller(ProductCategory) { ProductCategory c ->
            return [
                    id  : c.id,
                    name: c.name
            ]
        }

        JSON.registerObjectMarshaller(Sponsor) { Sponsor s ->
            return [
                    id         : s.id,
                    name       : s.name,
                    contactName: s.contactName,
                    phoneNumber: s.phoneNumber
            ]
        }

        JSON.registerObjectMarshaller(Product) { Product p ->
            return [
                    id                : p.id,
                    sku               : p.sku,
                    description       : p.description,
                    gallons           : p.gallons,
                    maximumQuantity   : p.maximumQuantity,
                    minimumQuantity   : p.minimumQuantity,
                    requiresQuantity  : p.requiresQuantity(),
                    base64EncodedImage: p.base64EncodedImage,
                    price             : [
                            amount      : p.price.amount,
                            currencyCode: p.price.currency.currencyCode
                    ],
                    category          : p.category.id
            ]
        }

        JSON.registerObjectMarshaller(ProductMrp) { ProductMrp p ->
            return [
                    kiosk_id  : p.kiosk.id,
                    product_id: p.product.id,
                    channel_id: p.salesChannel.id,
                    price     : [
                            amount      : p.price.amount,
                            currencyCode: p.price.currency.currencyCode
                    ]
            ]
        }

        JSON.registerObjectMarshaller(Promotion) { Promotion p ->
            return [
                    appliesTo         : p.appliesTo,
                    startDate         : dateFormatter.format(p.startDate),
                    endDate           : dateFormatter.format(p.endDate),
                    productSku        : p.productSku,
                    amount            : p.amount,
                    type              : p.type,
                    sku               : p.sku,
                    hasRange          : p.hasRange(),
                    base64EncodedImage: p.base64EncodedImage
            ]
        }

        if (Environment.current != Environment.TEST) {
            if (User.count() == 0) {
                new Operator(userName: "operator1", password: "password").save(failOnError: true)
                def c1 = new Country(name: "haiti", unitOfMeasure: "gallon").save(failOnError: true)
                new Region(name: "Region1", country: c1).save(failOnError: true)
            }
            if (Kiosk.count() == 0) {
                new Kiosk(name: "kiosk01", apiKey: 'pw', region: Region.first()).save(failOnError: true)
            }

            if (SalesChannel.count() == 0) {
                new SalesChannel(name: 'Over the Counter', description: 'Over the Counter', discountType: "AMOUNT", discountAmount: 12).save(failOnError: true)
                new SalesChannel(name: 'Door delivery', description: 'door delivery', discountType: "AMOUNT", discountAmount: 12, delayedDelivery: true).save(failOnError: true)
            }


            if (CustomerAccount.count() == 0) {
                CustomerType type = (new CustomerType(name: "School")).save(failOnError: true, flush: true)
                new CustomerAccount(id: "7878", name: "Customer1", contactName: 'contact1', customerType: type, kiosk: Kiosk.first()).addToChannels(SalesChannel.first()).save(failOnError: true, flush: true)
                new CustomerAccount(id: "7879", name: "Customer2", contactName: 'contact2', dueAmount: 4, customerType: type, kiosk: Kiosk.first()).addToChannels(SalesChannel.first()).save(failOnError: true, flush: true)
                new CustomerAccount(id: "9099", name: "Customer3", contactName: 'contact3', customerType: type, kiosk: Kiosk.first()).addToChannels(SalesChannel.last()).save(failOnError: true, flush: true)
                new CustomerAccount(id: "909x", name: "Customer4", contactName: 'contact4', dueAmount: 10, customerType: type, kiosk: Kiosk.first()).addToChannels(SalesChannel.last()).save(failOnError: true, flush: true)
            }

            if (Sponsor.count() == 0) {
                new Sponsor(id: "5678", name: "sponsor1", contactName: 'contact1', kiosk: Kiosk.first()).addToAccounts(CustomerAccount.first()).save(failOnError: true)
                new Sponsor(id: "5678sdsd", name: "sponsor2", contactName: 'contact1', kiosk: Kiosk.first()).addToAccounts(CustomerAccount.first()).save(failOnError: true)
            }

            if (DeliveryAgent.count() == 0) {
                new DeliveryConfiguration(minimumValue: 0, maximumValue: 24, defaultValue: 24, gallons: 4, price: new Money(amount: 5G)).save(failOnError: true)
            }

            if (DeliveryAgent.count() == 0) {
                new DeliveryAgent(name: "Agent 1", kiosk: Kiosk.findByName("kiosk01"), active: true).save(failOnError: true)
                new DeliveryAgent(name: "Agent 2", kiosk: Kiosk.findByName("kiosk01"), active: true).save(failOnError: true)
            }

            if (Promotion.count() == 0) {
                new Promotion(appliesTo: "SKU", productSku: '5GALLON', startDate: new Date(0), endDate: new LocalDate(2015, 1, 1).toDate(), amount: 10G, type: 'PERCENT', sku: '10P_OFF_5GAL', base64EncodedImage: "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").addToRegions(Region.first()).save(failOnError: true)
            }

            if (ProductCategory.count() == 0) {
                def category = new ProductCategory(name: "Category1", base64EncodedImage: "").save(failOnError: true)
                new ProductCategory(name: "Category2", base64EncodedImage: "").save(failOnError: true)
            }

            if (Product.count() == 0) {
                new Product(category: ProductCategory.first(), active: true, sku: '5GALLON', price: new Money(amount: 100), description: "5 Gallon Jug", gallons: 5, base64EncodedImage: "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").save(failOnError: true)
                new Product(category: ProductCategory.first(), active: true, sku: '10CAN', price: new Money(amount: 10), description: "10 Gallon Can", gallons: 10, base64EncodedImage: "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").save(failOnError: true)
                new Product(category: ProductCategory.first(), active: true, sku: 'DGROS', price: new Money(amount: 20), description: "Distribution Grossiste", gallons: 5, base64EncodedImage: "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TUw6Y29tLmFkb2JlLnhtcAAAAAAAPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iWE1QIENvcmUgNS4xLjIiPgogICA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczp0aWZmPSJodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyI+CiAgICAgICAgIDx0aWZmOlJlc29sdXRpb25Vbml0PjE8L3RpZmY6UmVzb2x1dGlvblVuaXQ+CiAgICAgICAgIDx0aWZmOkNvbXByZXNzaW9uPjU8L3RpZmY6Q29tcHJlc3Npb24+CiAgICAgICAgIDx0aWZmOlhSZXNvbHV0aW9uPjcyPC90aWZmOlhSZXNvbHV0aW9uPgogICAgICAgICA8dGlmZjpPcmllbnRhdGlvbj4xPC90aWZmOk9yaWVudGF0aW9uPgogICAgICAgICA8dGlmZjpZUmVzb2x1dGlvbj43MjwvdGlmZjpZUmVzb2x1dGlvbj4KICAgICAgPC9yZGY6RGVzY3JpcHRpb24+CiAgICAgIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiCiAgICAgICAgICAgIHhtbG5zOmV4aWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20vZXhpZi8xLjAvIj4KICAgICAgICAgPGV4aWY6UGl4ZWxYRGltZW5zaW9uPjE0NDwvZXhpZjpQaXhlbFhEaW1lbnNpb24+CiAgICAgICAgIDxleGlmOkNvbG9yU3BhY2U+MTwvZXhpZjpDb2xvclNwYWNlPgogICAgICAgICA8ZXhpZjpQaXhlbFlEaW1lbnNpb24+MTQ0PC9leGlmOlBpeGVsWURpbWVuc2lvbj4KICAgICAgPC9yZGY6RGVzY3JpcHRpb24+CiAgICAgIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiCiAgICAgICAgICAgIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyI+CiAgICAgICAgIDxk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").save(failOnError: true)
                new Product(category: ProductCategory.first(), active: true, sku: 'DDIST', price: new Money(amount: 20), description: "Distribution Distributeur", gallons: 5, base64EncodedImage: "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TUw6Y29tLmFkb2JlLnhtcAAAAAAAPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iWE1QIENvcmUgNS4xLjIiPgogICA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczp0aWZmPSJodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyI+CiAgICAgICAgIDx0aWZmOlJlc29sdXRpb25Vbml0PjE8L3RpZmY6UmVzb2x1dGlvblVuaXQ+CiAgICAgICAgIDx0aWZmOkNvbXByZXNzaW9uPjU8L3RpZmY6Q29tcHJlc3Npb24+CiAgICAgICAgIDx0aWZmOlhSZXNvbHV0aW9uPjcyPC90aWZmOlhSZXNvbHV0aW9uPgogICAgICAgICA8dGlmZjpPcmllbnRhdGlvbj4xPC90aWZmOk9yaWVudGF0aW9uPgogICAgICAgICA8dGlmZjpZUmVzb2x1dGlvbj43MjwvdGlmZjpZUmVzb2x1dGlvbj4KICAgICAgPC9yZGY6RGVzY3JpcHRpb24+CiAgICAgIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiCiAgICAgICAgICAgIHhtbG5zOmV4aWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20vZXhpZi8xLjAvIj4KICAgICAgICAgPGV4aWY6UGl4ZWxYRGltZW5zaW9uPjE0NDwvZXhpZjpQaXhlbFhEaW1lbnNpb24+CiAgICAgICAgIDxleGlmOkNvbG9yU3BhY2U+MTwvZXhpZjpDb2xvclNwYWNlPgogICAgICAgICA8ZXhpZjpQaXhlbFlEaW1lbnNpb24+MTQ0PC9leGlmOlBpeGVsWURpbWVuc2lvbj4KICAgICAgPC9yZGY6RGVzY3JpcHRpb24+CiAgICAgIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiCiAgICAgICAgICAgIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyI+CiAgICAgICAgIDxk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").save(failOnError: true)
            }

            if(ProductMrp.count()==0){
                new ProductMrp(kiosk: Kiosk.first(),salesChannel: SalesChannel.first(),product: Product.first(), price: new Money(amount: 2)).save(failOnError: true)
                new ProductMrp(kiosk: Kiosk.first(),salesChannel: SalesChannel.last(),product: Product.first(), price: new Money(amount: 3)).save(failOnError: true)
                new ProductMrp(kiosk: Kiosk.first(),salesChannel: SalesChannel.last(),product: Product.last(), price: new Money(amount: 3)).save(failOnError: true)
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

            if (KioskWiseParameter.count() == 0) {
                new KioskWiseParameter(kiosk: Kiosk.first(), samplingSite: SamplingSite.first(), parameter: Parameter.get(1)).save(failOnError: true)
                new KioskWiseParameter(kiosk: Kiosk.first(), samplingSite: SamplingSite.first(), parameter: Parameter.get(2)).save(failOnError: true)
                new KioskWiseParameter(kiosk: Kiosk.first(), samplingSite: SamplingSite.first(), parameter: Parameter.get(19)).save(failOnError: true)
            }

            if (Receipt.count() == 0) {
                new ReceiptLineItem(sku: "5GALLON", quantity: 1, type: "WATER", price: 20, currencyCode: "HTG", gallons: 10).save()
                new ReceiptLineItem(sku: "10CAN", quantity: 1, type: "TYPE-1", price: 30, currencyCode: "HTG", gallons: 20).save()
                new Receipt(createdDate: DateTime.now().minusDays(2).toDate(),
                        kiosk: Kiosk.first(),
                        totalGallons: 10,
                        total: 20,
                        currencyCode: "HTG",
                        receiptLineItems: asList(ReceiptLineItem.first(), ReceiptLineItem.last()),
                        salesChannel: SalesChannel.first(),
                        customerAccount: CustomerAccount.first(),
                        paymentMode: "Mode",
                        isSponsorSelected: true,
                        sponsor: Sponsor.first(),
                        sponsorAmount: 10,
                        customerAmount: 10,
                        paymentType: "CC",
                        deliveryTime: DateTime.now().minusDays(1).toString()).save(failOnError: true)
                new Receipt(createdDate: DateTime.now().minusDays(2).toDate(),
                        kiosk: Kiosk.first(),
                        totalGallons: 30,
                        total: 60,
                        currencyCode: "HTG",
                        receiptLineItems: asList(ReceiptLineItem.first(), ReceiptLineItem.last()),
                        salesChannel: SalesChannel.last(),
                        customerAccount: CustomerAccount.first(),
                        paymentMode: "Mode",
                        isSponsorSelected: true,
                        sponsor: Sponsor.first(),
                        sponsorAmount: 30,
                        customerAmount: 30,
                        paymentType: "CC",
                        deliveryTime: DateTime.now().minusDays(1).toString()).save(failOnError: true)
            }
        }
    }
    def destroy = {
    }
}
