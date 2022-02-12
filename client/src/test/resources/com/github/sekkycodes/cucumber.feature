Feature: Cucumber Feature File Interpretation
  Developer wants feature files to be interpreted and sent to test result server for test result mapping.

  Scenario: Upload new feature file
    Given a new feature file
    When the client is invoked with the cucumber option and the file path
    Then the scenarios are uploaded as new test cases to the server