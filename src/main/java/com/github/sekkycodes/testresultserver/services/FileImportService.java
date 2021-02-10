package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.converters.JunitConverter;
import com.github.sekkycodes.testresultserver.domain.TestSuite;
import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.junit.Testsuite;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteRepository;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

/**
 * Service for handling import of files in supported formats
 */
@Service
public class FileImportService {

  private final TestSuiteRepository testSuiteRepository;
  private final JunitReader junitReader;
  private final JunitConverter junitConverter;

  @Autowired
  public FileImportService(
      TestSuiteRepository testSuiteRepository,
      JunitReader junitReader,
      JunitConverter junitConverter) {

    this.testSuiteRepository = Objects.requireNonNull(testSuiteRepository);
    this.junitReader = Objects.requireNonNull(junitReader);
    this.junitConverter = Objects.requireNonNull(junitConverter);
  }

  /**
   * Imports from a JUnit XML input stream.
   *
   * @param source input stream source of JUnix XML file
   * @return collection of unique IDs of imported test suites
   */
  public Set<UUID> importJunitFile(InputStreamSource source) throws ImportException {
    try {
      Testsuite junitSuite = junitReader.readSuite(source.getInputStream());
      TestSuite suite = junitConverter.toTestSuite(junitSuite);
      UUID savedId = createOrUpdate(suite);
      return Collections.singleton(savedId);
    } catch (IOException | JAXBException e) {
      throw new ImportException("Failed to import JUnit file: " + e.getMessage(), e);
    }
  }

  private UUID createOrUpdate(TestSuite suite) {
    Optional<TestSuite> existingSuite = testSuiteRepository.findByName(suite.getName());
    existingSuite.ifPresent(testSuite -> suite.setId(testSuite.getId()));
    return testSuiteRepository.save(suite).getId();
  }
}
