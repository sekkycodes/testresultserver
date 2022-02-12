package com.github.sekkycodes;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class ServerMock {

  protected WireMockServer serverMock;

  @BeforeAll
  void beforeAllTestMethods() {
    serverMock = new WireMockServer(WireMockConfiguration.options().dynamicPort());
    stubTestResultServerMock();
    serverMock.start();
  }

  @BeforeEach
  void beforeEachTestMethod() {
    serverMock.resetRequests();
  }

  @AfterAll
  void afterAllTestMethods() {
    serverMock.stop();
  }

  private void stubTestResultServerMock() {
    serverMock.stubFor(post(urlEqualTo("/createTestCase"))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_CREATED)));
  }
}
