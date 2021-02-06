package com.github.sekkycodes.testresultserver.repositories;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.domain.TestCase;
import com.github.sekkycodes.testresultserver.domain.TestSuite;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestSuiteRepositoryIT {

  @Autowired
  TestSuiteRepository sut;

  private TestSuite storedTestSuite;

  @BeforeEach
  void beforeEach() {
    TestSuite testSuite = FixtureHelper.buildTestSuite();
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
    // Given another name is set for a stored test suite
    String newName = UUID.randomUUID().toString();
    storedTestSuite.setName(newName);
    // And an additional test case is added
    TestCase testCase = FixtureHelper.buildTestCase();
    String testCaseName = UUID.randomUUID().toString();
    testCase.setName(testCaseName);
    storedTestSuite.addTestCase(testCase);

    // When the test suite is saved
    sut.save(storedTestSuite);
    Optional<TestSuite> result = sut.findById(storedTestSuite.getId());

    // Then the stored test suite has the given name
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getName()).isEqualTo(newName);
    // And an additional test case
    assertThat(result.get().getTestCases().stream().map(TestCase::getName).collect(Collectors.toSet())).contains(testCaseName);
  }
}
