package com.github.sekkycodes.testresultserver.controllers;

import com.github.sekkycodes.testresultserver.services.LatestResultsReporter;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import java.util.Collection;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for retrieving report summaries
 */
@RestController
@RequestMapping("/api/reporting")
public class ReportingController {

  private final LatestResultsReporter latestResultsReporter;

  public ReportingController(LatestResultsReporter latestResultsReporter) {
    this.latestResultsReporter = Objects.requireNonNull(latestResultsReporter);
  }

  /**
   * Endpoint for retrieving latest suite execution results without aggregation or filtering.
   *
   * @return a collection of latest suite execution results
   */
  @GetMapping("/latest")
  public ResponseEntity<Collection<TestSuiteExecutionVO>> getAllLatestSuiteResults() {

    return new ResponseEntity<>(latestResultsReporter.getAllLatest(), HttpStatus.OK);
  }
}
