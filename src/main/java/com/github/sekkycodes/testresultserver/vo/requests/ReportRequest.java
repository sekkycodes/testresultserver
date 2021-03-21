package com.github.sekkycodes.testresultserver.vo.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregateBy;
import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Additional import data
 */
@Value
@Builder(toBuilder = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonDeserialize(builder = ReportRequest.ReportRequestBuilder.class)
public class ReportRequest {

  /**
   * List of criteria by which to aggregate the results. Defining multiple aggregation criteria
   * multiplies result entries. For example: DATE and LABEL will return all (((executions on a day)
   * for each label) for every day)
   */
  List<AggregateBy> aggregations;

  /**
   * Filter criteria which will eliminate all results from the report which do not meat all the
   * filter criteria. The filter criteria are therefore joined together on an AND-basis rather than
   * an OR-basis.
   */
  TestSuiteFilter filter;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ReportRequestBuilder {

  }
}
