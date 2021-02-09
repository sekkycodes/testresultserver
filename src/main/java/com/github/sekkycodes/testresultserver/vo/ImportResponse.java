package com.github.sekkycodes.testresultserver.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Response VO for importing, summarizing import data
 */
@Value
@Builder(toBuilder = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ImportResponse {

  /**
   * Collection of unique IDs of imported suites
   */
  Set<UUID> importedSuites;
}
