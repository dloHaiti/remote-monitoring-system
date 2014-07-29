package com.dlohaiti.dloserver



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Operator)
class OperatorTests {

    def testOperatorContraints() {
      mockForConstraintsTests(Operator)
        Operator operator=new Operator()

        assert !operator.validate()
        assert "nullable" == operator.errors["userName"]
        assert "nullable" == operator.errors["password"]
    }
    def testForUnique(){
      mockForConstraintsTests(Operator)
      Operator op1 = new Operator(userName: "Op1",password: "pass") 
      assert op1.validate()

    
      Operator op2 = new Operator(userName: "Op1",password: "pass") 
      mockForConstraintsTests(Operator,[op1,op2])
      assert !op2.validate()
      op2.userName = "op2"

      assert op1.validate()
    }

  }
