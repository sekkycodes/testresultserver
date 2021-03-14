# Test Result Server

A simple Java server to store and analyse test results.

![Build](https://github.com/sekkycodes/testresultserver/workflows/Build/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sekkycodes_testresultserver&metric=alert_status)](https://sonarcloud.io/dashboard?id=sekkycodes_testresultserver)

# Usage

## Obtain Docker Image

From dockerhub:

    docker pull sekkydocks/testresultserver

Or you can build it yourself by following the instructions below.

## Compose Docker Container

The TestResultServer needs a MongoDB running as the database. For production use the application
expects another docker image with the name trs_mongo.

The simplest way to set it up correctly is to run the docker-compose file using

    docker compose up

## Use the app

By default, the server is running on port 8081.

To access the frontend open http://localhost:8081/index.html.

To see the API documentation open http://localhost:8081/swagger-ui.html.

# Development

## Getting Started

### Prerequisites

* [Docker](https://www.docker.com/)
* [Docker-Compose](https://docs.docker.com/compose/)
* [IntelliJ IDEA](https://www.jetbrains.com/de-de/idea/), [Eclipse](https://www.eclipse.org/ide/) or
  another Java-able IDE
* JDK11
* [MongoDb Docker](https://hub.docker.com/_/mongo)

To set up mongodb with docker run the following commands:

    docker pull mongo
    docker run -p 27017:27017 --name trs-mongo -d mongo:4.4.4

## Code Style

Import the intellij-java-google-style.xml when coding with IntelliJ IDEA.

## Build and Run

### Tests

To run tests:

    mvn test

This will also create a Jacoco test coverage report under target/site.

### Docker

Create docker image:

    mvn clean install -P prod -DskipTests
    mkdir -p target/dependency
    cd target/dependency; jar -xf ../*.jar
    cd ../..
    docker build -t testresultserver .

To run only the testresultserver in a docker container:

	docker run -p 8081:8081 testresultserver

Compose docker container (rebuilds images, includes mongoDB):

    docker-compose up --build
