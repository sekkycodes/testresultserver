package com.github.sekkycodes.testresultserver.services;

import static com.google.common.truth.Truth.assertThat;

import java.time.Clock;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ClockFactoryTest {

  private ClockFactory sut;

  @BeforeEach
  void beforeEach() {
    sut = new ClockFactory();
  }

  @Nested
  class CreateClock {

    @Test
    void createsANewClockInstance() {
      Clock clock = sut.createClock();
      assertThat(clock.getZone()).isEqualTo(ZoneId.systemDefault());
    }
  }
}
