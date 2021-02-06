package com.github.sekkycodes.testresultserver.repositories;

import com.github.sekkycodes.testresultserver.domain.TestSuite;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * JPA Repository for TestSuites
 */
public interface TestSuiteRepository extends CrudRepository<TestSuite, UUID> {
}
