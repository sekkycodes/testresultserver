package com.github.sekkycodes.testresultserver.vo.reporting;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

/**
 * The result of calculating an aggregated report.
 */
@Value
@Builder(toBuilder = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class AggregatedReport {

  /**
   * The list of dimensions by which the results are aggregated. The values within the entries
   * reflect the order of aggregation.
   */
  List<AggregateBy> aggregationDimensions;

  /**
   * Aggregated entries
   */
  List<AggregatedReportEntry> entries;

  /**
   * Entry within an aggregated report
   */
  @Value
  @Builder(toBuilder = true)
  @JsonAutoDetect(fieldVisibility = Visibility.ANY)
  public static class AggregatedReportEntry {

    /**
     * The report has been aggregated along n dimensions. The values within this collection reflect
     * the individual value for each dimensions.
     */
    List<String> aggregatedByValues;

    /**
     * Sum of durations of all included suite executions
     */
    long duration;

    /**
     * Sum of all executed test cases of all included suite executions
     */
    int testCasesTotal;

    /**
     * Sum of passed test cases of all included suite executions
     */
    int testCasesPassed;

    /**
     * Sum of skipped test cases of all included suite executions
     */
    int testCasesSkipped;

    /**
     * Sum of failed test cases of all included suite executions
     */
    int testCasesFailed;

    /**
     * Sum of test cases completing with errors of all included suite executions
     */
    int testCasesWithError;

    /**
     * List of test suite execution IDs that matched the aggregation
     */
    List<Map.Entry<String, Long>> testSuiteExecutionIds;
  }
}
