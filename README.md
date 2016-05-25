# SDKMAN! Vendor Release API

This is a dedicated microservice API to aid Vendors in Releasing their own Candidate Versions on SDKMAN!

It has the ability to release Candidate Versions, as well as to mark existing Versions as default for a Candidate.

## Endpoints

### Release a Candidate Version

This is an endpoint used to release a new Version of an existing Candidate. This does not make the Version the default version!
See what it does starting at this point in the [source](https://github.com/sdkman/vendor-release/blob/master/src/main/scala/io/sdkman/release/releases.scala).

### Default a Candidate Version

This endpoint is used to mark an existing Version as the current default for that Candidate.
See what it does starting at this point in the [source](https://github.com/sdkman/vendor-release/blob/master/src/main/scala/io/sdkman/release/defaults.scala).

## Running it up locally

You will need to have MongoDB up and running locally on the default port.

    $ docker run --rm --net=host mongo:latest

Once running, step into the project folder and build.

    $ ./gradlew clean assemble

We can now run the app up locally with a simple

    $ java -jar build/libs/application.jar

## Running tests

The service has a comprehensive suite of Acceptance Tests, as well as Unit Tests that can be run as follows:

    $ ./gradlew check


### Acceptance Tests

All behaviour of this application is described in the Cucumber [specifications](https://github.com/sdkman/vendor-release/tree/master/features).
