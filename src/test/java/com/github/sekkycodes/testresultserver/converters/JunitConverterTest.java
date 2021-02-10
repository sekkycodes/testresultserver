package com.github.sekkycodes.testresultserver.converters;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.domain.TestSuite;
import com.github.sekkycodes.testresultserver.junit.Testsuite;
import com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

class JunitConverterTest {

  private JunitConverter sut;

  @BeforeEach
  void beforeEach() {
    sut = new JunitConverter();
  }

  @Test
  public void convertsJunitTestsuiteToCanonicalEntity() {
    Testsuite suite = buildSuite();

    TestSuite result = sut.toTestSuite(suite);

    assertThat(result.getName()).isEqualTo("dummyTestSuite");
    assertThat(result.getTestCases().iterator().next().getName()).isEqualTo("dummyTestCase");
  }

  private Testsuite buildSuite() {
    Testsuite suite = new Testsuite();
    suite.setName("dummyTestSuite");
    suite.getTestcase().add(buildTestCase());
    return suite;
  }

  private Testcase buildTestCase() {
    Testcase testCase = new Testcase();
    testCase.setName("dummyTestCase");
    return testCase;
  }
}
