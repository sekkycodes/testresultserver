package com.github.sekkycodes.testresultserver.configuration;

import com.github.sekkycodes.testresultserver.converters.mongo.TimeNamePkReadConverter;
import com.github.sekkycodes.testresultserver.converters.mongo.TimeNamePkWriteConverter;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
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

  private final Environment environment;

  public static final String FALLBACK_DB = "trs";
  public static final String FALLBACK_HOST = "localhost";
  public static final String FALLBACK_PORT = "27017";

  @Autowired
  public MongoConfiguration(Environment environment) {
    this.environment = Objects.requireNonNull(environment);
  }

  @Override
  @NonNull
  protected String getDatabaseName() {
    String database = environment.getProperty("spring.data.mongodb.database", FALLBACK_DB);
    log.info("database name: {}", database);
    return database;
  }

  @Override
  @NonNull
  public MongoClient mongoClient() {
    String port = environment.getProperty("spring.data.mongodb.port", FALLBACK_PORT);
    String host = environment.getProperty("spring.data.mongodb.host", FALLBACK_HOST);

    String connectionString = String.format("mongodb://%s:%s", host, port);

    log.info("database connection string: {}", connectionString);
    return MongoClients.create(connectionString);
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
