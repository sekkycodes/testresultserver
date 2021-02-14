package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Calculates the latest results across all test suites
 */
@Service
public class LatestResultsReporter {

  private final TestSuiteExecutionRepository testSuiteExecutionRepository;

  @Autowired
  public LatestResultsReporter(TestSuiteExecutionRepository testSuiteExecutionRepository) {
    this.testSuiteExecutionRepository = Objects.requireNonNull(testSuiteExecutionRepository);
  }

  /**
   * Calculates the latest results across test suites without aggregation or filter.
   * @return a collection of test suite execution results in no particular order
   */
  public Collection<TestSuiteExecutionVO> getAllLatest() {
    return testSuiteExecutionRepository.findLatestResults()
        .stream()
        .map(TestSuiteExecution::toValueObject)
        .collect(Collectors.toSet());
  }
}
