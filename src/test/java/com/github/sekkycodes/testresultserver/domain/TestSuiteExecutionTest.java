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
      assertThat(vo.getDuration()).isEqualTo(sut.getDuration());
    }
  }
}
