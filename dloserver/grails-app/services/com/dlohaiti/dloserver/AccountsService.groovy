package com.dlohaiti.dloserver

class AccountsService {

    def grailsApplication

    def saveAccount(params) {
        def newAccount = false;
        Date createdDate = params.date("createdDate", grailsApplication.config.dloserver.measurement.timeformat.toString())
        CustomerAccount account =   CustomerAccount.findById(params.id)

        if(account == null) {
            log.debug "Creating new customer account"
            newAccount=true
            account = new CustomerAccount(id: params.id,kiosk: params.kiosk)
        }
        account.name=params.name
        account.contactName=params.contactName
        account.phoneNumber = params.phoneNumber
        account.dueAmount=new Double(params.dueAmount)
        account.address=params.address
        account.customerType = CustomerType.findById(params.customerTypeId)
        account.gpsCoordinates= (params.gpsCoordinates==null) ? "" : params.gpsCoordinates
        if(!newAccount) {
            def salesChannels = account.channels.toArray()
            if (salesChannels != null) {
                for (sc in salesChannels) {
                    account.removeFromChannels(sc)
                }
            }

            def sponsors = account.sponsors.toArray()
            if(sponsors !=null ) {
                for (s in sponsors) {
                    account.removeFromSponsors(s)
                }
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

        for(sId in params.sponsorIds) {
            def sponsor = Sponsor.get(sId)
            if(sponsor == null) {
                log.debug "Unable to get sponsor"
                throw new MissingSponsorException()
            }
            account.addToSponsors(sponsor)
        }
        if(!account.isAttached() && !newAccount) {
            account.attach()
        }
        account.save(flush: true)
        return account
    }
}
