package com.github.sekkycodes.testresultserver.converters.mongo;

import com.github.sekkycodes.testresultserver.domain.TimeNamePK;
import org.springframework.core.convert.converter.Converter;

/**
 * Defines how an instance of TimeNamePK is converted when writing to database
 */
public class TimeNamePkWriteConverter implements Converter<TimeNamePK, String> {

  /**
   * Converts the TimeNamePK to a string for database persistence
   *
   * @param timeNamePK the value to be converted
   * @return a string representation of TimeNamePK
   */
  @Override
  public String convert(TimeNamePK timeNamePK) {
    return timeNamePK.toString();
  }
}
