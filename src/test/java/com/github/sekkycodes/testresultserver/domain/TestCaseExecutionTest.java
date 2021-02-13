package com.github.sekkycodes.testresultserver.domain;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.TestCaseExecutionVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestCaseExecutionTest {

  private TestCaseExecution sut;

  @BeforeEach
  void beforeEach() {
    sut = FixtureHelper.buildTestCaseExecution();
  }

  @Nested
  class ToValueObject {

    @Test
    void convertsDomainObjectToValueObject() {
      TestCaseExecutionVO vo = sut.toValueObject();

      assertThat(vo.getIdName()).isEqualTo(sut.getId().getName());
      assertThat(vo.getIdTime()).isEqualTo(sut.getId().getTime());
      assertThat(vo.getTestResult()).isEqualTo(sut.getResult().name());
      assertThat(vo.getDuration()).isEqualTo(sut.getDuration());
    }
  }
}
