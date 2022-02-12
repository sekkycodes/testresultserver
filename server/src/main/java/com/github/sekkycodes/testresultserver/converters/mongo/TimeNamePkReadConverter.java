package com.github.sekkycodes.testresultserver.converters.mongo;

import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import org.springframework.core.convert.converter.Converter;

/**
 * Defines how values are converted into an instance of TimeNamePK when reading from database
 */
public class TimeNamePkReadConverter implements Converter<String, TimeNamePK> {

  /**
   * Converts the String stored in database for TimeNamePKs to an instance of the object
   *
   * @param str the raw value to be converted
   * @return an instance of TimeNamePK; will return null if the conversion fails
   */
  @Override
  public TimeNamePK convert(String str) {
    String[] parts = str.split(";");
    return new TimeNamePK(parts[0], Long.parseLong(parts[1]));
  }
}