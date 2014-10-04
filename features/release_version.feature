Feature: Release a Candidate Version

  Background:
    Given endpoint "/oauth/token" exchanges credentials "auth_username" and "auth_password" for a bearer token

  Scenario: Release a Candidate Version
    Given the existing Default "groovy" Version is "2.3.5"
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

  Scenario: Attempt to Release a Version for a non-existent Candidate
    Given Candidate "groovee" does not exist
    When a JSON POST on the "/release" endpoint:
    """
         |{
         |  "candidate" : "groovee",
         |  "version" : "2.3.6",
         |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
         |}"""
    Then the status received is "BAD_REQUEST"
    And the message "not a valid candidate: groovee" is received
    And Candidate "groovee" Version "2.3.6" does not exists

  Scenario: Attempt to submit malformed JSON with no Candidate
    When a JSON POST on the "/release" endpoint:
    """
         |{
         |  "version" : "2.3.6",
         |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
         |}"""
    Then the status received is "BAD_REQUEST"
    And the error message received includes "on field 'candidate': rejected value [null]"
    And the error message received includes "Candidate can not be null."

  Scenario: Attempt to submit malformed JSON with no Version
    When a JSON POST on the "/release" endpoint:
    """
         |{
         |  "candidate" : "groovy",
         |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
         |}"""
    Then the status received is "BAD_REQUEST"
    And the error message received includes "on field 'version': rejected value [null]"
    And the error message received includes "Version can not be null."

  Scenario: Attempt to submit malformed JSON with no URL
    When a JSON POST on the "/release" endpoint:
    """
         |{
         |  "candidate" : "groovy",
         |  "version" : "2.3.6"
         |}"""
    Then the status received is "BAD_REQUEST"
    And the error message received includes "on field 'url': rejected value [null]"
    And the error message received includes "URL can not be null."

