package com.github.sekkycodes.testresultserver.repositories;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * JPA Repository for TestSuiteExecutions
 */
public interface TestSuiteExecutionRepository extends
    MongoRepository<TestSuiteExecution, TimeNamePK>, QuerydslPredicateExecutor<TestSuiteExecution> {

  Optional<TestSuiteExecution> findByIdNameAndIdTime(String name, long time);

  List<TestSuiteExecution> findAllByIdName(String name);
}
