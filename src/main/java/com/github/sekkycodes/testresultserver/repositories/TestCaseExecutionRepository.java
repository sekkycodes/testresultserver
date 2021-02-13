package com.github.sekkycodes.testresultserver.repositories;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * JPA Repository for TestCaseExecutions
 */
public interface TestCaseExecutionRepository extends
    CrudRepository<TestCaseExecution, TimeNamePK> {

  Optional<TestCaseExecution> findByIdNameAndIdTime(String name, long time);

  List<TestCaseExecution> findAllByIdName(String name);

  List<TestCaseExecution> findAllBySuiteNameAndIdTime(String suiteName, long time);
}
