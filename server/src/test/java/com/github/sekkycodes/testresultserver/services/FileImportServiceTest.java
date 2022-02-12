package com.github.sekkycodes.testresultserver.services;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.TestBase;
import com.github.sekkycodes.testresultserver.converters.JunitConverter;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.repositories.TestCaseExecutionRepository;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.importing.ImportRequest;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.core.io.InputStreamSource;

class FileImportServiceTest extends TestBase {

  private FileImportService sut;

  @Mock
  TestSuiteExecutionRepository testSuiteExecutionRepository;

  @Mock
  TestCaseExecutionRepository testCaseExecutionRepository;

  @Captor
  ArgumentCaptor<TestSuiteExecution> testSuiteExecutionArgumentCaptor;

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
              .importJunitFile(
                  () -> new ByteArrayInputStream("123".getBytes(StandardCharsets.UTF_8)),
                  importRequest));
    }

    @Test
    void onlyAddsLabelsContainingText() throws ImportException {
      // arrange
      when(testSuiteExecutionRepository.save(any()))
          .thenReturn(FixtureHelper.buildTestSuiteExecution());
      when(testCaseExecutionRepository.save(any()))
          .thenReturn(FixtureHelper.buildTestCaseExecution());
      importRequest = importRequest.toBuilder()
          .labels(Collections.singletonList(""))
          .build();

      // act
      sut.importJunitFile(textInputStream(), importRequest);

      // assert
      verify(testSuiteExecutionRepository).save(testSuiteExecutionArgumentCaptor.capture());
      TestSuiteExecution tse = testSuiteExecutionArgumentCaptor.getValue();
      assertThat(tse.getLabels()).isEmpty();
    }

    private InputStreamSource textInputStream() {
      return () -> this.getClass().getResourceAsStream("/junit-passed.xml");
    }
  }
}
