package com.github.sekkycodes.testresultserver.repositories;

import com.github.sekkycodes.testresultserver.domain.TestCase;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * JPA Repository for TestCases
 */
public interface TestCaseRepository extends CrudRepository<TestCase, UUID> {

}
