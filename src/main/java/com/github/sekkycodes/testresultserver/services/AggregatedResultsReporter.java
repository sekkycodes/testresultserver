package com.github.sekkycodes.testresultserver.services;

import static com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository.hasProject;
import static com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository.hasTestType;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregateBy;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport.AggregatedReportEntry;
import com.github.sekkycodes.testresultserver.vo.reporting.Filter;
import com.google.common.base.Strings;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Creates aggregated test reports
 */
@Service
public class AggregatedResultsReporter {

  private final TestSuiteExecutionRepository testSuiteExecutionRepository;
  private final Clock clock;

  @Autowired
  public AggregatedResultsReporter(TestSuiteExecutionRepository testSuiteExecutionRepository,
      Clock clock) {

    this.testSuiteExecutionRepository = Objects.requireNonNull(testSuiteExecutionRepository);
    this.clock = Objects.requireNonNull(clock);
  }

  /**
   * Creates aggregated test reports from a set of filters and aggregation parameters. Suites
   * matching ALL filter criteria are included in the report. These suites are then aggregated along
   * n dimensions.
   *
   * @param filter       a filter with several criteria excluding suites not to be included in the
   *                     report calculation
   * @param aggregateBys a list for creating the n-dimensional aggregations by
   * @return a full report with aggregated entries
   */
  public AggregatedReport report(Filter filter, List<AggregateBy> aggregateBys) {
    List<TestSuiteExecutionVO> suiteExecutions = getSuiteExecutionsByFilter(filter)
        .stream().map(TestSuiteExecution::toValueObject).collect(Collectors.toList());

    List<TestSuiteExecutionVO> limited = limit(suiteExecutions, filter.getDaysBack());

    AggregatedEntry aggregated = AggregatedEntry.builder()
        .aggregatedByValues(new ArrayList<>())
        .original(limited)
        .build();

    List<AggregatedEntry> aggregatedEntries = Collections.singletonList(aggregated);
    for (AggregateBy aggregateBy : aggregateBys) {
      aggregatedEntries = aggregate(aggregatedEntries, aggregateBy);
    }

    List<AggregatedReportEntry> entries = aggregatedEntries.stream()
        .map(AggregatedEntry::toReportEntry)
        .collect(Collectors.toList());

    return AggregatedReport.builder()
        .aggregationDimensions(new ArrayList<>(aggregateBys))
        .entries(entries)
        .build();
  }

  private List<TestSuiteExecution> getSuiteExecutionsByFilter(Filter filter) {
    Specification<TestSuiteExecution> spec = Specification.where(null);

    if (!Strings.isNullOrEmpty(filter.getProjectName())) {
      spec = spec.and(hasProject(filter.getProjectName()));
    }

    if (!Strings.isNullOrEmpty(filter.getTestType())) {
      spec = spec.and(hasTestType(filter.getTestType()));
    }

    return testSuiteExecutionRepository.findAll(Specification.where(spec));
  }

  private List<TestSuiteExecutionVO> limit(List<TestSuiteExecutionVO> suites, int daysBack) {

    if (daysBack <= 0) {
      return suites;
    }

    return suites.stream()
        .filter(x -> Instant.ofEpochMilli(x.getIdTime())
                .isAfter(clock.instant().minus(daysBack, ChronoUnit.DAYS)))
        .collect(Collectors.toList());
  }

  private List<AggregatedEntry> aggregate(List<AggregatedEntry> entries, AggregateBy aggregateBys) {

    switch (aggregateBys) {
      case DATE:
        return aggregateByDate(entries);
      case LABEL:
        return aggregateByLabels(entries);
      case TEST_TYPE:
        return aggregateByTestType(entries);
      default:
        throw new IllegalStateException("no support for aggregation by: " + aggregateBys.name());
    }
  }

  private List<AggregatedEntry> aggregateByDate(List<AggregatedEntry> entries) {
    // TODO
    return entries;
  }

  private List<AggregatedEntry> aggregateByLabels(List<AggregatedEntry> entries) {
    // TODO
    return entries;
  }

  private List<AggregatedEntry> aggregateByTestType(List<AggregatedEntry> entries) {

    List<AggregatedEntry> result = new ArrayList<>();

    for (AggregatedEntry entry : entries) {
      Map<String, List<TestSuiteExecutionVO>> grouped = entry.getOriginal().stream()
          .collect(Collectors.groupingBy(TestSuiteExecutionVO::getTestType));
      for (Map.Entry<String, List<TestSuiteExecutionVO>> groupedEntry : grouped.entrySet()) {
        List<String> aggregatedByValues = new ArrayList<>(entry.getAggregatedByValues());
        aggregatedByValues.add(groupedEntry.getKey());
        AggregatedEntry newEntry = AggregatedEntry.builder()
            .aggregatedByValues(aggregatedByValues)
            .original(groupedEntry.getValue())
            .build();
        result.add(newEntry);
      }
    }

    return result;
  }

  @Value
  @Builder(toBuilder = true)
  private static class AggregatedEntry {

    List<String> aggregatedByValues;

    List<TestSuiteExecutionVO> original;

    AggregatedReportEntry toReportEntry() {

      int totalTestCases = 0;
      int passedTestCases = 0;
      int failedTestCases = 0;
      int skippedTestCases = 0;
      int testCasesWithError = 0;
      long duration = 0;
      List<Map.Entry<String, Long>> entries = new ArrayList<>();

      for (TestSuiteExecutionVO suite : original) {
        totalTestCases += suite.getTestCasesTotal();
        passedTestCases += suite.getTestCasesPassed();
        failedTestCases += suite.getTestCasesFailed();
        skippedTestCases += suite.getTestCasesSkipped();
        testCasesWithError += suite.getTestCasesWithError();
        duration += suite.getDuration();
        entries.add(new SimpleEntry<>(suite.getIdName(), suite.getIdTime()));
      }

      return AggregatedReportEntry.builder()
          .testSuiteExecutionIds(entries)
          .testCasesTotal(totalTestCases)
          .testCasesPassed(passedTestCases)
          .testCasesSkipped(skippedTestCases)
          .testCasesFailed(failedTestCases)
          .testCasesWithError(testCasesWithError)
          .duration(duration)
          .aggregatedByValues(getAggregatedByValues())
          .build();
    }
  }
}
