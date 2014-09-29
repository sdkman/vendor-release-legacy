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

  Scenario: Mark an existing Candidate Version as Default
    Given a "groovy" Version "2.3.5" with URL "http://hostname/groovy-binary-2.3.5.zip" already exists
    And a "groovy" Version "2.3.6" with URL "http://hostname/groovy-binary-2.3.6.zip" already exists
    And the existing Default "groovy" Version is "2.3.5"
    When a JSON PUT on the "/default" endpoint:
    """
        |{
        |   "candidate" : "groovy",
        |   "default" : "2.3.6"
        |}
    """
    Then the status received is "ACCEPTED"
    And the message "default groovy version: 2.3.6" is received
    And the Default "groovy" Version has changed to "2.3.6"

  @pending
  Scenario: Attempt to mark a non-existent Candidate Version as Default
    Given the existing Default "groovy" Version is "2.3.5"
    And Candidate "groovy" Version "2.3.6" does not exists
    When a JSON PUT on the "/default" endpoint:
    """
        |{
        |   "candidate" : "groovy",
        |   "default" : "2.3.6"
        |}
    """
    Then the status received is "BAD_REQUEST"
    And the message "not a valid groovy version: 2.3.6" is received

  @pending
  Scenario: Attempt to mark a non-existent Candidate Default
    Given Candidate "groovee" does not exist
    When a JSON PUT on the "/default" endpoint:
    """
        |{
        |   "candidate" : "groovee",
        |   "default" : "2.3.6"
        |}
    """
    Then the status received is "BAD_REQUEST"
    And the message "not a valid candidate: groovy" is received

  @pending
  Scenario: Remove a Version from a Candidate

  @pending
  Scenario: The Candidate of the Version to remove does not exist

  @pending
  Scenario: The Version to remove does not exist
