package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.domain.TestSuite;
import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

/**
 * Service for handling import of files in supported formats
 */
@Service
public class FileImportService {

  private final TestSuiteRepository testSuiteRepository;

  @Autowired
  public FileImportService(TestSuiteRepository testSuiteRepository) {
    this.testSuiteRepository = Objects.requireNonNull(testSuiteRepository);
  }

  /**
   * Imports from a JUnit XML input stream.
   * @param source input stream source of JUnix XML file
   * @return collection of unique IDs of imported test suites
   */
  public Set<UUID> importJunitFile(InputStreamSource source) throws ImportException {
    try(BufferedReader reader = new BufferedReader(new InputStreamReader(source.getInputStream()))) {

      // TODO: handle file contents
      HashSet<UUID> importedSuites = new HashSet<>();
      importedSuites.add(testSuiteRepository.save(TestSuite.builder().build()).getId());
      return importedSuites;

    } catch (IOException e) {
      throw new ImportException("Failed to import JUnit file: " + e.getMessage(), e);
    }
  }
}
