package com.github.sekkycodes.testresultserver.testutils;

import com.github.sekkycodes.testresultserver.domain.TestCase;
import com.github.sekkycodes.testresultserver.domain.TestSuite;
import java.util.Collections;
import java.util.UUID;

/**
 * Utility class for creating fixtures and test data
 */
public class FixtureHelper {

  public static TestSuite buildTestSuite() {
    TestSuite suite = TestSuite.builder()
        .id(UUID.randomUUID())
        .name("dummy test suite")
        .build();

    TestCase testCase = buildTestCase();
    suite.setTestCases(Collections.singleton(testCase));
    return suite;
  }

  public static TestCase buildTestCase() {
    return TestCase.builder()
        .id(UUID.randomUUID())
        .name("dummy test case")
        .build();
  }
}
