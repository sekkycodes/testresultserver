package com.github.sekkycodes.testresultserver.domain;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.TestSuiteVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TestSuiteTest {

  private TestSuite sut;

  @BeforeEach
  void beforeEach() {
    sut = FixtureHelper.buildTestSuite().build();
  }

  @Nested
  class ToValueObject {

    @Test
    void convertsDomainObjectToValueObject() {
      TestSuiteVO vo = sut.toValueObject();

      assertThat(vo.getId()).isEqualTo(sut.getId());
      assertThat(vo.getName()).isEqualTo(sut.getName());
    }
  }
}
