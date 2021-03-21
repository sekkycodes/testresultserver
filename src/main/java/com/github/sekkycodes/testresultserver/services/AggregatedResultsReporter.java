package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.domain.QTestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.utils.DateFormatter;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregateBy;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport.AggregatedReportEntry;
import com.github.sekkycodes.testresultserver.vo.requests.TestSuiteFilter;
import com.google.common.base.Strings;
import com.querydsl.core.BooleanBuilder;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.Builder;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Creates aggregated test reports
 */
@Service
public class AggregatedResultsReporter {

  private final TestSuiteExecutionRepository testSuiteExecutionRepository;
  private final Clock clock;

  @Autowired
  public AggregatedResultsReporter(
      TestSuiteExecutionRepository testSuiteExecutionRepository,
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
  public AggregatedReport report(TestSuiteFilter filter, List<AggregateBy> aggregateBys) {
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

  private List<TestSuiteExecution> getSuiteExecutionsByFilter(TestSuiteFilter filter) {

    QTestSuiteExecution qTestSuiteExecution = new QTestSuiteExecution("test-suite-execution");
    BooleanBuilder booleanBuilder = new BooleanBuilder();

    if (!Strings.isNullOrEmpty(filter.getProjectName())) {
      booleanBuilder.and(qTestSuiteExecution.project.eq(filter.getProjectName()));
    }

    if (!Strings.isNullOrEmpty(filter.getTestType())) {
      booleanBuilder.and(qTestSuiteExecution.testType.eq(filter.getTestType()));
    }

    if (!Strings.isNullOrEmpty(filter.getEnvironment())) {
      booleanBuilder.and(qTestSuiteExecution.environment.eq(filter.getEnvironment()));
    }

    for (String label : filter.getLabels()) {
      booleanBuilder.and(qTestSuiteExecution.labels.contains(label));
    }

    Iterable<TestSuiteExecution> matched = testSuiteExecutionRepository
        .findAll(Objects.requireNonNull(booleanBuilder));

    return StreamSupport.stream(matched.spliterator(), false)
        .collect(Collectors.toList());
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
        return aggregateByFunction(entries,
            suite -> DateFormatter.toFormattedDate(suite.getIdTime()));
      case ENVIRONMENT:
        return aggregateByFunction(entries, TestSuiteExecutionVO::getEnvironment);
      case TEST_TYPE:
        return aggregateByFunction(entries, TestSuiteExecutionVO::getTestType);
      default:
        throw new IllegalStateException("no support for aggregation by: " + aggregateBys.name());
    }
  }

  private List<AggregatedEntry> aggregateByFunction(List<AggregatedEntry> entries,
      Function<TestSuiteExecutionVO, String> groupingFunction) {

    List<AggregatedEntry> result = new ArrayList<>();

    for (AggregatedEntry entry : entries) {
      Map<String, List<TestSuiteExecutionVO>> grouped = entry.getOriginal().stream()
          .collect(Collectors.groupingBy(groupingFunction));
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
