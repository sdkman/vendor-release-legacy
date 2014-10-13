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
    When the "/release" endpoint receives a POST with valid payload:
    """
        |{
        |  "candidate" : "groovy",
        |  "version" : "2.3.6",
        |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
        |}
    """
    Then an "UNAUTHORIZED" status is returned

  Scenario: The Release endpoints CAN be Accessed when Authorised
    Given the Client is Authorised and Authenticated
    And the appropriate candidate already exists
    When the "/release" endpoint receives a POST with valid payload:
    """
        |{
        |  "candidate" : "groovy",
        |  "version" : "2.3.6",
        |  "url" : "http://hostname/groovy-binary-2.3.6.zip"
        |}
    """
    Then an "CREATED" status is returned

  Scenario: The Admin Mappings endpoint can NOT be Accessed without Authorisation
    Given the Client is not Authorised and Authenticated
    When the "/admin/mappings" endpoint is accessed
    Then an "UNAUTHORIZED" status is returned

  Scenario: The Admin Mappings endpoint can be Accessed with Authorisation
    Given the Client is Authorised and Authenticated
    When the "/admin/mappings" endpoint is accessed
    Then an "OK" status is returned

  Scenario: The Admin Info endpoint can always be Accessed without Authorisation
    Given the Client is not Authorised and Authenticated
    When the "/admin/info" endpoint is accessed
    Then an "OK" status is returned

  Scenario: The Admin Health endpoint can always be Accessed without Authorisation
    Given the Client is not Authorised and Authenticated
    When the "/admin/health" endpoint is accessed
    Then an "OK" status is returned

