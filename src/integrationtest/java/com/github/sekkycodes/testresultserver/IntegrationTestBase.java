package com.github.sekkycodes.testresultserver;

import com.github.sekkycodes.testresultserver.repositories.TestCaseExecutionRepository;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTestBase {

  @Autowired
  protected TestSuiteExecutionRepository testSuiteExecutionRepository;

  @Autowired
  protected TestCaseExecutionRepository testCaseExecutionRepository;

  @AfterEach
  void cleanUp() {
    this.testCaseExecutionRepository.deleteAll();
    this.testSuiteExecutionRepository.deleteAll();
  }
}
