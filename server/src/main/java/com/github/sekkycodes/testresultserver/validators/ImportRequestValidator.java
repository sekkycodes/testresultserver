package com.github.sekkycodes.testresultserver.validators;

import com.github.sekkycodes.testresultserver.vo.importing.ImportRequest;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

@Service
public class ImportRequestValidator {

  public static final String PROJECT_EMPTY = "Project name is empty";
  public static final String ENVIRONMENT_EMPTY = "Environment name is empty";
  public static final String TEST_TYPE_EMPTY = "Test type name is empty";

  public ValidationResult validate(ImportRequest request) {
    if (Strings.isNullOrEmpty(request.getProject())) {
      return new ValidationResult(PROJECT_EMPTY, false);
    }

    if (Strings.isNullOrEmpty(request.getEnvironment())) {
      return new ValidationResult(ENVIRONMENT_EMPTY, false);
    }

    if (Strings.isNullOrEmpty(request.getTestType())) {
      return new ValidationResult(TEST_TYPE_EMPTY, false);
    }

    return new ValidationResult(null, true);
  }
}
