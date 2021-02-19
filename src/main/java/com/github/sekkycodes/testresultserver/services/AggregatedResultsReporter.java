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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Creates aggregated test reports
 */
@Service
public class AggregatedResultsReporter {

  private final TestSuiteExecutionRepository testSuiteExecutionRepository;
  private final ReportConverter reportConverter;

  @Autowired
  public AggregatedResultsReporter(TestSuiteExecutionRepository testSuiteExecutionRepository,
      ReportConverter reportConverter) {

    this.testSuiteExecutionRepository = Objects.requireNonNull(testSuiteExecutionRepository);
    this.reportConverter = Objects.requireNonNull(reportConverter);
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

    List<TestSuiteExecutionVO> limited = limit(suiteExecutions, filter, aggregateBys);

    List<AggregatedReportEntry> entries = limited.stream()
        .map(reportConverter::toAggregatedReportEntry)
        .collect(Collectors.toList());

    List<AggregatedReportEntry> aggregated = aggregate(entries, aggregateBys);
    return AggregatedReport.builder()
        .aggregationDimensions(new ArrayList<>(aggregateBys))
        .entries(aggregated)
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

  private List<TestSuiteExecutionVO> limit(List<TestSuiteExecutionVO> suiteExecutions,
      Filter filter,
      List<AggregateBy> aggregateBys) {

    if (filter.getLatestEntries() > 0) {

      if (aggregateBys.contains(AggregateBy.DATE)) {
        // TODO: limit days
      } else {
        // TODO: limit entries
      }
    }

    return suiteExecutions;
  }

  private List<AggregatedReportEntry> aggregate(List<AggregatedReportEntry> entries,
      List<AggregateBy> aggregateBys) {

    if(aggregateBys.isEmpty()) {
      return entries;
    }

    List<AggregatedReportEntry> subAggregated = aggregate(entries,
        aggregateBys.stream().skip(1).collect(
            Collectors.toList()));

    AggregateBy by = aggregateBys.get(0);

    switch(by) {
      case DATE:
        return aggregateByDate(entries);
      case LABEL:
        return aggregateByLabels(entries);
      case TEST_TYPE:
        return aggregateByTestType(entries);
      default:
        throw new IllegalStateException("no support for aggregation by: " + by.name());
    }
  }

  private List<AggregatedReportEntry> aggregateByDate(List<AggregatedReportEntry> entries) {
    // TODO
    return entries;
  }

  private List<AggregatedReportEntry> aggregateByLabels(List<AggregatedReportEntry> entries) {
    // TODO
    return entries;
  }

  private List<AggregatedReportEntry> aggregateByTestType(List<AggregatedReportEntry> entries) {
    // TODO
    return entries;
  }
}
