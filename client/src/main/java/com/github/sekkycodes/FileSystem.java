package com.github.sekkycodes;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Abstraction of file system
 */
public interface FileSystem {

  /**
   * Reads content from a file path (uses UTF-8 encoding)
   *
   * @param path absolute path to the file to read
   * @return content of the file
   */
  String read(Path path) throws IOException;
}
