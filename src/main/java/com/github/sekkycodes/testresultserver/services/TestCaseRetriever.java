package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.domain.QTestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestCaseExecutionRepository;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.vo.TestCaseExecutionVO;
import com.github.sekkycodes.testresultserver.vo.requests.TestCaseFilter;
import com.google.common.base.Strings;
import com.querydsl.core.BooleanBuilder;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
   * Retrieves a list of test case executions filtered by specified criteria.
   *
   * @param filter specifies what criteria to filter test cases by
   */
  // the implementation for this method is far from ideal - it loads all suites for a date before
  // filtering for test case results. better would be to filter out the test cases on DB side.
  public Set<TestCaseExecutionVO> retrieveByFilter(TestCaseFilter filter) {

    QTestSuiteExecution qSuite = new QTestSuiteExecution("test-suite-execution");
    BooleanBuilder booleanBuilder = new BooleanBuilder();

    if (!Strings.isNullOrEmpty(filter.getDate())) {
      booleanBuilder.and(qSuite.executionDate.eq(LocalDate.parse(filter.getDate())));
    }

    if (!Strings.isNullOrEmpty(filter.getEnvironment())) {
      booleanBuilder.and(qSuite.environment.eq(filter.getEnvironment()));
    }

    if (!Strings.isNullOrEmpty(filter.getProjectName())) {
      booleanBuilder.and(qSuite.project.eq(filter.getProjectName()));
    }

    if (!Strings.isNullOrEmpty(filter.getTestType())) {
      booleanBuilder.and(qSuite.testType.eq(filter.getTestType()));
    }

    if (filter.getLabels() != null) {
      for (String label : filter.getLabels()) {
        booleanBuilder.and(qSuite.labels.contains(label));
      }
    }

    TestResult result = null;
    if (!Strings.isNullOrEmpty(filter.getResult())) {
      result = TestResult.valueOf(filter.getResult().toUpperCase());

      switch (result) {
        case PASSED:
          booleanBuilder.and(qSuite.testCasesPassed.gt(0));
          break;
        case ERROR:
          booleanBuilder.and(qSuite.testCasesWithError.gt(0));
          break;
        case FAILED:
          booleanBuilder.and(qSuite.testCasesFailed.gt(0));
          break;
        case SKIPPED:
          booleanBuilder.and(qSuite.testCasesSkipped.gt(0));
          break;
      }
    }

    Iterable<TestSuiteExecution> matched = testSuiteExecutionRepository
        .findAll(Objects.requireNonNull(booleanBuilder));

    final TestResult rs = result;
    return StreamSupport.stream(matched.spliterator(), false)
        .flatMap(s -> s.getTestCaseExecutionList().stream())
        .filter(tc -> rs == null || tc.getResult().equals(rs))
        .map(TestCaseExecution::toValueObject)
        .collect(Collectors.toSet());
  }
}
