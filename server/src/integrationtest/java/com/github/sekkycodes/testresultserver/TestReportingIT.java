package com.github.sekkycodes.testresultserver;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.controllers.ReportingController;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.utils.DateFormatter;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregateBy;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport.AggregatedReportEntry;
import com.github.sekkycodes.testresultserver.vo.requests.ReportRequest;
import com.github.sekkycodes.testresultserver.vo.requests.TestSuiteFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class TestReportingIT extends IntegrationTestBase {

  private static final String DUMMY_PROJECT = "project1";

  @Autowired
  ReportingController reportingController;

  @Nested
  class AggregateByTestType {

    /**
     * Aggregations along only one dimension are covered by unit test. This IT verifies that
     * aggregation along multiple dimensions works. Test suite executions older than 14 days must
     * also be ignored by the reporter.
     */
    @Test
    void createReportAcrossAllSuitesInProjectAggregatedByTestTypeDateAndEnvironmentForLast14Days() {

      // arrange
      setUpTestData();
      List<AggregateBy> aggregations = new ArrayList<>();
      aggregations.add(AggregateBy.TEST_TYPE);
      aggregations.add(AggregateBy.DATE);
      aggregations.add(AggregateBy.ENVIRONMENT);
      ReportRequest request = ReportRequest.builder()
          .filter(TestSuiteFilter.builder()
              .projectName(DUMMY_PROJECT)
              .daysBack(14)
              .build())
          .aggregations(aggregations)
          .build();

      // act
      ResponseEntity<AggregatedReport> response = reportingController.aggregatedReport(request);

      // assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      AggregatedReport report = response.getBody();
      assertThat(report.getAggregationDimensions().size()).isEqualTo(3);
      assertThat(report.getAggregationDimensions()).isEqualTo(aggregations);
      assertThat(report.getEntries().size()).isEqualTo(7);
      assertThat(report.getEntries().get(0).getAggregatedByValues().size()).isEqualTo(3);

      List<String> aggregatedByValues = Arrays.asList("Unit", "2020-01-27", "local");
      Optional<AggregatedReportEntry> unitEntry = report.getEntries().stream()
          .filter(e -> e.getAggregatedByValues().containsAll(aggregatedByValues)).findFirst();
      assertThat(unitEntry.isPresent()).isTrue();
      assertThat(unitEntry.get().getTestSuiteExecutionIds().size()).isEqualTo(2);
    }

    @Test
    void createsReportFiltersOutUnwantedTestTypesAndEnvironments() {
      // arrange
      setUpTestData();
      addTest("Unit", "local", "2020-01-29", Collections.singleton("label12"));
      addTest("Unit", "staging", "2020-01-29", Collections.singleton("label11"));
      addTest("Unit", "staging", "2020-01-29", Collections.singleton("label12"));
      ReportRequest request = ReportRequest.builder()
          .filter(TestSuiteFilter.builder()
              .testType("Unit")
              .daysBack(14)
              .labels(Collections.singletonList("label12"))
              .environment("staging")
              .build())
          .aggregations(Collections.singletonList(AggregateBy.DATE))
          .build();

      // act
      ResponseEntity<AggregatedReport> response = reportingController.aggregatedReport(request);

      // assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      AggregatedReport report = response.getBody();
      assertThat(report.getEntries().size()).isEqualTo(1);
      assertThat(report.getEntries().get(0).getTestSuiteExecutionIds().size()).isEqualTo(1);
    }

    private void setUpTestData() {
      // note: the fixed clock in the FixtureHelper is set to 2020-01-29 (that's 'today')
      addTest("Unit", "local", "2020-01-29");
      addTest("Unit", "local", "2020-01-29");
      addTest("Unit", "local", "2020-01-27");
      addTest("Unit", "local", "2020-01-27");
      addTest("Unit", "local", "2020-01-13");
      addTest("Unit", "local", "2020-01-10");

      addTest("E2E", "dev", "2020-01-29");
      addTest("E2E", "dev", "2020-01-29");
      addTest("E2E", "prod", "2020-01-27");
      addTest("E2E", "prod", "2020-01-25");
      addTest("E2E", "dev", "2020-01-25");
      addTest("E2E", "test", "2020-01-25");

      List<TestSuiteExecutionVO> result = testSuiteExecutionRepository.findAll().stream()
          .map(TestSuiteExecution::toValueObject).collect(Collectors.toList());

      assertThat(result.size()).isEqualTo(12);
    }

    private void addTest(String testType, String environment, String date) {
      addTest(testType, environment, date, Collections.emptySet());
    }

    private void addTest(String testType, String environment, String date, Set<String> labels) {
      TestSuiteExecution testSuiteExecution = FixtureHelper.buildTestSuiteExecution();
      long dateAsMillis = DateFormatter.fromDate(date);
      testSuiteExecution
          .setId(new TimeNamePK(UUID.randomUUID().toString(), dateAsMillis));
      testSuiteExecution.setProject(DUMMY_PROJECT);
      testSuiteExecution.setTestType(testType);
      testSuiteExecution.setEnvironment(environment);
      testSuiteExecution.setLabels(labels);

      testSuiteExecutionRepository.save(testSuiteExecution);
    }
  }
}
