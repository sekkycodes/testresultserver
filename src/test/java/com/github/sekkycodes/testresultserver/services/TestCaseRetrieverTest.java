package com.github.sekkycodes.testresultserver.services;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.TestCaseExecutionVO;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestCaseRetrieverTest {

  @Mock
  TestSuiteExecutionRepository testSuiteExecutionRepository;

  private TestCaseRetriever sut;

  @Nested
  class RetrieveByResultAndDate {

    private TestSuiteExecution testSuiteExecution;

    @BeforeEach
    void beforeEach() {
      testSuiteExecution = FixtureHelper.buildTestSuiteExecution();
      TestCaseExecution failed = FixtureHelper.buildTestCaseExecution();
      failed.setResult(TestResult.FAILED);

      when(testSuiteExecutionRepository
          .findByExecutionDateAndProjectAndTestType(any(), anyString(), anyString()))
          .thenReturn(Collections.singletonList(testSuiteExecution));

      sut = new TestCaseRetriever(testSuiteExecutionRepository);
    }

    @Test
    void retrievesPassedTestCasesForDate() {
      Set<TestCaseExecutionVO> testCases = sut
          .retrieveByResultAndDate(testSuiteExecution.getExecutionDate(), TestResult.PASSED, "unit",
              "project01");

      assertThat(testCases.size()).isEqualTo(1);
      assertThat(testCases.iterator().next().getTestResult())
          .isEqualTo(TestResult.PASSED.toString());
    }

    @Test
    void returnsNothingInCaseNoMatchForResult() {
      Set<TestCaseExecutionVO> testCases = sut
          .retrieveByResultAndDate(testSuiteExecution.getExecutionDate(), TestResult.SKIPPED,
              "unit", "project01");

      assertThat(testCases).isEmpty();
    }
  }
}
