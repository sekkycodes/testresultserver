package com.github.sekkycodes.testresultserver.services;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.TestBase;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
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

class LatestResultsReporterTest extends TestBase {

  private LatestResultsReporter sut;

  @Mock
  MongoTemplate mongoTemplate;

  @Mock
  AggregationResults<TestSuiteExecution> aggregationResults;

  private TestSuiteExecution dummyTestSuiteExecution;

  @BeforeEach
  void beforeEach() {
    dummyTestSuiteExecution = FixtureHelper.buildTestSuiteExecution();

    when(aggregationResults.getMappedResults())
        .thenReturn(Collections.singletonList(FixtureHelper.buildTestSuiteExecution()));

    when(mongoTemplate.aggregate(
        Mockito.<TypedAggregation<TestSuiteExecution>>any(),
        eq(TestSuiteExecution.class)))
        .thenReturn(aggregationResults);

    sut = new LatestResultsReporter(mongoTemplate);
  }

  @Nested
  class GetAllLatest {

    @Test
    void retrievesListOfLatestResultsAndMapsThemToValueObjects() {
      Collection<TestSuiteExecutionVO> result = sut.getAllLatest();

      assertThat(result.isEmpty()).isFalse();
      assertThat(result.iterator().next().getIdName())
          .isEqualTo(dummyTestSuiteExecution.getId().getName());
    }
  }
}
