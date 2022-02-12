package com.github.sekkycodes.testresultserver.vo;

import lombok.Builder;
import lombok.Value;

/**
 * Immutable value object of TestCaseExecution
 */
@Value
@Builder(toBuilder = true)
public class TestCaseExecutionVO {

  String idName;
  long idTime;
  String testResult;
  long duration;
  String message;
  String details;
  String failureType;

}
