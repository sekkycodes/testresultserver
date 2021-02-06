package com.github.sekkycodes.testresultserver.vo;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Immutable value object of TestSuite
 */
@Value
@Builder(toBuilder = true)
public class TestSuiteVO {

  UUID id;
  String name;

}
