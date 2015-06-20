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

    }
    def destroy = {
    }
}
