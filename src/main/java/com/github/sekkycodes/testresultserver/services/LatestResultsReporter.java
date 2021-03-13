package com.github.sekkycodes.testresultserver.services;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.maxBy;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Calculates the latest results across all test suites
 */
@Service
public class LatestResultsReporter {

  private final TestSuiteExecutionRepository suiteExecutionRepository;

  @Autowired
  public LatestResultsReporter(TestSuiteExecutionRepository suiteExecutionRepository) {
    this.suiteExecutionRepository = Objects.requireNonNull(suiteExecutionRepository);
  }

  /**
   * Calculates the latest results across test suites without aggregation or filter.
   *
   * @return a collection of test suite execution results in no particular order
   */
  public Collection<TestSuiteExecutionVO> getAllLatest() {

    // TODO: optimize to not load all entries from database into RAM before grouping
    Map<String, Optional<TestSuiteExecution>> grouped = suiteExecutionRepository
        .findAll()
        .stream()
        .collect(groupingBy(
            tse -> tse.getId().getName(),
            maxBy(Comparator.comparing(tse -> tse.getId().getTime()))));

    return grouped
        .values()
        .stream()
        .filter(Optional::isPresent)
        .map(tse -> tse.get().toValueObject())
        .collect(Collectors.toList());
  }
}
