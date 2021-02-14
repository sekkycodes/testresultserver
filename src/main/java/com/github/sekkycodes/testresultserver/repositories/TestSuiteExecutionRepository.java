package com.github.sekkycodes.testresultserver.repositories;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * JPA Repository for TestSuiteExecutions
 */
public interface TestSuiteExecutionRepository extends
    CrudRepository<TestSuiteExecution, TimeNamePK> {

  Optional<TestSuiteExecution> findByIdNameAndIdTime(String name, long time);

  List<TestSuiteExecution> findAllByIdName(String name);

  @Query("SELECT tse "
      + " FROM TestSuiteExecution tse "
      + " WHERE tse.id.time = "
      + "   (SELECT MAX(tse1.id.time) FROM TestSuiteExecution tse1 WHERE tse1.id.name = tse.id.name) ")
  Collection<TestSuiteExecution> findLatestResults();
}
