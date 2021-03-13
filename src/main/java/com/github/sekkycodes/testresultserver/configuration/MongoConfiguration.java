package com.github.sekkycodes.testresultserver.configuration;

import com.github.sekkycodes.testresultserver.converters.mongo.TimeNamePkReadConverter;
import com.github.sekkycodes.testresultserver.converters.mongo.TimeNamePkWriteConverter;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions.MongoConverterConfigurationAdapter;
import org.springframework.lang.NonNull;

/**
 * Mongo configuration
 */
@Configuration
@Slf4j
public class MongoConfiguration extends AbstractMongoClientConfiguration {

  private final String databaseName;

  @Autowired
  public MongoConfiguration(Environment environment) {
    Objects.requireNonNull(environment);
    this.databaseName = environment.getProperty("spring.data.mongodb.database");
    log.info("connecting to database: {}", databaseName);
  }

  @Override
  @NonNull
  protected String getDatabaseName() {
    return this.databaseName;
  }

  /**
   * Registers custom converters needed for storing to and reading from MongoDB
   *
   * @param adapter provided
   */
  @Override
  protected void configureConverters(MongoConverterConfigurationAdapter adapter) {

    adapter.registerConverter(new TimeNamePkReadConverter());
    adapter.registerConverter(new TimeNamePkWriteConverter());
  }
}
