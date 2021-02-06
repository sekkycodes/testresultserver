package com.github.sekkycodes.testresultserver.vo;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Immutable value object of TestCase
 */
@Value
@Builder(toBuilder = true)
public class TestCaseVO {

  UUID id;
  String name;

}
