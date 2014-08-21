package com.dlohaiti.dloserver

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Kiosk {

    String name
    String apiKey

    static belongsTo = [region:Region]
    static hasMany = [sensors: Sensor,customerAccounts: CustomerAccount,productMrps: ProductMrp]

    static constraints = {
        name(blank: false, unique: true)
        apiKey(blank: false)
    }


    @Override
    public String toString() {
        name
    }

    List<SalesChannel> getSalesChannels() {
        ProductMrp.findAllByKiosk(this)*.salesChannel.unique()
    }

    List<Sponsor> getSponsors(){
        CustomerAccount.findAllByKiosk(this)*.sponsors.unique()
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
