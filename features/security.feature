Feature: Security

  @pending
  Scenario: The Release endpoints can NOT be Accessed when not Authorised

  @pending
  Scenario: The Release endpoints CAN be Accessed when Authorised

  @pending
  Scenario: The Candidates endpoint can always be Accessed without Authorisation
    Given the Client is not Authorised and Authenticated
    When the "/candidates" endpoint is accessed
    Then an "OK" status is returned

  Scenario: The Admin Mappings endpoint can NOT be Accessed without Authorisation
    Given the Client is not Authorised and Authenticated
    When the "/admin/mappings" endpoint is accessed
    Then an "Unauthorized" status is returned

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
