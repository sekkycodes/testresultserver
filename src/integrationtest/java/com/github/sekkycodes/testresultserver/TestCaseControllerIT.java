package com.github.sekkycodes.testresultserver;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.controllers.TestCaseController;
import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TestResult;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.github.sekkycodes.testresultserver.vo.TestCaseExecutionVO;
import com.github.sekkycodes.testresultserver.vo.requests.TestCaseFilter;
import com.github.sekkycodes.testresultserver.vo.requests.TestCaseFilter.TestCaseFilterBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TestCaseControllerIT extends IntegrationTestBase {

  @Autowired
  TestCaseController testCaseController;

  private static final String DUMMY_LABEL = "dummyLabel";
  private static final String DUMMY_PROJECT = "dummyProject";
  private static final String DUMMY_TEST_TYPE = "integration";
  private static final String DUMMY_ENVIRONMENT = "staging";
  private static final String DUMMY_DATE = "2021-03-15";
  private static final String DUMMY_RESULT = "failed";

  private TestCaseFilterBuilder filterBuilder;
  private TimeNamePK suiteId;

  @BeforeEach
  void beforeEach() {
    filterBuilder = TestCaseFilter.builder()
        .date(DUMMY_DATE)
        .environment(DUMMY_ENVIRONMENT)
        .labels(Collections.singleton(DUMMY_LABEL))
        .projectName(DUMMY_PROJECT)
        .testType(DUMMY_TEST_TYPE)
        .result(DUMMY_RESULT);

    persistSuite(buildTestSuite());
  }

  @Test
  void returnsFailedTestCaseMatchingAllCriteria() {
    invokeAndAssertCorrectTestCaseReturned(TestResult.FAILED);
  }

  @Test
  void returnsPassedTestCaseMatchingAllCriteria() {
    invokeAndAssertCorrectTestCaseReturned(TestResult.PASSED);
  }

  @Test
  void returnsSkippedTestCaseMatchingAllCriteria() {
    invokeAndAssertCorrectTestCaseReturned(TestResult.SKIPPED);
  }

  @Test
  void returnsErrorTestCaseMatchingAllCriteria() {
    invokeAndAssertCorrectTestCaseReturned(TestResult.ERROR);
  }

  @Test
  void returnsAllTestCasesIfNoFilterCriteriaSet() {
    TestCaseFilter filter = TestCaseFilter.builder().build();

    invokeAndAssertAllTestCasesReturned(filter);
  }

  @Test
  void returnsEmptyListIfNoTestCasesMeetDateCriteria() {
    TestCaseFilter filter = TestCaseFilter.builder().date("2020-01-01").build();

    invokeAndAssertNoTestCasesReturned(filter);
  }

  @Test
  void returnsEmptyListIfNoTestCasesMeetTestTypeCriteria() {
    TestCaseFilter filter = TestCaseFilter.builder().testType("nonexisting").build();

    invokeAndAssertNoTestCasesReturned(filter);
  }

  @Test
  void returnsEmptyListIfNoTestCasesMeetProjectCriteria() {
    TestCaseFilter filter = TestCaseFilter.builder().projectName("nonexisting").build();

    invokeAndAssertNoTestCasesReturned(filter);
  }

  @Test
  void returnsEmptyListIfNoTestCasesMeetEnvironmentCriteria() {
    TestCaseFilter filter = TestCaseFilter.builder().environment("nonexisting").build();

    invokeAndAssertNoTestCasesReturned(filter);
  }

  @Test
  void returnsEmptyListIfNoTestCasesMeetAllLabelsCriteria() {
    Set<String> labels = new HashSet<>();
    labels.add(DUMMY_LABEL); // is met
    labels.add("nonexisting"); // is not met
    TestCaseFilter filter = TestCaseFilter.builder().labels(labels).build();

    invokeAndAssertNoTestCasesReturned(filter);
  }

  private void invokeAndAssertCorrectTestCaseReturned(TestResult expectedResult) {
    TestCaseFilter filter = filterBuilder.result(expectedResult.name()).build();

    Set<TestCaseExecutionVO> testCases = invoke(filter);

    assertThat(testCases.size()).isEqualTo(1);
    assertThat(testCases.iterator().next().getTestResult())
        .isEqualTo(expectedResult.name());
  }

  private void invokeAndAssertNoTestCasesReturned(TestCaseFilter filter) {
    Set<TestCaseExecutionVO> testCases = invoke(filter);

    assertThat(testCases).isEmpty();
  }

  private void invokeAndAssertAllTestCasesReturned(TestCaseFilter filter) {
    Set<TestCaseExecutionVO> testCases = invoke(filter);

    assertThat(testCases.size()).isEqualTo(4);
  }

  private Set<TestCaseExecutionVO> invoke(TestCaseFilter filter) {
    ResponseEntity<Set<TestCaseExecutionVO>> testCases = testCaseController
        .filter(filter);

    assertThat(testCases.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(testCases.getBody()).isNotNull();

    return testCases.getBody();
  }

  private void persistSuite(TestSuiteExecution suite) {
    testSuiteExecutionRepository.save(suite);
    testCaseExecutionRepository.saveAll(suite.getTestCaseExecutionList());
  }

  private TestSuiteExecution buildTestSuite() {
    List<String> labels = new ArrayList<>();
    labels.add(DUMMY_LABEL);
    labels.add("anotherLabel");

    LocalDate executionDate = LocalDate.parse(DUMMY_DATE);
    // 2021-03-15 4:30 CET
    long dummyExecutionTimeStamp = 1615779000000L;
    suiteId = new TimeNamePK(
        "someTestSuite", dummyExecutionTimeStamp);

    List<TestCaseExecution> testCases = new ArrayList<>();
    testCases.add(buildTestCase(TestResult.FAILED));
    testCases.add(buildTestCase(TestResult.SKIPPED));
    testCases.add(buildTestCase(TestResult.PASSED));
    testCases.add(buildTestCase(TestResult.ERROR));

    return TestSuiteExecution.builder()
        .id(suiteId)
        .labels(labels)
        .executionDate(executionDate)
        .duration(1)
        .testCasesFailed(1)
        .testCasesPassed(1)
        .testCasesSkipped(1)
        .testCasesWithError(1)
        .testCasesTotal(4)
        .testType(DUMMY_TEST_TYPE)
        .environment(DUMMY_ENVIRONMENT)
        .project(DUMMY_PROJECT)
        .testCaseExecutionList(testCases)
        .build();
  }

  private TestCaseExecution buildTestCase(TestResult result) {
    return TestCaseExecution.builder()
        .id(new TimeNamePK("tc" + result.name(), suiteId.getTime()))
        .result(result)
        .suiteName(suiteId.getName())
        .build();
  }
}
