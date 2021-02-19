package com.github.sekkycodes.testresultserver.services;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.TestBase;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregateBy;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport.AggregatedReportEntry;
import com.github.sekkycodes.testresultserver.vo.reporting.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

class AggregatedResultReporterTest extends TestBase {

  private AggregatedResultsReporter sut;

  private List<TestSuiteExecution> testSuiteList;
  private TestSuiteExecution unitTestSuite1;
  private TestSuiteExecution unitTestSuite2;
  private TestSuiteExecution integrationTestSuite1;
  private AtomicInteger addedTestSuites = new AtomicInteger(1000);

  private static final String DUMMY_PROJECT = "project01";

  @Mock
  TestSuiteExecutionRepository testSuiteExecutionRepository;

  @BeforeEach
  void beforeEach() {
    testSuiteList = new ArrayList<>();
    unitTestSuite1 = createDummyTest("Unit");
    unitTestSuite2 = createDummyTest("Unit");
    integrationTestSuite1 = createDummyTest("Integration");
    testSuiteList.add(unitTestSuite1);
    testSuiteList.add(unitTestSuite2);
    testSuiteList.add(integrationTestSuite1);

    when(testSuiteExecutionRepository.findAll(Mockito.<Specification<TestSuiteExecution>>any()))
        .thenReturn(testSuiteList);

    sut = new AggregatedResultsReporter(testSuiteExecutionRepository);
  }

  @Nested
  class Report {

    @Test
    void aggregatesByTestType() {
      // arrange
      Filter filter = Filter.builder()
          .projectName(DUMMY_PROJECT)
          .build();

      // act
      AggregatedReport result = sut
          .report(filter, Collections.singletonList(AggregateBy.TEST_TYPE));

      // assert
      assertThat(result.getAggregationDimensions().size()).isEqualTo(1);
      assertThat(result.getAggregationDimensions().get(0)).isEqualTo(AggregateBy.TEST_TYPE);
      assertThat(result.getEntries().size()).isEqualTo(2);
      AggregatedReportEntry unitEntry = getEntryByAggregatedDimensionValue(result.getEntries(), 0,
          unitTestSuite1.getTestType());
      assertThat(unitEntry.getTestSuiteExecutionIds().size()).isEqualTo(2);
      AggregatedReportEntry integrationEntry = getEntryByAggregatedDimensionValue(
          result.getEntries(), 0, integrationTestSuite1.getTestType());
      assertThat(integrationEntry.getTestSuiteExecutionIds().size()).isEqualTo(1);
    }

    @Test
    void sumsUnitTestSuitesInAggregatedResult() {
      // arrange
      Filter filter = Filter.builder()
          .projectName(DUMMY_PROJECT)
          .build();

      // act
      AggregatedReport result = sut
          .report(filter, Collections.singletonList(AggregateBy.TEST_TYPE));

      // assert
      AggregatedReportEntry unitEntry = getEntryByAggregatedDimensionValue(result.getEntries(), 0,
          unitTestSuite1.getTestType());
      assertThat(unitEntry.getDuration())
          .isEqualTo(unitTestSuite1.getDuration() + unitTestSuite2.getDuration());
      assertThat(unitEntry.getTestCasesTotal())
          .isEqualTo(unitTestSuite1.getTestCasesTotal() + unitTestSuite2.getTestCasesTotal());
      assertThat(unitEntry.getTestCasesPassed())
          .isEqualTo(unitTestSuite1.getTestCasesPassed() + unitTestSuite2.getTestCasesPassed());
      assertThat(unitEntry.getTestCasesSkipped())
          .isEqualTo(unitTestSuite1.getTestCasesSkipped() + unitTestSuite2.getTestCasesSkipped());
      assertThat(unitEntry.getTestCasesFailed())
          .isEqualTo(unitTestSuite1.getTestCasesFailed() + unitTestSuite2.getTestCasesFailed());
      assertThat(unitEntry.getTestCasesWithError())
          .isEqualTo(
              unitTestSuite1.getTestCasesWithError() + unitTestSuite2.getTestCasesWithError());
    }

    private AggregatedReportEntry getEntryByAggregatedDimensionValue(
        List<AggregatedReportEntry> entries, int dimension, String value) {
      Optional<AggregatedReportEntry> entry = entries.stream().filter(e -> {
        Optional<String> aggrValue = e.getAggregatedByValues().stream().skip(dimension).findFirst();
        if (aggrValue.isEmpty()) {
          return false;
        }
        return value.equals(aggrValue.get());
      }).findFirst();

      assertThat(entry.isPresent()).isTrue();
      return entry.get();
    }
  }

  private TestSuiteExecution createDummyTest(String testType) {
    TestSuiteExecution testSuiteExecution = FixtureHelper.buildTestSuiteExecution();
    testSuiteExecution.setId(
        new TimeNamePK("someSuite",
            testSuiteExecution.getDuration() + addedTestSuites.getAndIncrement()));
    testSuiteExecution.setProject(DUMMY_PROJECT);
    testSuiteExecution.setTestType(testType);

    return testSuiteExecution;
  }
}
