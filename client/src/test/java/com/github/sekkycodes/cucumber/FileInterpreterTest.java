package com.github.sekkycodes.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.FileSystem;
import com.github.sekkycodes.dtos.TestCase;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileInterpreterTest {

  private FileInterpreter sut;

  @Mock
  private FileSystem fileSystem;

  private static final String gherkinText = "@ID_F001\n"
      + "Feature: Cucumber Feature File Interpretation\n"
      + "  Developer wants feature files to be interpreted and sent to test result server for test result mapping.\n"
      + "\n"
      + "  @ID_TC001\n"
      + "  Scenario: Upload new feature file\n"
      + "    Given a new feature file\n"
      + "    When the client is invoked with the cucumber option and the file path\n"
      + "    Then the scenarios are uploaded as new test cases to the server"
      + "\n"
      + "  @SomeTag\n"
      + "  Scenario: Upload existing feature file\n"
      + "    Given an existing feature file\n"
      + "    When the client is invoked with the cucumber option and the file path\n"
      + "    Then the scenarios are uploaded as existing test cases to the server"
      + "    And existing test cases are updated";

  @BeforeEach
  void beforeEach() throws IOException {
    when(fileSystem.read(any())).thenReturn(gherkinText);

    sut = new FileInterpreter(fileSystem);
  }

  @Test
  void parsesCucumberFeatureFileToTestCases() throws IOException {
    List<TestCase> tcs = sut.parse(Path.of("some.feature"));

    assertThat(tcs).hasSize(2);
    TestCase tc1 = tcs.get(0);
    assertThat(tc1.getFeature()).isEqualTo("Cucumber Feature File Interpretation");
    assertThat(tc1.getName()).isEqualTo("Upload new feature file");
    assertThat(tc1.getDescription()).contains(
        "When the client is invoked with the cucumber option and the file path");
    assertThat(tc1.getFeatureId()).isEqualTo("F001");
    assertThat(tc1.getTestCaseId()).isEqualTo("TC001");

    TestCase tc2 = tcs.get(1);
    assertThat(tc2.getFeature()).isEqualTo("Cucumber Feature File Interpretation");
    assertThat(tc2.getName()).isEqualTo("Upload existing feature file");
    assertThat(tc2.getDescription()).contains("And existing test cases are updated");
    assertThat(tc2.getFeatureId()).isEqualTo("F001");
    assertThat(tc2.getTestCaseId()).isEmpty();
  }
}
