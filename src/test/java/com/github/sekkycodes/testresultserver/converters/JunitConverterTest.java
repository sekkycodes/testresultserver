package com.github.sekkycodes.testresultserver.converters;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.junit.Testsuite;
import com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase;
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

  private Testsuite dummySuite;

  @BeforeEach
  void beforeEach() throws DatatypeConfigurationException {
    sut = new JunitConverter(Clock.fixed(Instant.ofEpochMilli(TIME_MILLIS), ZoneId.of("UTC")));
    dummySuite = buildSuite();
  }

  @Nested
  class ToTestSuiteExecution {

    @Test
    void convertsJunitTestsuiteToCanonicalEntity() {
      TestSuiteExecution result = sut.toTestSuiteExecution(dummySuite);

      assertThat(result.getId().getName()).isEqualTo("dummyTestSuite");
      assertThat(result.getId().getTime()).isEqualTo(TIME_MILLIS);
      assertThat(result.getDuration()).isEqualTo(10L);
    }

    @Test
    void usesCurrentMillisAsFallbackIfNoTimestampIsSet() {
      dummySuite.setTimestamp(null);

      TestSuiteExecution result = sut.toTestSuiteExecution(dummySuite);
    }
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

  private Testcase buildTestCase() {
    Testcase testCase = new Testcase();
    testCase.setName("dummyTestCase");
    return testCase;
  }
}
