Feature: Release a Candidate Version

  Background:
    Given endpoint "/oauth/token" exchanges credentials "auth_username" and "auth_password" for a bearer token

  Scenario: Release a Candidate Version
    When a JSON POST on the "/release" endpoint:
    """
         |{
         |  "candidate" : "groovy",
         |  "version" : "2.3.6",
         |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
         |}"""
    Then the status received is "CREATED"
    And a valid identifier is received in the response
    And the message "released groovy version: 2.3.6" is received
    And "groovy" Version "2.3.6" with URL "http://hostname/groovy-binary-2.3.6.zip" was published

  @pending
  Scenario: Remove a Candidate Version

  @pending
  Scenario: The Candidate of the Version to remove does not exist

  @pending
  Scenario: The Version to remove does not exist
