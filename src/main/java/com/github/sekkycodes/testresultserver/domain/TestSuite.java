package com.github.sekkycodes.testresultserver.domain;

import com.github.sekkycodes.testresultserver.vo.TestSuiteVO;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * A TestSuite is the top level collection which contains TestCases.
 */
@Entity
@Table(name = "testsuites")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class TestSuite {

  /**
   * Unique ID
   */
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(nullable = false, updatable = false)
  private UUID id;

  /**
   * Unique name of the test suite
   */
  @Column(nullable = false, unique = true)
  private String name;

  /**
   * Collection of test cases within this suite
   */
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "test_suite_id")
  private Set<TestCase> testCases = new HashSet<>();

  /**
   * Converts the domain object into an immutable value object
   *
   * @return mapped value object
   */
  public TestSuiteVO toValueObject() {
    return TestSuiteVO.builder()
        .id(getId())
        .name(getName())
        .testCaseIds(getTestCases().stream().map(TestCase::getId).collect(Collectors.toSet()))
        .build();
  }

  public void addTestCase(TestCase testCase) {
    testCases.add(testCase);
  }
}