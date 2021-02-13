package com.github.sekkycodes.testresultserver.repositories;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestCaseExecutionRepositoryIT {

  @Autowired
  TestCaseExecutionRepository sut;

  private TestCaseExecution storedExecution;

  @BeforeEach
  void beforeEach() {
    storedExecution = FixtureHelper.buildTestCaseExecution();
    sut.save(storedExecution);
  }

  @AfterEach
  void afterEach() {
    sut.deleteAll();
  }

  @Test
  void storesNewTestCase() {
    Optional<TestCaseExecution> result = sut
        .findByIdNameAndIdTime(storedExecution.getId().getName(), storedExecution.getId()
            .getTime());

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getId().getName()).isEqualTo(storedExecution.getId().getName());
    assertThat(result.get().getId().getTime()).isEqualTo(storedExecution.getId().getTime());
    assertThat(result.get().getSuiteName()).isEqualTo(storedExecution.getSuiteName());
    assertThat(result.get().getDuration()).isEqualTo(storedExecution.getDuration());
  }

  @Test
  void findsAllByTestSuite() {
    List<TestCaseExecution> result = sut.findAllBySuiteNameAndIdTime(storedExecution.getSuiteName(),
        storedExecution.getId().getTime());

    assertThat(result.isEmpty()).isFalse();
    assertThat(result.get(0).getId().getName()).isEqualTo(storedExecution.getId().getName());
  }

  @Test
  void findsAllTestCasesByName() {
    List<TestCaseExecution> result = sut.findAllByIdName(storedExecution.getId().getName());

    assertThat(result.isEmpty()).isFalse();
    assertThat(result.get(0).getId().getName()).isEqualTo(storedExecution.getId().getName());
  }
}
