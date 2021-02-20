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
  String project;
  String testType;
  String environment;
  long duration;
  int testCasesTotal;
  int testCasesPassed;
  int testCasesSkipped;
  int testCasesFailed;
  int testCasesWithError;

}
