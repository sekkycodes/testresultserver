package com.github.sekkycodes.testresultserver.controllers;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.TestBase;
import com.github.sekkycodes.testresultserver.converters.JunitConverter;
import com.github.sekkycodes.testresultserver.domain.TestSuite;
import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteRepository;
import com.github.sekkycodes.testresultserver.services.FileImportService;
import com.github.sekkycodes.testresultserver.services.JunitReader;
import com.github.sekkycodes.testresultserver.vo.ImportResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

class ResultImportControllerTest extends TestBase {

  private ResultImportController sut;

  private InputStream inputStream;
  private UUID id;

  @Mock
  TestSuiteRepository testSuiteRepository;

  @Mock
  MultipartFile multipartFile;

  @BeforeEach
  void beforeEach() throws IOException {
    inputStream = this.getClass().getResourceAsStream("/junit-passed.xml");

    when(multipartFile.getInputStream())
        .thenReturn(inputStream);

    id = UUID.randomUUID();
    when(testSuiteRepository.save(any()))
        .thenReturn(TestSuite.builder().id(id).build());

    JunitReader junitReader = new JunitReader();
    JunitConverter junitConverter = new JunitConverter();
    FileImportService fileImportService = new FileImportService(testSuiteRepository, junitReader,
        junitConverter);
    sut = new ResultImportController(fileImportService);
  }

  @AfterEach
  void afterEach() throws IOException {
    inputStream.close();
  }

  @Test
  void createsANewSuiteWithTestCases() {
    ResponseEntity<ImportResponse> responseEntity = sut.importJunitResults(multipartFile);

    assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(responseEntity.getBody()).isNotNull();
    Set<UUID> importedSuites = responseEntity.getBody().getImportedSuites();
    assertThat(importedSuites).isNotEmpty();
    assertThat(importedSuites.iterator().next()).isEqualTo(id);
  }

  @Test
  void returnsBadRequestResponseIfFileCannotBeRead() throws ImportException {
    FileImportService fileImportService = mock(FileImportService.class);
    when(fileImportService.importJunitFile(any())).thenThrow(new ImportException("some error"));
    sut = new ResultImportController(fileImportService);

    ResponseEntity<ImportResponse> responseEntity = sut.importJunitResults(multipartFile);

    assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }
}
