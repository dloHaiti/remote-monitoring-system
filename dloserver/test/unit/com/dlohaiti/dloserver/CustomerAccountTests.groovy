package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(CustomerAccount)
class CustomerAccountTests {

    void testConstraints() {

        CustomerAccount account = new CustomerAccount()
        mockForConstraintsTests(CustomerAccount,[account])

        assert !account.validate()
        assert account.errors['contactName'] != null
        assertNull  account.errors['name']

        account.contactName="name1"
        assert !account.validate()
        assertNull account.errors['contactName']
        assertNull account.errors['name']

        account.contactName=null
        account.name="name2"
        assert !account.validate()
        assertNull account.errors['contactName']
        assertNull account.errors['name']

    }

    void testValidations(){
        mockForConstraintsTests(CustomerAccount)
        mockForConstraintsTests(Kiosk)
        mockForConstraintsTests(CustomerType)
        Kiosk k1 = new Kiosk(name: "k4",apiKey: "sampleKey")
        CustomerType type=(new CustomerType(name:"Schools"))

        CustomerAccount account = (new CustomerAccount(name: "Customer2",customerType: type,kiosk: k1))
        assert account.validate()

        CustomerAccount account2 = (new CustomerAccount(name: "Customer2",customerType: type,kiosk: k1))
        mockForConstraintsTests(CustomerAccount,[account,account2])
        assert !account2.validate()
    }

    void testCustomerTypeAssociation(){
        mockForConstraintsTests(CustomerAccount)
        CustomerType type=(new CustomerType(name:"School"))

        CustomerAccount account = (new CustomerAccount(name: "Customer1",contactName: 'contact1',customerType: type))
        assert "School" == account.customerType.name
    }

    void testKioskAssociation(){
        mockForConstraintsTests(CustomerAccount)
        Kiosk k1 = new Kiosk(name: "k1",apiKey: "sampleKey")

        CustomerAccount account = (new CustomerAccount(name: "Customer2",contactName: 'contact1',kiosk: k1))
        assert "k1" == account.kiosk.name
    }
}
