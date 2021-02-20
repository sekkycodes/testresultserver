package com.github.sekkycodes.testresultserver.repositories;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestSuiteExecutionRepositoryIT {

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

  @Test
  void findsLatestTestSuiteExecutionPerSuite() {
    TestSuiteExecution latest = FixtureHelper.buildTestSuiteExecution();
    latest.setId(
        new TimeNamePK(storedExecution.getId().getName(), storedExecution.getId().getTime() + 10L));
    sut.save(latest);

    Collection<TestSuiteExecution> result = sut.findLatestResults();

    Optional<TestSuiteExecution> execution = result.stream()
        .filter(r -> r.getId().equals(latest.getId())).findFirst();
    assertThat(execution.isPresent()).isTrue();
  }
}
