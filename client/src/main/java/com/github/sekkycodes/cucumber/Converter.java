package com.github.sekkycodes.cucumber;

import com.github.sekkycodes.dtos.TestCase;
import io.cucumber.messages.types.Feature;
import io.cucumber.messages.types.FeatureChild;
import io.cucumber.messages.types.GherkinDocument;
import io.cucumber.messages.types.Scenario;
import io.cucumber.messages.types.Tag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converts gherkin documents to test cases. Features are considered when converting to test cases.
 * Also, tags starting with 'ID' are interpreted as feature and test case IDs.
 */
public class Converter {

  /**
   * Converts gherkin document to test cases
   *
   * @param doc feature document written in gherkin style
   * @return a list of test cases parsed from the scenarios
   */
  public List<TestCase> convert(GherkinDocument doc) {
    String featureId = getFeatureId(doc.getFeature()).orElse("");
    List<TestCase> testCases = new ArrayList<>();

    for (FeatureChild fc : doc.getFeature().getChildren()) {
      Scenario scenario = fc.getScenario();

      TestCase tc = convert(scenario)
          .feature(doc.getFeature().getName())
          .featureId(featureId)
          .build();
      testCases.add(tc);
    }

    return testCases;
  }

  private TestCase.TestCaseBuilder convert(Scenario scenario) {
    String stepsText = scenario.getSteps().stream()
        .map(st -> st.getKeyword() + st.getText()).collect(Collectors.joining("\n"));
    String testCaseId = getTestCaseId(scenario).orElse("");

    return TestCase.builder()
        .name(scenario.getName())
        .description(stepsText)
        .testCaseId(testCaseId);
  }

  private Optional<String> getFeatureId(Feature feature) {
    return getId(feature.getTags());
  }

  private Optional<String> getTestCaseId(Scenario scenario) {
    return getId(scenario.getTags());
  }

  private Optional<String> getId(Collection<Tag> tags) {
    Optional<Tag> tag = tags.stream()
        .filter(f -> f.getName().startsWith("@ID_") && f.getName().length() > 4)
        .findFirst();
    return tag.map(t -> t.getName().substring(4));
  }
}
