package com.github.sekkycodes.testresultserver.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Combined primary key for an entity uniquely identifiable by name at a given point in time
 */
@EqualsAndHashCode
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeNamePK implements Serializable {

  /**
   * Unique name of the entity
   */
  @Column(name="id_name")
  private String name;

  /**
   * Unique point in time as epoch millis
   */
  @Column(name="id_time")
  private long time;
}
