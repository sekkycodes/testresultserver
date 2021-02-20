package com.github.sekkycodes.testresultserver.fakes;

import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class FakeClockConfig {

  @Bean
  @Primary
  public Clock createFakeClock() {
    return FixtureHelper.FIXED_CLOCK;
  }
}
