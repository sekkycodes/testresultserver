package com.github.sekkycodes.testresultserver.repositories;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * JPA Repository for TestSuiteExecutions
 */
public interface TestSuiteExecutionRepository extends
    JpaRepository<TestSuiteExecution, TimeNamePK>, JpaSpecificationExecutor<TestSuiteExecution> {

  Optional<TestSuiteExecution> findByIdNameAndIdTime(String name, long time);

  List<TestSuiteExecution> findAllByIdName(String name);

  @Query("SELECT tse "
      + " FROM TestSuiteExecution tse "
      + " WHERE tse.id.time = "
      + "   (SELECT MAX(tse1.id.time) FROM TestSuiteExecution tse1 WHERE tse1.id.name = tse.id.name) ")
  Collection<TestSuiteExecution> findLatestResults();

  /**
   * Filters for suite executions with project name
   * @param project the identifier for the project
   * @return Specification filtering project name
   */
  static Specification<TestSuiteExecution> hasProject(String project) {
    return (root, query, builder) -> builder.equal(root.get("project"), project);
  }

  /**
   * Filters for suite executions of test type
   * @param testType the name of the test type
   * @return Specification filtering for test type
   */
  static Specification<TestSuiteExecution> hasTestType(String testType) {
    return (root, query, builder) -> builder.equal(root.get("testType"), testType);
  }
}
