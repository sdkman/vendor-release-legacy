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

Feature: Security

  Scenario: The Release endpoints can NOT be Accessed when not Authorised
    Given the Client is not Authorised and Authenticated
    When a JSON POST on the "/release" endpoint:
    """
          |{
          |  "candidate" : "groovy",
          |  "version" : "2.3.6",
          |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
          |}
    """
    Then the status received is "FORBIDDEN"

  Scenario: The Release endpoints CAN be Accessed when Authorised as valid Consumer
    Given the Client is Authorised and Authenticated as "groovy"
    And the appropriate candidate already exists
    When a JSON POST on the "/release" endpoint:
    """
          |{
          |  "candidate" : "groovy",
          |  "version" : "2.3.6",
          |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
          |}
    """
    Then the status received is "CREATED"

  Scenario: The Release endpoints CAN be Accessed when Authorised as Administrator
    Given the Client is Authorised and Authenticated as "default_admin"
    And the appropriate candidate already exists
    When a JSON POST on the "/release" endpoint:
    """
          |{
          |  "candidate" : "groovy",
          |  "version" : "2.3.6",
          |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
          |}
    """
    Then the status received is "CREATED"

  Scenario: The Default endpoints can NOT be Accessed when not Authorised
    Given the Client is not Authorised and Authenticated
    And a "groovy" Version "2.3.5" with URL "http://hostname/groovy-binary-2.3.5.zip" already exists
    And a "groovy" Version "2.3.6" with URL "http://hostname/groovy-binary-2.3.6.zip" already exists
    And the existing Default "groovy" Version is "2.3.5"
    When a JSON PUT on the "/default" endpoint:
    """
          |{
          |   "candidate" : "groovy",
          |   "default" : "2.3.6"
          |}
    """
    Then the status received is "FORBIDDEN"

  Scenario: The Default endpoints CAN be Accessed when Authorised as valid Consumer
    Given the Client is Authorised and Authenticated as "groovy"
    And a "groovy" Version "2.3.5" with URL "http://hostname/groovy-binary-2.3.5.zip" already exists
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

  Scenario: The Default endpoints CAN be Accessed when Authorised as Administrator
    Given the Client is Authorised and Authenticated as "default_admin"
    And a "groovy" Version "2.3.5" with URL "http://hostname/groovy-binary-2.3.5.zip" already exists
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

  @pending
  Scenario: The Admin Mappings endpoint can NOT be Accessed without Authorisation
    Given the Client is not Authorised and Authenticated
    When the "/admin/mappings" endpoint is accessed
    Then the status received is "FORBIDDEN"

  Scenario: The Admin Mappings endpoint can be Accessed with Authorisation
    Given the Client is Authorised and Authenticated as "groove"
    When the "/admin/mappings" endpoint is accessed
    Then the status received is "OK"

  Scenario: The Admin Info endpoint can always be Accessed without Authorisation
    Given the Client is not Authorised and Authenticated
    When the "/admin/info" endpoint is accessed
    Then the status received is "OK"

  Scenario: The Admin Health endpoint can always be Accessed without Authorisation
    Given the Client is not Authorised and Authenticated
    When the "/admin/health" endpoint is accessed
    Then the status received is "OK"