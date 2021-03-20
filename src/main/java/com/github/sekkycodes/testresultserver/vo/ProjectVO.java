package com.github.sekkycodes.testresultserver.vo;

import java.util.Set;
import lombok.Builder;
import lombok.Value;

/**
 * Immutable value object for project information
 */
@Value
@Builder(toBuilder = true)
public class ProjectVO {

  String name;
  Set<String> environments;
  Set<String> testType;

}
