package com.github.sekkycodes.testresultserver.domain;

import com.github.sekkycodes.testresultserver.vo.TestCaseVO;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * A test case is a runnable test with a unique name and ID.
 * Executions of test cases are TestCaseExecution objects.
 */
@Entity
@Table(name = "testcases")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class TestCase {

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
   * Unique name of the test case
   */
  @Column(nullable = false, unique = true)
  private String name;

  /**
   * Converts the domain object into an immutable value object
   * @return mapped value object
   */
  public TestCaseVO toValueObject() {
    return TestCaseVO.builder()
        .id(getId())
        .name(getName())
        .build();
  }
}
