package com.github.sekkycodes.testresultserver.services;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.sekkycodes.testresultserver.TestBase;
import com.github.sekkycodes.testresultserver.converters.JunitConverter;
import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.repositories.TestCaseExecutionRepository;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.vo.importing.ImportRequest;
import java.io.ByteArrayInputStream;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class FileImportServiceTest extends TestBase {

  private FileImportService sut;

  @Mock
  TestSuiteExecutionRepository testSuiteExecutionRepository;

  @Mock
  TestCaseExecutionRepository testCaseExecutionRepository;

  @BeforeEach
  void beforeEach() {
    sut = new FileImportService(
        testSuiteExecutionRepository,
        testCaseExecutionRepository,
        new JunitReader(),
        new JunitConverter(
            Clock.fixed(Instant.ofEpochMilli(1613240779300L), ZoneId.systemDefault())));
  }

  @Nested
  class ImportJunitFile {

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
    void throwsImportExceptionInCaseReadingInputStreamFails() {
      assertThrows(ImportException.class,
          () -> sut
              .importJunitFile(() -> new ByteArrayInputStream("123".getBytes()), importRequest));
    }
  }
}
