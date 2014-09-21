package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode


@EqualsAndHashCode
class Kiosk {

    String name
    String apiKey

    static belongsTo = [region:Region]
    static hasMany = [sensors: Sensor,sponsors: Sponsor,customerAccounts: CustomerAccount,productMrps: ProductMrp,kioskWiseParameters: KioskWiseParameter]

    static constraints = {
        name(blank: false, unique: true)
        apiKey(blank: false)
    }


    @Override
    public String toString() {
        name
    }

    List<SamplingSite> getSamplingSites(Parameter p) {
        KioskWiseParameter.findAllByKioskAndParameter(this,p)*.samplingSite
    }

    List<Parameter> getParameters() {
       def parameters = KioskWiseParameter.findAllByKiosk(this)*.getActiveAndManualParameter().unique()
        parameters.removeAll([null])
        return parameters
    }

    List<SalesChannel> getSalesChannels() {
        ProductMrp.findAllByKiosk(this)*.salesChannel.unique()
    }

    List<Promotion> getPromotions(){
        def promotions = this.region.promotions
        return (promotions != null ) ? promotions.toArray() : []
    }
    List<Rebate> getRebates(){
        def rebates = this.region.rebates
        return (rebates != null ) ? rebates.toArray() : []
    }
}
