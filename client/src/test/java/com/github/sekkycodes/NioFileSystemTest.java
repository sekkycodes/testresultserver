package com.github.sekkycodes;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NioFileSystemTest {

  private FileSystem fileSystem;

  @BeforeEach
  void beforeEach() {
    fileSystem = new NioFileSystem();
  }

  @Test
  void readsUtf8FileContent() throws IOException, URISyntaxException {
    URL url = Thread.currentThread().getContextClassLoader().getResource("cucumber.feature");
    assertThat(url).isNotNull();
    Path filePath = Path.of(url.toURI());

    String str = fileSystem.read(filePath);

    assertThat(str).contains("Scenario");
  }
}
