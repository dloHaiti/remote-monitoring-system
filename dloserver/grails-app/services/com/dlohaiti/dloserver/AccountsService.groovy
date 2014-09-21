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
        account.save(flush: true)
        return account
    }
}
