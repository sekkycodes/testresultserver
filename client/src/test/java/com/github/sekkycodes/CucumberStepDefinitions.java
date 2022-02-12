package com.github.sekkycodes;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {

  private String filePath;

  @Given("a new feature file")
  public void aNewFeatureFile() {
    filePath = "cucumber.feature";
  }

  @When("the client is invoked with the cucumber option and the file path")
  public void theClientIsInvokedWithTheCucumberOptionAndTheFilePath() {

  }

  @Then("the scenarios are uploaded as new test cases to the server")
  public void theScenariosAreUploadedAsNewTestCasesToTheServer() {
  }
}
