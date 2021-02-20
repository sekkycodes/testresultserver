package com.github.sekkycodes.testresultserver.testutils;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.github.sekkycodes.testresultserver.vo.importing.ImportRequest;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;

/**
 * Utility class for creating fixtures and test data
 */
public class FixtureHelper {

  public static final Instant FIXED_TIMESTAMP = Instant.parse("2020-01-29T10:00:00.00Z");
  public static final Clock FIXED_CLOCK = Clock.fixed(FIXED_TIMESTAMP, ZoneId.systemDefault());

  public static TestSuiteExecution buildTestSuiteExecution() {
    return TestSuiteExecution.builder()
        .id(new TimeNamePK("dummy test suite", FIXED_TIMESTAMP.toEpochMilli()))
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
        .id(new TimeNamePK("dummy test case", FIXED_TIMESTAMP.toEpochMilli()))
        .suiteName("dummy test suite")
        .result(TestResult.PASSED)
        .duration(200L)
        .build();
  }

  public static ImportRequest buildImportRequest() {
    return ImportRequest.builder()
        .executionTimeStamp(FIXED_TIMESTAMP.toEpochMilli())
        .project("myDummyProject")
        .testType("Unit")
        .labels(Collections.singletonList("label01"))
        .build();
  }
}
