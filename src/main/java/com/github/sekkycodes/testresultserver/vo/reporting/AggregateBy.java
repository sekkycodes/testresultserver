package com.github.sekkycodes.testresultserver.vo.reporting;

/**
 * Aggregation of test results
 */
public enum AggregateBy {

  /**
   * Aggregates by label attached to suite
   */
  LABEL,

  /**
   * Aggregates by date of suite execution
   */
  DATE,

  /**
   * Aggregates by test type
   */
  TEST_TYPE
}
