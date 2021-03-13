package com.github.sekkycodes.testresultserver.repositories;

import com.github.sekkycodes.testresultserver.domain.TestCaseExecution;
import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Mongo Repository for TestCaseExecutions
 */
@Repository
public interface TestCaseExecutionRepository extends
    MongoRepository<TestCaseExecution, TimeNamePK> {

  @Query("{ '_id': '?0;?1' }")
  Optional<TestCaseExecution> findByIdNameAndIdTime(String name, long time);

  @Query("{ '_id': { $regex: '?0;.*' }}")
  List<TestCaseExecution> findAllByIdName(String name);

  @Query("{ 'suiteName': ?0, '_id': { $regex: '.*;?1' }}")
  List<TestCaseExecution> findAllBySuiteNameAndIdTime(String suiteName, long time);

  List<TestCaseExecution> findAllBySuiteName(String suiteName);
}
