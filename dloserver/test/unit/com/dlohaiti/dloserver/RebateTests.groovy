package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Rebate)
class RebateTests {

    void testConstraints() {

        mockForConstraintsTests(Rebate)

       Rebate rebate=new Rebate()

        assert !rebate.validate()
        assert "nullable" == rebate.errors['noOfSkus']
        assert "nullable" == rebate.errors['transactionType']
        assert "nullable" == rebate.errors['noOfFreeSkus']
        assert "nullable" == rebate.errors['product']
    }
}
