package com.dlohaiti.dloserver

import grails.test.mixin.TestFor

@TestFor(Parameter)
public class ParameterTest {

    void testShouldKnowWhenItHasRange() {
      def parameter = new ParameterBuilder(min: null, max: null).build()
      assert !parameter.hasRange()

      parameter = new ParameterBuilder(min: 1G, max: null).build()
      assert parameter.hasRange()

      parameter = new ParameterBuilder(min: null, max: 10G).build()
      assert parameter.hasRange()

      parameter = new ParameterBuilder(min: 1G, max: 100G).build()
      assert parameter.hasRange()
    }
}
