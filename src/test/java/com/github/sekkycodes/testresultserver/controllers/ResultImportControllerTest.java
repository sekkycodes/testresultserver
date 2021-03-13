package com.github.sekkycodes.testresultserver.controllers;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.TestBase;
import com.github.sekkycodes.testresultserver.converters.JunitConverter;
import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.repositories.TestCaseExecutionRepository;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.services.FileImportService;
import com.github.sekkycodes.testresultserver.services.JunitReader;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.importing.ImportRequest;
import com.github.sekkycodes.testresultserver.vo.importing.ImportResult;
import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

class ResultImportControllerTest extends TestBase {

  private ResultImportController sut;

  private InputStream inputStream;
  private final static long TIMESTAMP = 1613232242954L;

  @Mock
  TestSuiteExecutionRepository testSuiteExecutionRepository;

  @Mock
  TestCaseExecutionRepository testCaseExecutionRepository;

  @Mock
  MultipartFile multipartFile;

  RequestParams params;

  @BeforeEach
  void beforeEach() throws IOException {
    inputStream = this.getClass().getResourceAsStream("/junit-passed.xml");

    when(multipartFile.getInputStream())
        .thenReturn(inputStream);

    when(testSuiteExecutionRepository.save(any()))
        .thenReturn(
            TestSuiteExecution.builder()
                .id(new TimeNamePK("dummy suite", TIMESTAMP)).build());

    when(testCaseExecutionRepository.save(any()))
        .thenReturn(
            TestCaseExecution.builder()
                .id(new TimeNamePK("dummy case", TIMESTAMP)).result(
                TestResult.PASSED).suiteName("dummy suite").build());

    JunitReader junitReader = new JunitReader();
    JunitConverter junitConverter = new JunitConverter(
        Clock.fixed(Instant.ofEpochMilli(TIMESTAMP), ZoneId.of("UTC")));
    FileImportService fileImportService = new FileImportService(
        testSuiteExecutionRepository,
        testCaseExecutionRepository,
        junitReader,
        junitConverter);

    params = RequestParams.builder()
        .testType("unit")
        .environment("dev")
        .executionTimeStamp(1615553404650L)
        .file(multipartFile)
        .project("project01")
        .labels("label1 label2")
        .build();

    sut = new ResultImportController(fileImportService);
  }

  @AfterEach
  void afterEach() throws IOException {
    inputStream.close();
  }

  @Nested
  class ImportJunitResults {

    private ImportRequest importRequest;

    @BeforeEach
    void beforeEach() {
      importRequest = FixtureHelper.buildImportRequest();
    }

    @Test
    void createsANewSuiteExecution() {
      ResponseEntity<ImportResult> responseEntity = invoke();

      assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
      assertThat(responseEntity.getBody()).isNotNull();
      ImportResult importResult = responseEntity.getBody();
      assertThat(importResult.getImportedSuite()).isNotNull();
      assertThat(importResult.getImportedCases().size()).isEqualTo(1);
      assertThat(importResult.getImportedSuite().getIdTime()).isEqualTo(TIMESTAMP);
    }

    @Test
    void returnsBadRequestResponseIfFileCannotBeRead() throws ImportException {
      FileImportService fileImportService = mock(FileImportService.class);
      when(fileImportService.importJunitFile(any(), any()))
          .thenThrow(new ImportException("some error"));
      sut = new ResultImportController(fileImportService);

      ResponseEntity<ImportResult> responseEntity = invoke();

      assertErrorMessage(responseEntity, "some error", HttpStatus.BAD_REQUEST);
    }

    @Test
    void returnsBadRequestIfNoFileWasProvided() {
      params.setFile(null);

      ResponseEntity<ImportResult> responseEntity = invoke();

      assertErrorMessage(responseEntity, ResultImportController.NO_FILE_ERROR_TEXT,
          HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ImportResult> invoke() {
      return sut.importJunitResults(params.getFile(), params.getTestType(), params.getProject(),
          params.getEnvironment(), params.getExecutionTimeStamp(), params.getLabels());
    }

    private void assertErrorMessage(ResponseEntity<ImportResult> response,
        String expectedErrorMessage, HttpStatus expectedStatus) {

      assertThat(response.getStatusCodeValue()).isEqualTo(expectedStatus.value());
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getErrorMessage()).isEqualTo(expectedErrorMessage);
    }
  }

  @Builder(toBuilder = true)
  @Data
  private static class RequestParams {

    String testType;
    String project;
    String environment;
    String labels;
    Long executionTimeStamp;
    MultipartFile file;
  }
}
