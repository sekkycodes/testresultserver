package com.github.sekkycodes.testresultserver.converters;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.junit.Testsuite;
import com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase;
import com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase.Error;
import com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase.Failure;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JunitConverterTest {

  private JunitConverter sut;

  private static final long TIME_MILLIS = 1613232242954L;
  private static final long FALLBACK_MILLIS = 1613232212345L;

  @BeforeEach
  void beforeEach() {
    sut = new JunitConverter(Clock.fixed(Instant.ofEpochMilli(FALLBACK_MILLIS), ZoneId.of("UTC")));
  }

  @Nested
  class ToTestSuiteExecution {

    private Testsuite dummySuite;

    @BeforeEach
    void beforeEach() throws DatatypeConfigurationException {
      dummySuite = buildSuite();
    }

    @Test
    void convertsJunitTestsuiteToCanonicalEntity() {
      TestSuiteExecution result = sut.toTestSuiteExecution(dummySuite);

      assertThat(result.getId().getName()).isEqualTo("dummyTestSuite");
      assertThat(result.getId().getTime()).isEqualTo(TIME_MILLIS);
      assertThat(result.getDuration()).isEqualTo(10_000L);
    }

    @Test
    void usesCurrentMillisAsFallbackIfNoTimestampIsSet() {
      dummySuite.setTimestamp(null);

      TestSuiteExecution result = sut.toTestSuiteExecution(dummySuite);

      assertThat(result.getId().getTime()).isEqualTo(FALLBACK_MILLIS);
    }

    @Test
    void usesCurrentMillisAsFallbackIfSetTimestampIsZero() throws DatatypeConfigurationException {
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTimeInMillis(0L);
      dummySuite.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));

      TestSuiteExecution result = sut.toTestSuiteExecution(dummySuite);

      assertThat(result.getId().getTime()).isEqualTo(FALLBACK_MILLIS);
    }

    private Testsuite buildSuite() throws DatatypeConfigurationException {
      Testsuite suite = new Testsuite();
      suite.setName("dummyTestSuite");
      suite.setTime(BigDecimal.TEN);
      suite.getTestcase().add(buildTestCase());
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTimeInMillis(TIME_MILLIS);
      suite.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
      return suite;
    }
  }

  @Nested
  class ToTestCaseExecution {

    private Testsuite.Testcase dummyCase;
    private static final String SUITE_NAME = "suite name";

    @BeforeEach
    void beforeEach() {
      dummyCase = buildTestCase();
    }

    @Test
    void convertsJunitTestcaseToCanonicalEntity() {
      TestCaseExecution result = invokeSut();

      assertThat(result.getId().getName()).isEqualTo(dummyCase.getName());
      assertThat(result.getId().getTime()).isEqualTo(TIME_MILLIS);
      assertThat(result.getSuiteName()).isEqualTo(SUITE_NAME);
      assertThat(result.getDuration()).isEqualTo(1000L);
      assertThat(result.getResult()).isEqualTo(TestResult.PASSED);
    }

    @Test
    void setsSkippedTestResult() {
      dummyCase.setSkipped(new Object());

      TestCaseExecution result = invokeSut();

      assertThat(result.getResult()).isEqualTo(TestResult.SKIPPED);
    }

    @Test
    void setsErrorTestResult() {
      dummyCase.setError(new Error());

      TestCaseExecution result = invokeSut();

      assertThat(result.getResult()).isEqualTo(TestResult.ERROR);
    }

    @Test
    void setsFailedTestResult() {
      dummyCase.setFailure(new Failure());

      TestCaseExecution result = invokeSut();

      assertThat(result.getResult()).isEqualTo(TestResult.FAILED);
    }

    private TestCaseExecution invokeSut() {
      return sut.toTestCaseExecution(dummyCase, SUITE_NAME, TIME_MILLIS);
    }
  }

  private Testcase buildTestCase() {
    Testcase testCase = new Testcase();
    testCase.setName("dummyTestCase");
    testCase.setTime(BigDecimal.ONE);
    return testCase;
  }
}
