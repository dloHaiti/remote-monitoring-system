package com.dlohaiti.dloserver

class SponsorsService {

    def grailsApplication

    def saveAccount(params) {
        def newSponsor = false
        Sponsor sponsor = Sponsor.findById(params.id)
        if (sponsor == null) {
            log.debug "Creating new sponsor"
            newSponsor = true
            sponsor = new Sponsor(id: params.id,kiosk: params.kiosk)
        }
        sponsor.name = params.name
        sponsor.contactName = params.contactName
        sponsor.phoneNumber = params.phoneNumber
        if (!newSponsor) {
            def accounts = sponsor.accounts.toArray()
            if (accounts != null) {
                for (a in accounts) {
                    sponsor.removeFromAccounts(a)
                }
            }
        }
        for (accountId in params.customerAccountIds) {
            def account = CustomerAccount.get(accountId)
            if (account == null) {
                log.debug "Unable to get sales channel"
                continue
            }
            sponsor.addToAccounts(account)
        }

        if(!sponsor.isAttached() && !newSponsor) {
            sponsor.attach()
        }
        sponsor.save(flush: true)
        return sponsor
    }
}
