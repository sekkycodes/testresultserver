package com.github.sekkycodes.testresultserver.repositories;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.IntegrationTestBase;
import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

class TestSuiteExecutionRepositoryIT extends IntegrationTestBase {

  @Autowired
  TestSuiteExecutionRepository sut;

  @Autowired
  TestCaseExecutionRepository testCaseExecutionRepository;

  @Autowired
  MongoTemplate mongoTemplate;

  private TestSuiteExecution storedExecution;

  @BeforeEach
  void beforeEach() {
    storedExecution = FixtureHelper.buildTestSuiteExecution();
    storedExecution.getTestCaseExecutionList().forEach(tc -> testCaseExecutionRepository.save(tc));
    sut.save(storedExecution);

    TestSuiteExecution differentProject = FixtureHelper.buildTestSuiteExecution();
    differentProject.setProject("different project");
    differentProject
        .setId(new TimeNamePK("some other ID", FixtureHelper.FIXED_TIMESTAMP.toEpochMilli()));
    sut.save(differentProject);

    TestSuiteExecution differentTestType = FixtureHelper.buildTestSuiteExecution();
    differentTestType.setTestType("different testType");
    differentTestType
        .setId(new TimeNamePK("yet another ID", FixtureHelper.FIXED_TIMESTAMP.toEpochMilli()));
    sut.save(differentTestType);
  }

  @AfterEach
  void afterEach() {
    sut.deleteAll();
  }

  @Test
  void storesNewTestSuite() {
    Optional<TestSuiteExecution> result = sut
        .findById(new TimeNamePK(storedExecution.getId().getName(), storedExecution.getId()
            .getTime()));

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getId().getName()).isEqualTo(storedExecution.getId().getName());
    assertThat(result.get().getId().getTime()).isEqualTo(storedExecution.getId().getTime());
    assertThat(result.get().getDuration()).isEqualTo(storedExecution.getDuration());
  }

  @Test
  void findsTestSuiteByName() {
    List<TestSuiteExecution> result = sut.findAllByIdName(storedExecution.getId().getName());

    assertThat(result.isEmpty()).isFalse();
    assertThat(result.get(0).getId().getName()).isEqualTo(storedExecution.getId().getName());
  }

  @Test
  void findsTestSuitesByExecutionDate() {
    List<TestSuiteExecution> result = sut.findByExecutionDateAndProjectAndTestType(
        LocalDate.parse("2020-01-29"), storedExecution.getProject(), storedExecution.getTestType());

    assertThat(result.size()).isEqualTo(1);
    TestSuiteExecution resultSuite = result.get(0);
    assertThat(resultSuite.getId().getName()).isEqualTo(storedExecution.getId().getName());
    assertThat(resultSuite.getProject()).isEqualTo(storedExecution.getProject());
    assertThat(resultSuite.getTestType()).isEqualTo(storedExecution.getTestType());
  }

  @Test
  void aggregatesAllProjects() {
    List<TestSuiteExecution> suites = sut.findAllProjects();

    assertThat(suites.size()).isEqualTo(3);
    Optional<TestSuiteExecution> suite = suites.stream()
        .filter(s -> storedExecution.getId().equals(s.getId())).findFirst();
    assertThat(suite.isPresent()).isTrue();
    assertThat(suite.get().getProject()).isEqualTo(storedExecution.getProject());
    assertThat(suite.get().getEnvironment()).isEqualTo(storedExecution.getEnvironment());
    assertThat(suite.get().getTestType()).isEqualTo(storedExecution.getTestType());
    assertThat(suite.get().getLabels()).contains(storedExecution.getLabels().get(0));
  }
}
