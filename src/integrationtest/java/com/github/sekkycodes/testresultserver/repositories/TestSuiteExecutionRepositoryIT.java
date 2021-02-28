package com.github.sekkycodes.testresultserver.repositories;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.IntegrationTestBase;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TestSuiteExecutionRepositoryIT extends IntegrationTestBase {

  @Autowired
  TestSuiteExecutionRepository sut;

  private TestSuiteExecution storedExecution;

  @BeforeEach
  void beforeEach() {
    storedExecution = FixtureHelper.buildTestSuiteExecution();
    sut.save(storedExecution);
  }

  @AfterEach
  void afterEach() {
    sut.deleteAll();
  }

  @Test
  void storesNewTestSuite() {
    Optional<TestSuiteExecution> result = sut
        .findByIdNameAndIdTime(storedExecution.getId().getName(), storedExecution.getId()
            .getTime());

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getId().getName()).isEqualTo(storedExecution.getId().getName());
    assertThat(result.get().getId().getTime()).isEqualTo(storedExecution.getId().getTime());
    assertThat(result.get().getDuration()).isEqualTo(storedExecution.getDuration());
  }

  @Test
  void findsTestSuiteByName() {
    List<TestSuiteExecution> result = sut.findAllByIdName(storedExecution.getId().getName());

    assertThat(result.isEmpty()).isFalse();
    assertThat(result.get(0).getId().getName()).isEqualTo(storedExecution.getId().getName());
  }
}
