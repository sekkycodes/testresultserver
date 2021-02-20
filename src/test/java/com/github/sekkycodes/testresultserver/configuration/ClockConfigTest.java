package com.github.sekkycodes.testresultserver.configuration;

import static com.google.common.truth.Truth.assertThat;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ClockConfigTest {

  private ClockConfig sut;

  @BeforeEach
  void beforeEach() {
    sut = new ClockConfig();
  }

  @Nested
  class CreateClock {

    @Test
    void createsANewClockInstance() {
      Clock clock = sut.createClock();
      assertThat(clock.getZone()).isEqualTo(ZoneOffset.UTC);
    }
  }
}
