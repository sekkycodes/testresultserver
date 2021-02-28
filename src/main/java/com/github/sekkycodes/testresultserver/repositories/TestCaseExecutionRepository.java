package com.github.sekkycodes.testresultserver.repositories;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Mongo Repository for TestCaseExecutions
 */
@Repository
public interface TestCaseExecutionRepository extends
    MongoRepository<TestCaseExecution, TimeNamePK> {

  Optional<TestCaseExecution> findByIdNameAndIdTime(String name, long time);

  List<TestCaseExecution> findAllByIdName(String name);

  List<TestCaseExecution> findAllBySuiteNameAndIdTime(String suiteName, long time);
}
