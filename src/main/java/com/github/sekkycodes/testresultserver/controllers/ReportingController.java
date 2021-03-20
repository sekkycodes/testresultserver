package com.github.sekkycodes.testresultserver.controllers;

import com.github.sekkycodes.testresultserver.services.AggregatedResultsReporter;
import com.github.sekkycodes.testresultserver.services.LatestResultsReporter;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport;
import com.github.sekkycodes.testresultserver.vo.reporting.ReportRequest;
import java.util.Collection;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for retrieving report summaries
 */
@RestController
@RequestMapping("/api/reporting")
public class ReportingController {

  private final LatestResultsReporter latestResultsReporter;
  private final AggregatedResultsReporter aggregatedResultsReporter;

  public ReportingController(LatestResultsReporter latestResultsReporter,
      AggregatedResultsReporter aggregatedResultsReporter) {
    this.latestResultsReporter = Objects.requireNonNull(latestResultsReporter);
    this.aggregatedResultsReporter = Objects.requireNonNull(aggregatedResultsReporter);
  }

  /**
   * Endpoint for retrieving latest suite execution results without aggregation or filtering.
   *
   * @return a collection of latest suite execution results
   */
  @GetMapping("/latest-suites")
  public ResponseEntity<Collection<TestSuiteExecutionVO>> getAllLatestSuiteResults(
      @RequestParam String project) {

    return ResponseEntity.ok(latestResultsReporter.getAllLatest(project));
  }

  /**
   * Endpoint for retrieving a trend of latest test executions by label.
   *
   * @param reportRequest the request lists all criteria by which the report result is first
   *                      filtered and then aggregated
   * @return an aggregated report over test suite executions
   */
  @PostMapping("/aggregated")
  public ResponseEntity<AggregatedReport> aggregatedReport(
      @RequestBody ReportRequest reportRequest) {

    AggregatedReport report = aggregatedResultsReporter
        .report(reportRequest.getFilter(), reportRequest.getAggregations());
    return ResponseEntity.ok(report);
  }
}
