package com.github.sekkycodes.dtos;

import lombok.Builder;
import lombok.Value;

/**
 * Represents the design for a test (not its execution)
 */
@Value
@Builder(toBuilder = true)
public class TestCase {

  String feature;
  String featureId;
  String name;
  String description;
  String testCaseId;
}
