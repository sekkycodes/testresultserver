package com.github.sekkycodes.testresultserver.exceptions;

public class ImportException extends Exception {

  public ImportException(String message) {
    super(message);
  }

  public ImportException(String message, Throwable cause) {
    super(message, cause);
  }
}
