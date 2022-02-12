package com.github.sekkycodes.testresultserver.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Utils class for formatting dates
 */
public class DateFormatter {

  private DateFormatter() {
  }

  /**
   * The pattern used for handling formatted dates
   */
  public static final String PATTERN = "yyyy-MM-dd";

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN)
      .withZone(ZoneOffset.UTC);

  /**
   * Converts a timestamp to a formatted date using UTC zone
   *
   * @param epochMillis long timestamp value in epoch millis (since 1970) to be converted
   * @return formatted date
   */
  public static String toFormattedDate(long epochMillis) {
    return formatter.format(Instant.ofEpochMilli(epochMillis));
  }

  /**
   * Converts a string in the usual pattern (see PATTERN) to epoch millis utc zone
   *
   * @param date the date as a spring representation to be parsed - needs to have the format defined
   *             by the pattern set for the DateFormatter
   * @return epoch millis
   */
  public static long fromDate(String date) {
    return LocalDate.parse(date, formatter)
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli();
  }
}
