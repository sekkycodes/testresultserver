package com.github.sekkycodes.testresultserver.services;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.TestCaseExecutionVO;
import com.github.sekkycodes.testresultserver.vo.requests.TestCaseFilter;
import com.querydsl.core.types.Predicate;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestCaseRetrieverTest {

  @Mock
  TestSuiteExecutionRepository testSuiteExecutionRepository;

  private TestCaseRetriever sut;

  @Nested
  class RetrieveByResultAndDate {

    @BeforeEach
    void beforeEach() {
      TestSuiteExecution testSuiteExecution = FixtureHelper.buildTestSuiteExecution();
      TestCaseExecution failed = FixtureHelper.buildTestCaseExecution();
      failed.setResult(TestResult.FAILED);

      when(testSuiteExecutionRepository
          .findAll(Mockito.<Predicate>any()))
          .thenReturn(Collections.singletonList(testSuiteExecution));

      sut = new TestCaseRetriever(testSuiteExecutionRepository);
    }

    @Test
    void retrievesSetOfTestCases() {
      Set<TestCaseExecutionVO> testCases = sut
          .retrieveByFilter(TestCaseFilter.builder().build());

      assertThat(testCases.size()).isEqualTo(1);
      assertThat(testCases.iterator().next().getTestResult())
          .isEqualTo(TestResult.PASSED.toString());
    }
  }
}
