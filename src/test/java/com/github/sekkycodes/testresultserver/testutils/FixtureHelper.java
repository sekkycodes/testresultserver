package com.github.sekkycodes.testresultserver.testutils;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.github.sekkycodes.testresultserver.vo.importing.ImportRequest;
import java.util.Collections;

/**
 * Utility class for creating fixtures and test data
 */
public class FixtureHelper {

  public static long DEFAULT_TIMESTAMP = 1613729996246L;

  public static TestSuiteExecution buildTestSuiteExecution() {
    return TestSuiteExecution.builder()
        .id(new TimeNamePK("dummy test suite", DEFAULT_TIMESTAMP))
        .duration(1000L)
        .project("myDummyProject")
        .testType("Unit")
        .testCasesTotal(10)
        .testCasesPassed(1)
        .testCasesFailed(2)
        .testCasesSkipped(3)
        .testCasesWithError(4)
        .build();
  }

  public static TestCaseExecution buildTestCaseExecution() {
    return TestCaseExecution.builder()
        .id(new TimeNamePK("dummy test case", DEFAULT_TIMESTAMP))
        .suiteName("dummy test suite")
        .result(TestResult.PASSED)
        .duration(200L)
        .build();
  }

  public static ImportRequest buildImportRequest() {
    return ImportRequest.builder()
        .executionTimeStamp(DEFAULT_TIMESTAMP)
        .project("myDummyProject")
        .testType("Unit")
        .labels(Collections.singletonList("label01"))
        .build();
  }
}
