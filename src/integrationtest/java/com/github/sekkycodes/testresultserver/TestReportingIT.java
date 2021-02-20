package com.github.sekkycodes.testresultserver;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.controllers.ReportingController;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregateBy;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport.AggregatedReportEntry;
import com.github.sekkycodes.testresultserver.vo.reporting.Filter;
import com.github.sekkycodes.testresultserver.vo.reporting.ReportRequest;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class TestReportingIT {

  private static final String DUMMY_PROJECT = "project1";

  @Autowired
  ReportingController reportingController;

  @Autowired
  TestSuiteExecutionRepository testSuiteExecutionRepository;

  @AfterEach
  void afterEach() {
    testSuiteExecutionRepository.deleteAll();
  }

  @Nested
  class AggregateByTestType {

    private int testSuitesAddedToRepo = 0;

    @Test
    void createReportAcrossAllSuitesInProjectAggregatedByTestType() {

      // arrange
      addTest("Unit");
      addTest("Unit");
      TestSuiteExecutionVO integrationSuite1 = addTest("Integration");

      ReportRequest request = ReportRequest.builder()
          .filter(Filter.builder()
              .projectName(DUMMY_PROJECT)
              .build())
          .aggregations(
              Collections.singletonList(AggregateBy.TEST_TYPE))
          .build();

      // act
      ResponseEntity<AggregatedReport> response = reportingController.aggregatedReport(request);

      // assert
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      AggregatedReport report = response.getBody();
      assertThat(report.getAggregationDimensions().size()).isEqualTo(1);
      assertThat(report.getAggregationDimensions().get(0)).isEqualTo(AggregateBy.TEST_TYPE);
      assertThat(report.getEntries().size()).isEqualTo(2);
      AggregatedReportEntry unitEntry = getEntryByType(report, "Unit");
      assertThat(unitEntry.getAggregatedByValues().size()).isEqualTo(1);
      assertThat(unitEntry.getTestSuiteExecutionIds().size()).isEqualTo(2);
      AggregatedReportEntry integrationEntry = getEntryByType(report, "Integration");
      assertThat(integrationEntry.getTestSuiteExecutionIds().size()).isEqualTo(1);
      assertThat(integrationEntry.getTestSuiteExecutionIds().get(0).getKey())
          .isEqualTo(integrationSuite1.getIdName());
    }

    private AggregatedReportEntry getEntryByType(AggregatedReport report, String testType) {
      Optional<AggregatedReportEntry> entry = report.getEntries().stream()
          .filter(e -> testType.equalsIgnoreCase(e.getAggregatedByValues().get(0))).findFirst();
      assertThat(entry.isPresent()).isTrue();
      return entry.get();
    }

    private TestSuiteExecutionVO addTest(String testType) {
      TestSuiteExecution testSuiteExecution = FixtureHelper.buildTestSuiteExecution();
      testSuiteExecution.setId(
          new TimeNamePK("someSuite", testSuiteExecution.getDuration() + testSuitesAddedToRepo++));
      testSuiteExecution.setProject(DUMMY_PROJECT);
      testSuiteExecution.setTestType(testType);

      return testSuiteExecutionRepository.save(testSuiteExecution)
          .toValueObject();
    }

  }
}
