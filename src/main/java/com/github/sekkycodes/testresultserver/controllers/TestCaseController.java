package com.github.sekkycodes.testresultserver.controllers;

import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.services.TestCaseRetriever;
import com.github.sekkycodes.testresultserver.vo.TestCaseExecutionVO;
import com.github.sekkycodes.testresultserver.vo.requests.TestCaseFilter;
import com.google.common.base.Strings;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
   * Endpoint for retrieving test cases filtered for by a number of criteria. For example all
   * successfully executed tests ('PASSED') on the 01.01.2021.
   *
   * @param filter specifies what test cases to filter for
   * @return a list of test case executions
   */
  @PostMapping("/filter")
  public ResponseEntity<Set<TestCaseExecutionVO>> filter(
      @RequestBody TestCaseFilter filter
  ) {

    if(!Strings.isNullOrEmpty(filter.getResult())) {
      try {
        TestResult.valueOf(filter.getResult().toUpperCase());
      } catch (IllegalArgumentException il) {
        return ResponseEntity.badRequest().build();
      }
    }

    if(!Strings.isNullOrEmpty(filter.getDate())) {
      try {
        LocalDate.parse(filter.getDate());
      } catch (DateTimeParseException dtpe) {
        return ResponseEntity.badRequest().build();
      }
    }

    Set<TestCaseExecutionVO> testCaseExecutionVOs = testCaseRetriever
        .retrieveByFilter(filter);

    return ResponseEntity.ok(testCaseExecutionVOs);
  }
}
