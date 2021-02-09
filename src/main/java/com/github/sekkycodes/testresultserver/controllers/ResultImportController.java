package com.github.sekkycodes.testresultserver.controllers;

import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.services.FileImportService;
import com.github.sekkycodes.testresultserver.vo.ImportResponse;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Endpoint for importing test results in all supported formats
 */
@RestController
@Slf4j
public class ResultImportController {

  private final FileImportService fileImportService;

  @Autowired
  public ResultImportController(FileImportService fileImportService) {
    this.fileImportService = Objects.requireNonNull(fileImportService);
  }

  /**
   * Endpoint for importing JUnit XML file
   * @param file the JUnit XML file
   * @return a collection of imported test suite IDs
   */
  @PostMapping("/import-junit")
  public ResponseEntity<ImportResponse> importJunitResults(@RequestParam("file") MultipartFile file) {

    try {
      Set<UUID> importedSuites = fileImportService.importJunitFile(file);
      return new ResponseEntity<>(ImportResponse.builder().importedSuites(importedSuites).build(), HttpStatus.CREATED);
    } catch (ImportException e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(ImportResponse.builder().build(), HttpStatus.BAD_REQUEST);
    }
  }
}
