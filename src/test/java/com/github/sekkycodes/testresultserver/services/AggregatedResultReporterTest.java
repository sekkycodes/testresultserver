package com.github.sekkycodes.testresultserver.services;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.TestBase;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.utils.DateFormatter;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregateBy;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport.AggregatedReportEntry;
import com.github.sekkycodes.testresultserver.vo.reporting.Filter;
import com.querydsl.core.types.Predicate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

class AggregatedResultReporterTest extends TestBase {

  private AggregatedResultsReporter sut;

  private TestSuiteExecution unitTestSuite1;
  private TestSuiteExecution unitTestSuite2;
  private TestSuiteExecution integrationTestSuite1;

  private final AtomicInteger addedTestSuites = new AtomicInteger(1000);

  private static final String DUMMY_PROJECT = "project01";

  @Mock
  TestSuiteExecutionRepository testSuiteExecutionRepository;

  @BeforeEach
  void beforeEach() {
    List<TestSuiteExecution> testSuiteList = new ArrayList<>();
    unitTestSuite1 = createDummyTest("Unit");
    unitTestSuite2 = createDummyTest("Unit");
    integrationTestSuite1 = createDummyTest("Integration");
    testSuiteList.add(unitTestSuite1);
    testSuiteList.add(unitTestSuite2);
    testSuiteList.add(integrationTestSuite1);

    when(testSuiteExecutionRepository.findAll(Mockito.<Predicate>any()))
        .thenReturn(testSuiteList);

    sut = new AggregatedResultsReporter(testSuiteExecutionRepository, FixtureHelper.FIXED_CLOCK);
  }

  @Nested
  class AggregateByDate {

    @Test
    void aggregatesByDate() {
      // arrange
      TestSuiteExecution suiteToday1 = createExecutionAtTime(
          FixtureHelper.FIXED_TIMESTAMP.toEpochMilli());
      TestSuiteExecution suiteToday2 = createExecutionAtTime(
          FixtureHelper.FIXED_TIMESTAMP.minus(1, ChronoUnit.HOURS).toEpochMilli());
      TestSuiteExecution suiteYesterday1 = createExecutionAtTime(
          FixtureHelper.FIXED_TIMESTAMP.minus(1, ChronoUnit.DAYS).toEpochMilli());
      List<TestSuiteExecution> testSuiteList = new ArrayList<>();
      testSuiteList.add(suiteToday1);
      testSuiteList.add(suiteToday2);
      testSuiteList.add(suiteYesterday1);

      when(testSuiteExecutionRepository.findAll(Mockito.<Predicate>any()))
          .thenReturn(testSuiteList);

      // act
      AggregatedReport result = sut
          .report(Filter.builder().build(), Collections.singletonList(AggregateBy.DATE));

      // assert
      assertThat(result.getAggregationDimensions().size()).isEqualTo(1);
      assertThat(result.getAggregationDimensions().get(0)).isEqualTo(AggregateBy.DATE);
      assertThat(result.getEntries().size()).isEqualTo(2);
      AggregatedReportEntry todayEntry =
          getEntryByFirstAggregatedDimensionValue(result.getEntries(), FixtureHelper.TODAY_DATE);
      assertThat(todayEntry.getTestSuiteExecutionIds().size()).isEqualTo(2);
      AggregatedReportEntry yesterdayEntry =
          getEntryByFirstAggregatedDimensionValue(result.getEntries(), yesterday());
      assertThat(yesterdayEntry.getTestSuiteExecutionIds().size()).isEqualTo(1);
      assertThat(yesterdayEntry.getTestSuiteExecutionIds().get(0).getKey())
          .isEqualTo(suiteYesterday1.getId().getName());
    }

    private TestSuiteExecution createExecutionAtTime(long timestamp) {
      TestSuiteExecution suite = FixtureHelper.buildTestSuiteExecution();
      suite.setId(new TimeNamePK(UUID.randomUUID().toString(), timestamp));
      return suite;
    }

    private String yesterday() {
      Instant yesterdayInstant = FixtureHelper.FIXED_TIMESTAMP.minus(1, ChronoUnit.DAYS);
      return DateFormatter.toFormattedDate(yesterdayInstant.toEpochMilli());
    }
  }

  @Nested
  class AggregateByEnvironment {

