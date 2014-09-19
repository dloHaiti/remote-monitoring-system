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
        account.save(flush: true)
        return account
    }
}
