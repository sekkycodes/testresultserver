package com.github.sekkycodes.testresultserver.repositories;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.domain.TestCase;
import com.github.sekkycodes.testresultserver.domain.TestSuite;
import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.services.FileImportService;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileImportIT {

  @Autowired
  FileImportService sut;

  @Autowired
  TestSuiteRepository testSuiteRepository;

  @Test
  void importToUpdateExistingTestSuite() throws ImportException {
    // Given an existing TestSuite with a TestCase
    TestSuite existingTestSuite = TestSuite.builder()
        .name("com.github.sekkycodes.testresultserver.repositories.TestSuiteRepositoryIT")
        .testCases(Collections.singleton(TestCase.builder().name("testCaseToDelete").build()))
        .build();
    testSuiteRepository.save(existingTestSuite);

    // When importing a JUnit XML with the same TestSuite name, but different TestCases
    sut.importJunitFile(() -> this.getClass().getResourceAsStream("/junit.xml"));

    // Then the existing TestCase is deleted
    assertThat(testSuiteRepository.count()).isEqualTo(1);
    Optional<TestSuite> savedSuite = testSuiteRepository.findByName(existingTestSuite.getName());
    assertThat(savedSuite.isPresent()).isTrue();
    Set<TestCase> testCases = savedSuite.get().getTestCases();
    assertThat(testCases.stream().anyMatch(tc -> tc.getName().equals("testCaseToDelete"))).isFalse();

    // And the new TestCases are added to the existing TestSuite
    assertThat(testCases.stream().anyMatch(tc -> tc.getName().equals("storesNewTestSuite"))).isTrue();
    assertThat(testCases.stream().anyMatch(tc -> tc.getName().equals("updatesStoredTestSuite"))).isTrue();
  }
}
