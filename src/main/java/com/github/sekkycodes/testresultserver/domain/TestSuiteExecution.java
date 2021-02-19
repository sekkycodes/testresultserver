package com.github.sekkycodes.testresultserver.domain;

import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A specific execution (or run) of a testsuite and all tests therein.
 * An execution is correlated with a result obtained at a specific point in time.
 */
@Entity
@Table(name = "testsuite_executions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class TestSuiteExecution {

  /**
   * Unique identifier of the test suite execution by suite name and time of execution
   */
  @EmbeddedId
  TimeNamePK id;

  /**
   * A project identifier which the test suite was executed for
   */
  String project;

  /**
   * Type of test (for example "Unit", "Integration", "E2E")
   */
  String testType;

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
   * Converts the domain object into an immutable value object
   * @return mapped value object
   */
  public TestSuiteExecutionVO toValueObject() {
    return TestSuiteExecutionVO.builder()
        .idName(getId().getName())
        .idTime(getId().getTime())
        .project(getProject())
        .testType(getTestType())
        .duration(getDuration())
        .testCasesTotal(getTestCasesTotal())
        .testCasesPassed(getTestCasesPassed())
        .testCasesFailed(getTestCasesFailed())
        .testCasesSkipped(getTestCasesSkipped())
        .testCasesWithError(getTestCasesWithError())
        .build();
  }
}