    @Test
    void aggregatesByEnvironment() {
      // arrange
      TestSuiteExecution suiteDev1 = createExecutionForEnvironment("dev");
      TestSuiteExecution suiteDev2 = createExecutionForEnvironment("dev");
      TestSuiteExecution suiteProd1 = createExecutionForEnvironment("prod");
      List<TestSuiteExecution> testSuiteList = new ArrayList<>();
      testSuiteList.add(suiteDev1);
      testSuiteList.add(suiteDev2);
      testSuiteList.add(suiteProd1);

      when(testSuiteExecutionRepository.findAll(Mockito.<Predicate>any()))
          .thenReturn(testSuiteList);

      // act
      AggregatedReport result = sut
          .report(Filter.builder().build(), Collections.singletonList(AggregateBy.ENVIRONMENT));

      // assert
      assertThat(result.getAggregationDimensions().size()).isEqualTo(1);
      assertThat(result.getAggregationDimensions().get(0)).isEqualTo(AggregateBy.ENVIRONMENT);
      assertThat(result.getEntries().size()).isEqualTo(2);
      AggregatedReportEntry todayEntry =
          getEntryByFirstAggregatedDimensionValue(result.getEntries(), "dev");
      assertThat(todayEntry.getTestSuiteExecutionIds().size()).isEqualTo(2);
      AggregatedReportEntry yesterdayEntry =
          getEntryByFirstAggregatedDimensionValue(result.getEntries(), "prod");
      assertThat(yesterdayEntry.getTestSuiteExecutionIds().size()).isEqualTo(1);
      assertThat(yesterdayEntry.getTestSuiteExecutionIds().get(0).getKey())
          .isEqualTo(suiteProd1.getId().getName());
    }

    private TestSuiteExecution createExecutionForEnvironment(String environment) {
      TestSuiteExecution suite = FixtureHelper.buildTestSuiteExecution();
      suite.setTestType("E2E");
      suite.setEnvironment(environment);
      return suite;
    }
  }

  @Nested
  class AggregateByTestType {

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
      AggregatedReportEntry unitEntry = getEntryByFirstAggregatedDimensionValue(result.getEntries(),
          unitTestSuite1.getTestType());
      assertThat(unitEntry.getTestSuiteExecutionIds().size()).isEqualTo(2);
      AggregatedReportEntry integrationEntry = getEntryByFirstAggregatedDimensionValue(
          result.getEntries(), integrationTestSuite1.getTestType());
      assertThat(integrationEntry.getTestSuiteExecutionIds().size()).isEqualTo(1);
    }
  }

  @Nested
  class Report {

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
      AggregatedReportEntry unitEntry = getEntryByFirstAggregatedDimensionValue(result.getEntries(),
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

    @Test
    void considersOnlyRecentResultsWhenFilterIsConfiguredTo() {
      // arrange
      TestSuiteExecution tooOldTestSuite = createDummyTest("Unit");
      // set suite execution back 6 days
      tooOldTestSuite.setId(new TimeNamePK(UUID.randomUUID().toString(),
          FixtureHelper.FIXED_CLOCK.instant().minus(6, ChronoUnit.DAYS).toEpochMilli()));

      TestSuiteExecution newTestSuite = createDummyTest("Unit");
      // set suite execution back 4 days
      newTestSuite.setId(new TimeNamePK(UUID.randomUUID().toString(),
          FixtureHelper.FIXED_CLOCK.instant().minus(4, ChronoUnit.DAYS).toEpochMilli()));

      List<TestSuiteExecution> suiteExecutions = new ArrayList<>();
      suiteExecutions.add(tooOldTestSuite);
      suiteExecutions.add(newTestSuite);
      when(testSuiteExecutionRepository.findAll(Mockito.<Predicate>any()))
          .thenReturn(suiteExecutions);

      Filter filter = Filter.builder()
          .daysBack(5)
          .build();

      // act
      AggregatedReport result = sut
          .report(filter, Collections.singletonList(AggregateBy.TEST_TYPE));

      // assert
      AggregatedReportEntry unitEntry = getEntryByFirstAggregatedDimensionValue(result.getEntries(),
          tooOldTestSuite.getTestType());
      boolean tooOldSuiteFound = unitEntry.getTestSuiteExecutionIds().stream()
          .anyMatch(e -> e.getKey().equals(tooOldTestSuite.getId().getName()));
      assertThat(tooOldSuiteFound).isFalse();
      boolean newSuiteFound = unitEntry.getTestSuiteExecutionIds().stream()
          .anyMatch(e -> e.getKey().equals(newTestSuite.getId().getName()));
      assertThat(newSuiteFound).isTrue();
    }
  }

  private AggregatedReportEntry getEntryByFirstAggregatedDimensionValue(
      List<AggregatedReportEntry> entries, String value) {
    Optional<AggregatedReportEntry> entry = entries.stream().filter(e -> {
      Optional<String> aggregatedValue = e.getAggregatedByValues().stream().skip(0).findFirst();
      if (aggregatedValue.isEmpty()) {
        return false;
      }
      return value.equals(aggregatedValue.get());
    }).findFirst();

    assertThat(entry.isPresent()).isTrue();
    return entry.get();
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
