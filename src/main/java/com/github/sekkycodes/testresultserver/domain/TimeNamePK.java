package com.github.sekkycodes.testresultserver.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Combined primary key for an entity uniquely identifiable by name at a given point in time
 */
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeNamePK implements Serializable {

  /**
   * Unique name of the entity
   */
  private String name;

  /**
   * Unique point in time as epoch millis
   */
  private long time;
}
