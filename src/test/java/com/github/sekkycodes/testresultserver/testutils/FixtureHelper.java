package com.github.sekkycodes.testresultserver.testutils;

import com.github.sekkycodes.testresultserver.domain.TestSuite;
import com.github.sekkycodes.testresultserver.domain.TestSuite.TestSuiteBuilder;
import java.util.UUID;

/**
 * Utility class for creating fixtures and test data
 */
public class FixtureHelper {


  public static TestSuiteBuilder buildTestSuite() {
    return TestSuite.builder()
        .id(UUID.randomUUID())
        .name("dummy test suite");
  }
}
