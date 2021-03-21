package com.github.sekkycodes.testresultserver.vo.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Set;
import lombok.Builder;
import lombok.Value;

/**
 * Determines what entries to filter for. All properties are optional and won't be considered if not
 * set.
 */
@Value
@Builder(toBuilder = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonDeserialize(builder = TestCaseFilter.TestCaseFilterBuilder.class)
public class TestCaseFilter {

  /**
   * Only consider test cases for suites under the specified project name.
   */
  String projectName;

  /**
   * Only consider test cases for suites marked as executed with the specified type.
   */
  String testType;

  /**
   * Only consider test cases for suites executed under the specified environment.
   */
  String environment;

  /**
   * Only consider test cases executed on the specified date.
   */
  String date;

  /**
   * Only consider test cases completed with the specified result.
   */
  String result;

  /**
   * Only consider test cases of suites marked with ALL of the specified labels.
   */
  Set<String> labels;

  @JsonPOJOBuilder(withPrefix = "")
  public static class TestCaseFilterBuilder {

  }
}
