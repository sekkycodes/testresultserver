package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.vo.TestCaseExecutionVO;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for retrieving test case executions
 */
@Service
public class TestCaseRetriever {

  private final TestSuiteExecutionRepository testSuiteExecutionRepository;

  @Autowired
  public TestCaseRetriever(TestSuiteExecutionRepository testSuiteExecutionRepository) {
    this.testSuiteExecutionRepository = Objects.requireNonNull(testSuiteExecutionRepository);
  }

  /**
   * Retrieves a list of test case executions on a specified date matching the specified result.
   *
   * @param executionDate date of test execution
   * @param result        the result of the test case execution (i.e. PASSED, FAILED, etc)
   */
  // the implementation for this method is far from ideal - it loads all suites for a date before
  // filtering for test case results. better would be to filter out the test cases on DB side.
  public Set<TestCaseExecutionVO> retrieveByResultAndDate(LocalDate executionDate,
      TestResult result, String testType, String project) {
    List<TestSuiteExecution> testSuiteExecutionList = testSuiteExecutionRepository
        .findByExecutionDateAndProjectAndTestType(executionDate, project, testType);

    return testSuiteExecutionList.stream()
        .map(TestSuiteExecution::getTestCaseExecutionList)
        .flatMap(List::stream)
        .filter(tc -> result.equals(tc.getResult()))
        .map(TestCaseExecution::toValueObject)
        .collect(Collectors.toSet());
  }
}
