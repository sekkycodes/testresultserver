package com.github.sekkycodes.testresultserver.converters;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.github.sekkycodes.testresultserver.junit.Testsuite;
import com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase.Error;
import com.github.sekkycodes.testresultserver.junit.Testsuite.Testcase.Failure;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.logging.log4j.util.Strings;
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
   * @param junitSuite                 suite to be converted
   * @param fallbackExecutionTimestamp used if the timestamp in the junit suite is not set
   * @return converted suite execution object
   */
  public TestSuiteExecution toTestSuiteExecution(Testsuite junitSuite,
      long fallbackExecutionTimestamp) {

    long executionTimestamp = getExecutionTimeStamp(junitSuite.getTimestamp(),
        fallbackExecutionTimestamp);

    int passed =
        junitSuite.getTests() - junitSuite.getErrors()
            - junitSuite.getSkipped() - junitSuite.getFailures();

    List<TestCaseExecution> testCases = junitSuite.getTestcase().stream()
        .map(tc -> toTestCaseExecution(tc, junitSuite.getName(), executionTimestamp))
        .collect(Collectors.toList());

    return TestSuiteExecution.builder()
        .id(new TimeNamePK(junitSuite.getName(), executionTimestamp))
        .executionDate(
            Instant.ofEpochMilli(executionTimestamp).atZone(ZoneId.of("UTC")).toLocalDate())
        .duration(toMillis(junitSuite.getTime()))
        .testCasesTotal(junitSuite.getTests())
        .testCasesPassed(passed)
        .testCasesFailed(junitSuite.getFailures())
        .testCasesSkipped(junitSuite.getSkipped())
        .testCasesWithError(junitSuite.getErrors())
        .testCaseExecutionList(testCases)
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

    TestResult result = toTestResult(testCase);
    String message = Strings.EMPTY;
    String details = Strings.EMPTY;
    String failureType = Strings.EMPTY;

    switch (result) {
      case FAILED:
        Failure failure = testCase.getFailure();
        message = failure.getMessage();
        details = failure.getValue();
        failureType = failure.getType();
        break;
      case ERROR:
        Error error = testCase.getError();
        message = error.getMessage();
        details = error.getValue();
        failureType = error.getType();
        break;
    }

    return TestCaseExecution.builder()
        .id(new TimeNamePK(testCase.getName(), executionTime))
        .suiteName(suiteName)
        .duration(toMillis(testCase.getTime()))
        .result(toTestResult(testCase))
        .failureType(failureType)
        .message(message)
        .details(details)
        .build();
  }

  /**
   * Falls back to current timestamp if none is set in the junit suite nor the fallback
   *
   * @param calendar                   the calendar object which is to be converted to
   * @param fallbackExecutionTimestamp used if calendar is null
   * @return time stamp of the test execution as epoch millis
   */
  private long getExecutionTimeStamp(XMLGregorianCalendar calendar,
      long fallbackExecutionTimestamp) {

    long executionTimestamp = 0;
    if (calendar != null) {
      executionTimestamp = calendar.toGregorianCalendar().getTimeInMillis();
    }

    if (executionTimestamp == 0) {
      executionTimestamp = fallbackExecutionTimestamp;
    }

    if (executionTimestamp == 0) {
      executionTimestamp = clock.millis();
    }

    return executionTimestamp;
  }

  // time in test suites and cases is given as a floating point number denoting seconds
  private long toMillis(BigDecimal decimal) {
    return decimal.multiply(BigDecimal.valueOf(1000)).longValue();
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
