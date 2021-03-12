package com.github.sekkycodes.testresultserver.controllers;

import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.services.FileImportService;
import com.github.sekkycodes.testresultserver.vo.importing.ImportRequest;
import com.github.sekkycodes.testresultserver.vo.importing.ImportResult;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Endpoint for importing test results in all supported formats
 */
@RestController
@RequestMapping("/api/result-import")
@Slf4j
public class ResultImportController {

  public static final String NO_FILE_ERROR_TEXT = "no file supplied for import";
  public static final String NO_BODY_ERROR_TEXT = "no import request body";

  private final FileImportService fileImportService;

  @Autowired
  public ResultImportController(FileImportService fileImportService) {
    this.fileImportService = Objects.requireNonNull(fileImportService);
  }

  /**
   * Endpoint for importing JUnit XML file
   *
   * @param file the JUnit XML file
   * @return a collection of imported test suite IDs
   */
  @PostMapping("/import-junit")
  public ResponseEntity<ImportResult> importJunitResults(
      @RequestParam("file") MultipartFile file,
      @RequestParam("testType") String testType,
      @RequestParam("project") String project,
      @RequestParam("environment") String environment,
      @RequestParam("executionTimeStamp") long executionTimeStamp,
      @RequestParam("labels") String labels) {

    if (file == null) {
      return new ResponseEntity<>(
          ImportResult.builder().errorMessage(NO_FILE_ERROR_TEXT).build(),
          HttpStatus.BAD_REQUEST);
    }

    ImportRequest fileImportRequest = ImportRequest.builder()
        .environment(environment)
        .project(project)
        .executionTimeStamp(executionTimeStamp)
        .testType(testType)
        .labels(Arrays.asList(labels.split(" ")))
        .build();

    try {
      ImportResult response = fileImportService.importJunitFile(file, fileImportRequest);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (ImportException e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(ImportResult.builder().errorMessage(e.getMessage()).build(),
          HttpStatus.BAD_REQUEST);
    }
  }
}
