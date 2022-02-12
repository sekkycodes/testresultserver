package com.github.sekkycodes.testresultserver.services;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.TestBase;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class LatestResultsReporterTest extends TestBase {

  private LatestResultsReporter sut;

  @Mock
  TestSuiteExecutionRepository testSuiteExecutionRepository;

  private TestSuiteExecution dummyTestSuiteExecution;

  @BeforeEach
  void beforeEach() {
    dummyTestSuiteExecution = FixtureHelper.buildTestSuiteExecution();

    when(testSuiteExecutionRepository.findAllByProject(anyString()))
        .thenReturn(Collections.singletonList(dummyTestSuiteExecution));

    sut = new LatestResultsReporter(testSuiteExecutionRepository);
  }

  @Nested
  class GetAllLatest {

    @Test
    void retrievesListOfLatestResultsAndMapsThemToValueObjects() {
      Collection<TestSuiteExecutionVO> result = sut.getAllLatest("project01");

      assertThat(result.isEmpty()).isFalse();
      assertThat(result.iterator().next().getIdName())
          .isEqualTo(dummyTestSuiteExecution.getId().getName());
    }
  }
}
