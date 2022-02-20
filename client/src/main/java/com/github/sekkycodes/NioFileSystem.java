package com.github.sekkycodes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class NioFileSystem implements FileSystem {

  @Override
  public String read(Path path) throws IOException {
    return Files.readString(path, StandardCharsets.UTF_8);
  }
}
