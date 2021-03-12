package com.github.sekkycodes.testresultserver;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.services.FileImportService;
import com.github.sekkycodes.testresultserver.vo.importing.ImportRequest;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TestFileImportIT extends IntegrationTestBase {

  @Autowired
  FileImportService fileImportService;

  // from junit.xml
  private static final String SUITE_NAME = "com.github.sekkycodes.testresultserver.repositories.TestSuiteRepositoryIT";

  private ImportRequest importRequest;

  @BeforeEach
  void beforeEach() {
    importRequest = ImportRequest.builder()
        .testType("unit")
        .project("project01")
        .labels(Collections.singletonList("label1"))
        .executionTimeStamp(1615553404650L)
        .environment("dev")
        .build();
  }

  @Test
  void importsTestSuiteAndTestCasesFromJunitXml() throws ImportException {
    // When importing a JUnit XML
    fileImportService
        .importJunitFile(() -> this.getClass().getResourceAsStream("/junit.xml"), importRequest);

    // Then a new test suite execution is created
    assertThat(testSuiteExecutionRepository.count()).isEqualTo(1);
    List<TestSuiteExecution> savedSuiteExecutions = testSuiteExecutionRepository
        .findAllByIdName(SUITE_NAME);
    assertThat(savedSuiteExecutions.isEmpty()).isFalse();

    // And test case executions are added for the suite
    List<TestCaseExecution> savedCaseExecutions = testCaseExecutionRepository
        .findAllBySuiteNameAndIdTime(SUITE_NAME, savedSuiteExecutions.get(0).getId().getTime());
    assertThat(savedCaseExecutions.size()).isEqualTo(2);
  }
}
