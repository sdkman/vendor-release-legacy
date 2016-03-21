# SDKMAN! Release API

This is a dedicated microservice API to aid vendors in Releasing their own Candidate Versions on SDKMAN!

It has the ability to release Candidate Versions, as well as to mark existing Versions as default for a Candidate.

## Endpoints

### Release a Candidate Version

This is an endpoint used to release a new Version of an existing Candidate. This does not make the Version the default version!
See what it does starting at this point in the [source](https://github.com/sdkman/sdkman-release-api/blob/master/src/main/scala/io/sdkman/release/releases.scala).

### Default a Candidate Version

This endpoint is used to mark an existing Version as the current default for that Candidate.
See what it does starting at this point in the [source](https://github.com/sdkman/sdkman-release-api/blob/master/src/main/scala/io/sdkman/release/defaults.scala).

## Build

The code can be built from source using the provided Gradle wrapper. This will also run all unit and acceptance tests.

    $ ./gradlew clean check

## Acceptance Tests

All behaviour of this application is described in the Cucumber [specifications](https://github.com/sdkman/sdkman-release-api/tree/master/features).