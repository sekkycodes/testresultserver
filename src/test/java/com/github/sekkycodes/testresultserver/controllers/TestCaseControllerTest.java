package com.github.sekkycodes.testresultserver.controllers;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.services.TestCaseRetriever;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.TestCaseExecutionVO;
import com.github.sekkycodes.testresultserver.vo.requests.TestCaseFilter;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class TestCaseControllerTest {

  @Mock
  private TestCaseRetriever testCaseRetriever;

  private TestCaseController sut;

  private TestCaseExecutionVO dummyTestCaseExecution;

  @Nested
  class GetByDateAndResult {

    @BeforeEach
    void beforeEach() {
      dummyTestCaseExecution = FixtureHelper.buildTestCaseExecution().toValueObject();

      sut = new TestCaseController(testCaseRetriever);
    }

    @Test
    void returnsBadRequestIfResultCannotBeMapped() {
      TestCaseFilter filter = TestCaseFilter.builder().result("invalid string").build();

      ResponseEntity<Set<TestCaseExecutionVO>> response = sut.filter(filter);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void returnsBadRequestIfDateCannotBeMapped() {
      TestCaseFilter filter = TestCaseFilter.builder().date("invalid string").build();

      ResponseEntity<Set<TestCaseExecutionVO>> response = sut.filter(filter);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void returnsSetOfTestCaseExecutions() {
      when(testCaseRetriever.retrieveByFilter(any()))
          .thenReturn(Collections.singleton(dummyTestCaseExecution));

      ResponseEntity<Set<TestCaseExecutionVO>> response = sut
          .filter(TestCaseFilter.builder().build());

      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().size()).isEqualTo(1);
      assertThat(response.getBody().iterator().next().getIdName())
          .isEqualTo(dummyTestCaseExecution.getIdName());
    }
  }
}
