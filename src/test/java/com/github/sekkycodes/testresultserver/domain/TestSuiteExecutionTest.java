package com.github.sekkycodes.testresultserver.domain;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestSuiteExecutionTest {

  private TestSuiteExecution sut;

  @BeforeEach
  void beforeEach() {
    sut = FixtureHelper.buildTestSuiteExecution();
  }

  @Nested
  class ToValueObject {

    @Test
    void convertsDomainObjectToValueObject() {
      TestSuiteExecutionVO vo = sut.toValueObject();

      assertThat(vo.getIdName()).isEqualTo(sut.getId().getName());
      assertThat(vo.getIdTime()).isEqualTo(sut.getId().getTime());
      assertThat(vo.getProject()).isEqualTo(sut.getProject());
      assertThat(vo.getTestType()).isEqualTo(sut.getTestType());
      assertThat(vo.getDuration()).isEqualTo(sut.getDuration());
      assertThat(vo.getTestCasesTotal()).isEqualTo(sut.getTestCasesTotal());
      assertThat(vo.getTestCasesPassed()).isEqualTo(sut.getTestCasesPassed());
      assertThat(vo.getTestCasesSkipped()).isEqualTo(sut.getTestCasesSkipped());
      assertThat(vo.getTestCasesFailed()).isEqualTo(sut.getTestCasesFailed());
      assertThat(vo.getTestCasesWithError()).isEqualTo(sut.getTestCasesWithError());
    }
  }
}
