package com.github.sekkycodes.testresultserver.configuration;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Supplies Clocks for Dependency Injection
 */
@Configuration
public class ClockConfig {

  /**
   * Creates a new Clock object with the System's default time zone
   * @return new Clock instance
   */
  @Bean
  public Clock createClock() {
    return Clock.systemDefaultZone();
  }
}
