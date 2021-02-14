# Test Result Server

A simple Java server to store and analyse test results.

![Build](https://github.com/sekkycodes/testresultserver/workflows/Build/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sekkycodes_testresultserver&metric=alert_status)](https://sonarcloud.io/dashboard?id=sekkycodes_testresultserver)

# Usage

To see the API documentation open /swagger-ui.html on your running instance.

# Development

## Getting Started
### Prerequisites
* [IntelliJ IDEA](https://www.jetbrains.com/de-de/idea/), [Eclipse](https://www.eclipse.org/ide/) or another Java-able IDE
* JDK11

## Code Style

Import the intellij-java-google-style.xml when coding with IntelliJ IDEA.

## Build and Run

To run unit tests:

    mvn test

To run unit and integration tests:

    mvn verify

Run docker container:

    mvn install
    mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
    docker build -t sekkycodes/testresultserver .
    docker run -p 8080:8080 sekkycodes/testresultserver
