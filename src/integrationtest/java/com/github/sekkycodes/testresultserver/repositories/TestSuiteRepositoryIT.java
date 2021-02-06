package com.github.sekkycodes.testresultserver.repositories;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.domain.TestSuite;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestSuiteRepositoryIT {

  @Autowired
  TestSuiteRepository sut;

  private TestSuite storedTestSuite;

  @BeforeEach
  void beforeEach() {
    TestSuite testSuite = FixtureHelper.buildTestSuite().build();
    storedTestSuite = sut.save(testSuite);
  }

  @AfterEach
  void afterEach() {
    sut.deleteAll();
  }

  @Test
  void storesNewTestSuite() {
    Optional<TestSuite> result = sut.findById(storedTestSuite.getId());

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getName()).isEqualTo(storedTestSuite.getName());
  }

  @Test
  void updatesStoredTestSuite() {
    String newName = UUID.randomUUID().toString();
    storedTestSuite.setName(newName);

    sut.save(storedTestSuite);
    Optional<TestSuite> result = sut.findById(storedTestSuite.getId());

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getName()).isEqualTo(newName);
  }
}
