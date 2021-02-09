package com.github.sekkycodes.testresultserver.converters;

import com.github.sekkycodes.testresultserver.domain.TestCase;
import com.github.sekkycodes.testresultserver.domain.TestSuite;
import com.github.sekkycodes.testresultserver.junit.Testsuite;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Converts JUnit objects to canonical value objects
 */
@Service
public class JunitConverter {

  /**
   * Converts JUnit Testsuite object to canonical TestSuite entity
   *
   * @param junitSuite suite to be converted
   * @return converted suite object
   */
  public TestSuite toTestSuite(Testsuite junitSuite) {
    Set<TestCase> testCases = junitSuite.getTestcase().stream().map(this::toTestCase)
        .collect(Collectors.toSet());

    return TestSuite.builder()
        .name(junitSuite.getName())
        .testCases(testCases)
        .build();
  }

  /**
   * Converts JUnit Testcase object to canonical TestCase entity
   *
   * @param testCase case to be converted
   * @return converted case object
   */
  public TestCase toTestCase(Testsuite.Testcase testCase) {
    return TestCase.builder()
        .name(testCase.getName())
        .build();
  }
}
