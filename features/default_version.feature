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

Feature: Default Candidate Version

  Background:
    Given a valid security token "default_token"

  Scenario: Mark an existing Candidate Version as Default
    Given a "groovy" Version "2.3.5" with URL "http://hostname/groovy-binary-2.3.5.zip" already exists
    And a "groovy" Version "2.3.6" with URL "http://hostname/groovy-binary-2.3.6.zip" already exists
    And the existing Default "groovy" Version is "2.3.5"
    When a JSON PUT on the "/default" endpoint for consumer "groovy":
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
    When a JSON PUT on the "/default" endpoint for consumer "groovy":
    """
          |{
          |   "candidate" : "groovy",
          |   "default" : "2.3.6"
          |}
    """
    Then the status received is "BAD_REQUEST"
    And the error message received includes "invalid candidate version: groovy 2.3.6"

  Scenario: Attempt to mark a non-existent Candidate Default
    Given Candidate "groovee" does not exist
    When a JSON PUT on the "/default" endpoint for consumer "groovy":
    """
          |{
          |   "candidate" : "groovee",
          |   "default" : "2.3.6"
          |}
    """
    Then the status received is "BAD_REQUEST"
    And the error message received includes "not a valid candidate: groovee"

  Scenario: Attempt to submit malformed JSON with no candidate
    When a JSON PUT on the "/default" endpoint for consumer "groovy":
    """
          |{
          |   "default" : "2.3.6"
          |}
    """
    Then the status received is "BAD_REQUEST"
    And the error message received includes "on field 'candidate'"
    And the error message received includes "rejected value [null]"
    And the error message received includes "Candidate name can not be null"

  Scenario: Attempt to submit malformed JSON with no default version
    When a JSON PUT on the "/default" endpoint for consumer "groovy":
    """
          |{
          |   "candidate" : "groovy"
          |}
    """
    Then the status received is "BAD_REQUEST"
    And the error message received includes "on field 'version'"
    And the error message received includes "rejected value [null]"
    And the error message received includes "Default Version can not be null"
