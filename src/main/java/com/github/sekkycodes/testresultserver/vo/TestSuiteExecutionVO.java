package com.github.sekkycodes.testresultserver.vo;

import lombok.Builder;
import lombok.Value;

/**
 * Immutable value object of TestSuiteExecution
 */
@Value
@Builder(toBuilder = true)
public class TestSuiteExecutionVO {

  String idName;
  long idTime;
  long duration;

}
