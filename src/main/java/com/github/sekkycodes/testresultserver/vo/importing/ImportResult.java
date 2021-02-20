package com.github.sekkycodes.testresultserver.vo.importing;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.github.sekkycodes.testresultserver.vo.TestCaseExecutionVO;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
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

  /**
   * If an error occurred during import, this field can be used to provide additional information
   * about that error
   */
  String errorMessage;

  public static ImportResult empty() {
    return ImportResult.builder().build();
  }
}
