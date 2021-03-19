package com.github.sekkycodes.testresultserver.controllers;

import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.services.TestCaseRetriever;
import com.github.sekkycodes.testresultserver.vo.TestCaseExecutionVO;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for retrieving test case execution information.
 */
@RestController
@RequestMapping("/api/testcase")
public class TestCaseController {

  private final TestCaseRetriever testCaseRetriever;

  @Autowired
  public TestCaseController(TestCaseRetriever testCaseRetriever) {
    this.testCaseRetriever = Objects.requireNonNull(testCaseRetriever);
  }

  /**
   * Endpoint for retrieving test cases executed on a specified date with a given result. For
   * example all successfully executed tests ('PASSED') on the 01.01.2021.
   *
   * @param date   date to filter for
   * @param result result to filter for
   * @return a list of test case executions
   */
  @GetMapping("/by-date-and-result")
  public ResponseEntity<Set<TestCaseExecutionVO>> getByDateAndResult(
      @RequestParam String date,
      @RequestParam String result,
      @RequestParam String testType,
      @RequestParam String project
  ) {
    TestResult filterForResult;
    try {
      filterForResult = TestResult.valueOf(result.toUpperCase());
    } catch (IllegalArgumentException il) {
      return ResponseEntity.badRequest().build();
    }

    LocalDate filterForDate = LocalDate.parse(date);

    Set<TestCaseExecutionVO> testCaseExecutionVOs = testCaseRetriever
        .retrieveByResultAndDate(filterForDate, filterForResult, testType, project);

    return ResponseEntity.ok(testCaseExecutionVOs);
  }
}
