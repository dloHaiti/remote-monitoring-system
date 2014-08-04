package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(CustomerType)
class CustomerTypeTests {

    void testConstraints() {

        mockForConstraintsTests(CustomerType)

        CustomerType nullType = new CustomerType()

        assert !nullType.validate()
        assert "nullable" == nullType.errors['name']
        nullType.name="Customer Type"
        assert nullType.validate()

    }

    void testSubType(){
        mockForConstraintsTests(CustomerType)

        CustomerType parent = new CustomerType(name: "parent")

        assert parent.validate()
        assert !parent.isSubType()

        CustomerType child = new CustomerType(name: "child1",parent: parent)

        assert child.validate()
        assert child.isSubType()

    }
}
