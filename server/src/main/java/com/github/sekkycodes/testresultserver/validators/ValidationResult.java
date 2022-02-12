package com.github.sekkycodes.testresultserver.validators;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ValidationResult {

  String validationMessage;
  boolean isValid;

}
