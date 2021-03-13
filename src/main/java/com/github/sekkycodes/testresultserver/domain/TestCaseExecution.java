package com.github.sekkycodes.testresultserver.domain;

import com.github.sekkycodes.testresultserver.vo.TestCaseExecutionVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A specific execution (or run) of a test case.
 * An execution is correlated with a result obtained at a specific point in time.
 */
@Document("caseExecutions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class TestCaseExecution {

  /**
   * Unique identifier of the test case execution by test case name and time of execution
   */
  @Id
  TimeNamePK id;

  /**
   * Name of the suite under which the test case was executed.
   * The suite's execution and the case share the same execution time (id.time).
   */
  String suiteName;

  /**
   * Result of the test case run
   */
  TestResult result;

  /**
   * How long the test case was running (in milliseconds)
   */
  long duration;

  /**
   * Converts the domain object into an immutable value object
   * @return mapped value object
   */
  public TestCaseExecutionVO toValueObject() {
    return TestCaseExecutionVO.builder()
        .idName(id.getName())
        .idTime(id.getTime())
        .testResult(result.name())
        .duration(duration)
        .build();
  }
}
