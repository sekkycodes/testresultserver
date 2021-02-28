package com.github.sekkycodes.testresultserver.controllers;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.TestBase;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.services.AggregatedResultsReporter;
import com.github.sekkycodes.testresultserver.services.LatestResultsReporter;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ReportingControllerTest extends TestBase {

  private ReportingController sut;

  @Mock
  TestSuiteExecutionRepository testSuiteExecutionRepository;

  @Mock
  MongoTemplate mongoTemplate;

  @Mock
  AggregationResults<TestSuiteExecution> aggregationResults;

  private TestSuiteExecution dummyTestSuiteExecution;

  @BeforeEach
  void beforeEach() {
    dummyTestSuiteExecution = FixtureHelper.buildTestSuiteExecution();

    when(aggregationResults.getMappedResults())
        .thenReturn(Collections.singletonList(dummyTestSuiteExecution));

    when(mongoTemplate.aggregate(
        Mockito.<TypedAggregation<TestSuiteExecution>>any(),
        eq(TestSuiteExecution.class)))
        .thenReturn(aggregationResults);

    LatestResultsReporter latestResultsReporter = new LatestResultsReporter(mongoTemplate);

    AggregatedResultsReporter aggregatedResultsReporter = new AggregatedResultsReporter(
        testSuiteExecutionRepository, FixtureHelper.FIXED_CLOCK);

    sut = new ReportingController(latestResultsReporter, aggregatedResultsReporter);
  }

  @Nested
  class GetAllLatestSuiteResults {

    @Test
    void returnsLatestSuiteResults() {
      ResponseEntity<Collection<TestSuiteExecutionVO>> response = sut.getAllLatestSuiteResults();

      assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
      assertThat(response.getBody()).isNotNull();
      Collection<TestSuiteExecutionVO> testResults = response.getBody();
      assertThat(testResults.isEmpty()).isFalse();
      assertThat(testResults.iterator().next().getIdName())
          .isEqualTo(dummyTestSuiteExecution.getId().getName());
      assertThat(testResults.iterator().next().getIdTime())
          .isEqualTo(dummyTestSuiteExecution.getId().getTime());
    }
  }
}
