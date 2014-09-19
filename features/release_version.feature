Feature: Release a Candidate Version

  #Create

  @release
  Scenario: Release a Default Version for a Candidate
    Given a Candidate "groovy"
    And an older Version "2.3.5" with URL http://dl.bintray.com/groovy/maven/groovy-binary-2.3.5.zip
    And a new Version "2.3.6" with URL http://dl.bintray.com/groovy/maven/groovy-binary-2.3.6.zip
    When the new Version is Released as Default
    Then the "groovy" Version "2.3.6" was Released
    And the Default Version for "groovy" is "2.3.6"

  @release
  Scenario: Release a Non-Default Version for a Candidate
    Given a Candidate "groovy"
    And an older Version "2.3.6" with URL http://dl.bintray.com/groovy/maven/groovy-binary-2.3.6.zip
    And a new Version "2.4.0-beta-3" with URL http://dl.bintray.com/groovy/maven/groovy-binary-2.4.0-beta-3.zip
    When the new Version is Released as Non-Default
    Then the "groovy" Version "2.4.0-beta-3" was Released
    And the Default Version for "groovy" is "2.3.6"

  @release @pending
  Scenario: The Candidate of the Release Version does not exist

  #Default

  @pending
  Scenario: Select a new Default Version for a Candidate

  @pending
  Scenario: The Candidate of the Version to Default does not exist

  @pending
  Scenario: The Version to Default does not exist

  #Remove

  @pending
  Scenario: Remove a Version from a Candidate

  @pending
  Scenario: The Candidate of the Version to remove does not exist

  @pending
  Scenario: The Version to remove does not exist
