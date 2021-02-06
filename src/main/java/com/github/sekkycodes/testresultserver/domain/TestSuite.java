package com.github.sekkycodes.testresultserver.domain;

import com.github.sekkycodes.testresultserver.vo.TestSuiteVO;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 * A TestSuite is the top level collection which contains TestCases.
 */
@Entity
@Table(name = "testsuites")
@AllArgsConstructor
@NoArgsConstructor
@Data
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
   * Converts the domain object into an immutable value object
   * @return mapped value object
   */
  public TestSuiteVO toValueObject() {
    return TestSuiteVO.builder()
        .id(getId())
        .name(getName())
        .build();
  }
}
