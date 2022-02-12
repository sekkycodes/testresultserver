package com.github.sekkycodes.testresultserver.vo.reporting;

/**
 * Aggregation of test results
 */
public enum AggregateBy {

  /**
   * Aggregates by date of suite execution
   */
  DATE,

  /**
   * Aggregates by test type
   */
  TEST_TYPE,

  /**
   * Aggregates by the environment the test suite was executed again
   */
  ENVIRONMENT
}
