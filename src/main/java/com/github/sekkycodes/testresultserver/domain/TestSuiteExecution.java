package com.github.sekkycodes.testresultserver.domain;

import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import com.querydsl.core.annotations.QueryEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A specific execution (or run) of a testsuite and all tests therein. An execution is correlated
 * with a result obtained at a specific point in time.
 */
@Document("suiteExecutions")
@QueryEntity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class TestSuiteExecution {

  /**
   * Unique identifier of the test suite execution by suite name and time of execution
   */
  @Id
  TimeNamePK id;

  /**
   * Date of execution
   */
  LocalDate executionDate;

  /**
   * A project identifier which the test suite was executed for
   */
  String project;

  /**
   * Type of test (for example "Unit", "Integration", "E2E")
   */
  String testType;

  /**
   * The environment the suite was executed against. Typically this is "local" or "ci" for unit and
   * integration tests, and (for example) "dev", "test", or "prod" for E2E tests.
   */
  String environment;

  /**
   * How long the test suite was running until all test cases completed (in milliseconds)
   */
  long duration;

  /**
   * Total number of test cases executed
   */
  int testCasesTotal;

  /**
   * Number of passed test cases
   */
  int testCasesPassed;

  /**
   * Number of skipped test cases
   */
  int testCasesSkipped;

  /**
   * Number of failed test cases
   */
  int testCasesFailed;

  /**
   * Number of test cases that had an error occurring during execution
   */
  int testCasesWithError;

  /**
   * The test cases that were executed within this suite
   */
  @DBRef(lazy = true)
  @Field("testCases")
  List<TestCaseExecution> testCaseExecutionList = new ArrayList<>();

  /**
   * Converts the domain object into an immutable value object
   *
   * @return mapped value object
   */
  public TestSuiteExecutionVO toValueObject() {
    return TestSuiteExecutionVO.builder()
        .idName(getId().getName())
        .idTime(getId().getTime())
        .project(getProject())
        .testType(getTestType())
        .environment(getEnvironment())
        .duration(getDuration())
        .testCasesTotal(getTestCasesTotal())
        .testCasesPassed(getTestCasesPassed())
        .testCasesFailed(getTestCasesFailed())
        .testCasesSkipped(getTestCasesSkipped())
        .testCasesWithError(getTestCasesWithError())
        .build();
  }
}
