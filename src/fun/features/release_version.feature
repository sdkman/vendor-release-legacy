#
#  Copyright 2014 Marco Vermeulen
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#  http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

Feature: Release a Candidate Version

  Background:
    Given the Client is Authorised and Authenticated as "groovy"

  Scenario: Release a Candidate Version
    Given the existing Default "groovy" Version is "2.3.5"
    When a JSON POST on the "/release" endpoint:
    """
          |{
          |  "candidate" : "groovy",
          |  "version" : "2.3.6",
          |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
          |}
    """
    Then the status received is "CREATED"
    And a valid identifier is received in the response
    And the message "released groovy version: 2.3.6" is received
    And "groovy" Version "2.3.6" with URL "http://hostname/groovy-binary-2.3.6.zip" was published

  Scenario: Attempt to Release a duplicate Version
    Given the existing Default "groovy" Version is "2.3.5"
    When a JSON POST on the "/release" endpoint:
    """
          |{
          |  "candidate" : "groovy",
          |  "version" : "2.3.6",
          |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
          |}
    """
    And a JSON POST on the "/release" endpoint:
    """
          |{
          |  "candidate" : "groovy",
          |  "version" : "2.3.6",
          |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
          |}
    """
    Then the status received is "CONFLICT"
    And the error message received includes "duplicate candidate version: groovy 2.3.6"

  Scenario: Attempt to Release a Version for a non-existent Candidate
    Given Candidate "groovy" does not exist
    When a JSON POST on the "/release" endpoint:
    """
          |{
          |  "candidate" : "groovy",
          |  "version" : "2.3.6",
          |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
          |}
    """
    Then the status received is "BAD_REQUEST"
    And the error message received includes "not a valid candidate: groovy"
    And Candidate "groovy" Version "2.3.6" does not exists

  Scenario: Attempt to submit malformed JSON with no Candidate
    When a JSON POST on the "/release" endpoint:
    """
          |{
          |  "version" : "2.3.6",
          |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
          |}
    """
    Then the status received is "BAD_REQUEST"
    And the error message received includes "on field 'candidate': rejected value [null]"
    And the error message received includes "Candidate can not be null."

  Scenario: Attempt to submit malformed JSON with no Version
    When a JSON POST on the "/release" endpoint:
    """
          |{
          |  "candidate" : "groovy",
          |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
          |}
    """
    Then the status received is "BAD_REQUEST"
    And the error message received includes "on field 'version': rejected value [null]"
    And the error message received includes "Version can not be null."

  Scenario: Attempt to submit malformed JSON with no URL
    When a JSON POST on the "/release" endpoint:
    """
          |{
          |  "candidate" : "groovy",
          |  "version" : "2.3.6"
          |}
    """
    Then the status received is "BAD_REQUEST"
    And the error message received includes "on field 'url': rejected value [null]"
    And the error message received includes "URL can not be null."