package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Service;

/**
 * Calculates the latest results across all test suites
 */
@Service
public class LatestResultsReporter {

  private final MongoTemplate mongoTemplate;

  @Autowired
  public LatestResultsReporter(MongoTemplate mongoTemplate) {
    this.mongoTemplate = Objects.requireNonNull(mongoTemplate);
  }

  /**
   * Calculates the latest results across test suites without aggregation or filter.
   *
   * @return a collection of test suite execution results in no particular order
   */
  public Collection<TestSuiteExecutionVO> getAllLatest() {
    TypedAggregation<TestSuiteExecution> aggregation = TypedAggregation
            .newAggregation(TestSuiteExecution.class,
                Aggregation.group("id.name")
                    .max("id.time")
                    .as("test-suite-executions"));

    AggregationResults<TestSuiteExecution> aggregationResults = mongoTemplate
        .aggregate(aggregation, TestSuiteExecution.class);

    return aggregationResults.getMappedResults()
        .stream()
        .map(TestSuiteExecution::toValueObject)
        .collect(Collectors.toSet());
  }
}
