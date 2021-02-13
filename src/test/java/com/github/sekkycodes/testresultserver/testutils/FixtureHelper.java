package com.github.sekkycodes.testresultserver.testutils;

import com.github.sekkycodes.testresultserver.domain.TestCase;
import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.domain.TestSuite;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;

/**
 * Utility class for creating fixtures and test data
 */
public class FixtureHelper {

  public static TestSuite buildTestSuite() {
    TestSuite suite = TestSuite.builder()
        .id(UUID.randomUUID())
        .name("dummy test suite")
        .build();

    TestCase testCase = buildTestCase();
    suite.setTestCases(Collections.singleton(testCase));
    return suite;
  }

  public static TestCase buildTestCase() {
    return TestCase.builder()
        .id(UUID.randomUUID())
        .name("dummy test case")
        .build();
  }

  public static TestSuiteExecution buildTestSuiteExecution() {
    return TestSuiteExecution.builder()
        .id(new TimeNamePK("dummy test suite", System.currentTimeMillis()))
        .duration(1000L)
        .build();
  }

  public static TestCaseExecution buildTestCaseExecution() {
    return TestCaseExecution.builder()
        .id(new TimeNamePK("dummy test case", System.currentTimeMillis()))
        .suiteName("dummy test suite")
        .result(TestResult.PASSED)
        .duration(200L)
        .build();
  }
}
