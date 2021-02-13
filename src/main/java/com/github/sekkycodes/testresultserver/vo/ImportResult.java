package com.github.sekkycodes.testresultserver.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.util.Set;
import lombok.Builder;
import lombok.Value;

/**
 * Result VO for importing, summarizing imported data
 */
@Value
@Builder(toBuilder = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ImportResult {

  /**
   * Imported test suite execution
   */
  TestSuiteExecutionVO importedSuite;

  /**
   * Set of imported test case executions
   */
  Set<TestCaseExecutionVO> importedCases;
}
