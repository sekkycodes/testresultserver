package com.github.sekkycodes.testresultserver.vo.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Determines what entries are filtered out of the result. Leaving any field empty ignores that
 * field - i.e. no filtering.
 */
@Value
@Builder(toBuilder = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonDeserialize(builder = TestSuiteFilter.FilterBuilder.class)
public class TestSuiteFilter {

  /**
   * Only consider suites matching the project name
   */
  String projectName;

  /**
   * Only consider suites of this test type
   */
  String testType;

  /**
   * Only consider suites having all of these labels
   */
  @Builder.Default
  List<String> labels = new ArrayList<>();

  /**
   * Only consider suites matching this environment
   */
  String environment;

  /**
   * Take only the last x days of suite executions - if set to 0 all suites will be included in the
   * report calculation
   */
  int daysBack;

  @JsonPOJOBuilder(withPrefix = "")
  public static class FilterBuilder {

  }
}
