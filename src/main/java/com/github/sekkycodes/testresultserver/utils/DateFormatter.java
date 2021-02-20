package com.github.sekkycodes.testresultserver.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utils class for formatting dates
 */
public class DateFormatter {

  /**
   * The pattern used for formatting dates
   */
  public static final String PATTERN = "yyyy-MM-dd";

  /**
   * Converts a timestamp to a formatted date using the system's default zone id
   *
   * @param epochMillis long timestamp value in epoch millis (since 1970) to be converted
   * @return formatted date
   */
  public static String toFormattedDate(long epochMillis) {
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern(PATTERN)
            .withZone(ZoneId.systemDefault());

    return formatter.format(Instant.ofEpochMilli(epochMillis));
  }
}
