package com.github.sekkycodes.testresultserver.vo.importing;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Additional import data
 */
@Value
@Builder(toBuilder = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonDeserialize(builder = ImportRequest.ImportRequestBuilder.class)
public class ImportRequest {

  /**
   * A free-text field categorizing the tests that are imported. This will mark the suite(s) as
   * tests of this type. Examples are: "Unit", "Integration", "E2E".
   */
  String testType;

  /**
   * An identifier for the project for which the tests to be imported were executed
   */
  String project;

  /**
   * The environment the tests were executed against. Typically this might be "local" or "ci" for
   * unit and integration tests, or (for example) "dev", "test", or "prod" for E2E tests
   */
  String environment;

  /**
   * Any freely definable textual labels for later aggregation and filtering of test results
   */
  List<String> labels;

  /**
   * Optional. Time the tests started executing as epoch millis. This field will be used to
   * determine execution start for the imported suite(s). It will also define what date the suite(s)
   * are counted for. If this field is not set, then the timestamp from the suite will be used. If
   * the suite has no timestamp set either, then the current date and time will be used.
   */
  Long executionTimeStamp;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ImportRequestBuilder {

  }
}
