package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(SalesChannel)
class SalesChannelTests {

    void testConstraints() {

        mockForConstraintsTests(SalesChannel)

        SalesChannel channel = new SalesChannel()

        assert !channel.validate()
        assert "nullable" == channel.errors['name']
        assertNull channel.errors["description"]
    }

    def testForUnique(){
        mockForConstraintsTests(SalesChannel)

        SalesChannel c1 = new SalesChannel(name: 'c1',description: '')
        assert c1.validate()


        SalesChannel c2 = new SalesChannel(name: 'c1',description: '',discountType: "AMOUNT",discountAmount: 12)
        mockForConstraintsTests(SalesChannel, [c1,c2])
        assert !c2.validate()
        c2.name="c2"
        assert c2.validate()

    }
}
