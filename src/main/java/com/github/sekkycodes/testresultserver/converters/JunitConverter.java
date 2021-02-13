package com.github.sekkycodes.testresultserver.converters;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.github.sekkycodes.testresultserver.junit.Testsuite;
import java.time.Clock;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Converts JUnit objects to canonical value objects
 */
@Service
public class JunitConverter {

  private final Clock clock;

  @Autowired
  public JunitConverter(Clock clock) {
    this.clock = clock;
  }

  /**
   * Converts JUnit Testsuite object to canonical TestSuiteExecution entity
   *
   * @param junitSuite suite to be converted
   * @return converted suite execution object
   */
  public TestSuiteExecution toTestSuiteExecution(Testsuite junitSuite) {
    long executionTimestamp = getExecutionTimeStamp(junitSuite.getTimestamp());

    return TestSuiteExecution.builder()
        .id(new TimeNamePK(junitSuite.getName(), executionTimestamp))
        .duration(junitSuite.getTime().longValue())
        .build();
  }

  /**
   * Converts JUnit Testcase object to canonical TestCaseExecution entity
   *
   * @param testCase case to be converted
   * @return converted case execution object
   */
  public TestCaseExecution toTestCaseExecution(Testsuite.Testcase testCase, String suiteName,
      long executionTime) {

    return TestCaseExecution.builder()
        .id(new TimeNamePK(testCase.getName(), executionTime))
        .suiteName(suiteName)
        .duration(testCase.getTime().longValue())
        .result(toTestResult(testCase))
        .build();
  }

  /**
   * Falls back to current timestamp if none is set in the junit suite
   *
   * @param calendar the calendar object which is to be converted to
   * @return time stamp of the test execution as epoch millis
   */
  private long getExecutionTimeStamp(XMLGregorianCalendar calendar) {
    long executionTimestamp = calendar == null
        ? clock.millis()
        : calendar.toGregorianCalendar().getTimeInMillis();

    return executionTimestamp == 0
        ? System.currentTimeMillis()
        : executionTimestamp;
  }

  private TestResult toTestResult(Testsuite.Testcase testcase) {
    if (testcase.getSkipped() != null) {
      return TestResult.SKIPPED;
    } else if (testcase.getError() != null) {
      return TestResult.ERROR;
    } else if (testcase.getFailure() != null) {
      return TestResult.FAILED;
    } else {
      return TestResult.PASSED;
    }
  }
}
