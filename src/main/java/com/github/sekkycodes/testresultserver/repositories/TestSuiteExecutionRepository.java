package com.github.sekkycodes.testresultserver.repositories;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import com.sun.istack.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * JPA Repository for TestSuiteExecutions
 */
public interface TestSuiteExecutionRepository extends
    CrudRepository<TestSuiteExecution, TimeNamePK> {

  Optional<TestSuiteExecution> findByIdNameAndIdTime(String name, long time);

  List<TestSuiteExecution> findAllByIdName(String name);
}
