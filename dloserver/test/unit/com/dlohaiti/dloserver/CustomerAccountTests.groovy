package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(CustomerAccount)
class CustomerAccountTests {

    void testConstraints() {

        mockForConstraintsTests(CustomerAccount)

        CustomerAccount account = new CustomerAccount()

        assert !account.validate()
        assert "nullable" == account.errors['name']

    }

    void testCustomerTypeAssociation(){
        mockForConstraintsTests(CustomerAccount)
        CustomerType type=(new CustomerType(name:"School"))

        CustomerAccount account = (new CustomerAccount(name: "Customer1",contactNames: ['contact1'],customerType: type))
        assert "School" == account.customerType.name
    }
}
