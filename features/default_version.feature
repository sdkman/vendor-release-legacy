Feature: Default Candidate Version

  Background:
    Given endpoint "/oauth/token" exchanges credentials "auth_username" and "auth_password" for a bearer token

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
    And the message "invalid candidate version: groovy 2.3.6" is received

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
    And the message "not a valid candidate: groovee" is received

  Scenario: Attempt to submit malformed JSON with no candidate
    When a JSON PUT on the "/default" endpoint:
    """
        |{
        |   "default" : "2.3.6"
        |}
    """
    Then the status received is "BAD_REQUEST"
    And the error message received includes "Field error in object 'defaultVersionRequest'"
    And the error message received includes "on field 'candidate'"
    And the error message received includes "rejected value [null]"

  Scenario: Attempt to submit malformed JSON with no default version
    When a JSON PUT on the "/default" endpoint:
    """
        |{
        |   "candidate" : "groovy"
        |}
    """
    Then the status received is "BAD_REQUEST"
    And the error message received includes "Field error in object 'defaultVersionRequest'"
    And the error message received includes "on field 'defaultVersion'"
    And the error message received includes "rejected value [null]"