package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.converters.JunitConverter;
import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.exceptions.ImportException;
import com.github.sekkycodes.testresultserver.junit.Testsuite;
import com.github.sekkycodes.testresultserver.repositories.TestCaseExecutionRepository;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.vo.ImportResult;
import com.github.sekkycodes.testresultserver.vo.TestCaseExecutionVO;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

/**
 * Service for handling import of files in supported formats
 */
@Service
public class FileImportService {

  private final TestSuiteExecutionRepository testSuiteExecutionRepository;
  private final TestCaseExecutionRepository testCaseExecutionRepository;
  private final JunitReader junitReader;
  private final JunitConverter junitConverter;

  @Autowired
  public FileImportService(
      TestSuiteExecutionRepository testSuiteExecutionRepository,
      TestCaseExecutionRepository testCaseExecutionRepository,
      JunitReader junitReader,
      JunitConverter junitConverter) {

    this.testSuiteExecutionRepository = Objects.requireNonNull(testSuiteExecutionRepository);
    this.testCaseExecutionRepository = Objects.requireNonNull(testCaseExecutionRepository);
    this.junitReader = Objects.requireNonNull(junitReader);
    this.junitConverter = Objects.requireNonNull(junitConverter);
  }

  /**
   * Imports from a JUnit XML input stream.
   *
   * @param source input stream source of JUnix XML file
   * @return value object of saved test suite execution
   */
  public ImportResult importJunitFile(InputStreamSource source) throws ImportException {
    try {
      Testsuite junitSuite = junitReader.readSuite(source.getInputStream());
      TestSuiteExecution suite = junitConverter.toTestSuiteExecution(junitSuite);
      TestSuiteExecutionVO suiteVO = testSuiteExecutionRepository.save(suite).toValueObject();

      Set<TestCaseExecutionVO> caseExecutionVOs = new HashSet<>();
      for (Testsuite.Testcase junitTestcase : junitSuite.getTestcase()) {
        TestCaseExecution caseExe = junitConverter
            .toTestCaseExecution(junitTestcase, suiteVO.getIdName(), suiteVO.getIdTime());
        caseExecutionVOs.add(testCaseExecutionRepository.save(caseExe).toValueObject());
      }

      return ImportResult.builder()
          .importedSuite(suiteVO)
          .importedCases(caseExecutionVOs)
          .build();

    } catch (IOException | JAXBException e) {
      throw new ImportException("Failed to import JUnit file: " + e.getMessage(), e);
    }
  }
}
