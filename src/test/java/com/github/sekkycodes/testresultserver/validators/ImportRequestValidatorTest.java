package com.github.sekkycodes.testresultserver.validators;

import static com.google.common.truth.Truth.assertThat;

import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.importing.ImportRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ImportRequestValidatorTest {

  private ImportRequestValidator importRequestValidator;

  private ImportRequest.ImportRequestBuilder importRequestBuilder;

  @BeforeEach
  void beforeEach() {
    importRequestValidator = new ImportRequestValidator();

    importRequestBuilder = FixtureHelper.buildImportRequest().toBuilder();
  }

  @Nested
  class Validate {

    @Test
    void negativeIfProjectIsEmpty() {
      ValidationResult result = importRequestValidator
          .validate(importRequestBuilder.project("").build());

      assertThat(result.isValid()).isFalse();
      assertThat(result.getValidationMessage()).isEqualTo(ImportRequestValidator.PROJECT_EMPTY);
    }

    @Test
    void negativeIfEnvironmentIsEmpty() {
      ValidationResult result = importRequestValidator
          .validate(importRequestBuilder.environment("").build());

      assertThat(result.isValid()).isFalse();
      assertThat(result.getValidationMessage()).isEqualTo(ImportRequestValidator.ENVIRONMENT_EMPTY);
    }

    @Test
    void negativeIfTestTypeIsEmpty() {
      ValidationResult result = importRequestValidator
          .validate(importRequestBuilder.testType("").build());

      assertThat(result.isValid()).isFalse();
      assertThat(result.getValidationMessage()).isEqualTo(ImportRequestValidator.TEST_TYPE_EMPTY);
    }

    @Test
    void positiveIfAllPropertiesAreSet() {
      ValidationResult result = importRequestValidator.validate(importRequestBuilder.build());

      assertThat(result.isValid()).isTrue();
      assertThat(result.getValidationMessage()).isNull();
    }
  }
}
