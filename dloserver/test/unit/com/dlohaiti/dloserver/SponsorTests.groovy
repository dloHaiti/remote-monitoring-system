package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Sponsor)
class SponsorTests {

    void testConstraints() {

        mockForConstraintsTests(Sponsor)

        Sponsor sponsor=new Sponsor()

        assert !sponsor.validate()
        assert "nullable" == sponsor.errors['name']
        assert "nullable" == sponsor.errors['contactName']
        assertNull sponsor.errors["description"]
    }

    def testForUnique(){
        mockForConstraintsTests(Sponsor)
        Kiosk k1 = new Kiosk(name: "k4",apiKey: "sampleKey")
        Sponsor sponsor=new Sponsor(name:"sponsor1",contactName:"contact name",kiosk: k1)
        assert sponsor.validate()


        Sponsor sponsor2=new Sponsor(name:"sponsor1",contactName:"contact name",kiosk: k1)
        mockForConstraintsTests(Sponsor, [sponsor,sponsor2])
        assert !sponsor2.validate()
        sponsor2.name="sponsor2"
        assert sponsor2.validate()
    }


}
