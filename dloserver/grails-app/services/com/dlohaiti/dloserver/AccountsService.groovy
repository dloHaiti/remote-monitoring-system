package com.dlohaiti.dloserver

class AccountsService {

    def grailsApplication

    def saveAccount(params) {
        Date createdDate = params.date("createdDate", grailsApplication.config.dloserver.measurement.timeformat.toString())
        CustomerAccount account =   CustomerAccount.findById(params.id)

        if(account == null) {
            throw new Exception()
        }
        account.name=params.name
        account.contactName=params.contactName
        account.phoneNumber = params.phoneNumber
        account.dueAmount=new Double(params.dueAmount)
        account.customerType = CustomerType.findById(params.customerTypeId)
        account.gpsCoordinates= (params.gpsCoordinates==null) ? "" : params.gpsCoordinates
        def salesChannels = account.channels.toArray()
        if(salesChannels !=null ) {
            for (sc in salesChannels) {
                account.removeFromChannels(sc)
            }
        }
        for(sc in params.channels) {
          def channel = SalesChannel.get(sc.id)
            if(channel == null) {
                log.debug "Unable to get sales channel"
                throw new MissingSalesChannelException()
            }
          account.addToChannels(channel)
        }
        def sponsors = account.sponsors.toArray()
        if(sponsors !=null ) {
            for (s in sponsors) {
                account.removeFromSponsors(s)
            }
        }
        for(sId in params.sponsorIds) {
            def sponsor = Sponsor.get(sId)
            if(sponsor == null) {
                log.debug "Unable to get sponsor"
                throw new MissingSponsorException()
            }
            account.addToSponsors(sponsor)
        }
        if(!account.isAttached()) {
            account.attach()
        }
        account.save(flush: true)
        return account
    }
}
