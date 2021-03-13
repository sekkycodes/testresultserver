package com.github.sekkycodes.testresultserver.repositories;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Mongo Repository for TestSuiteExecutions
 */
@Repository
public interface TestSuiteExecutionRepository extends
    MongoRepository<TestSuiteExecution, TimeNamePK>, QuerydslPredicateExecutor<TestSuiteExecution> {

  @Query("{'_id': { $regex: '?0;.*' }}")
  List<TestSuiteExecution> findAllByIdName(String name);
}
