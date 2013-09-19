Feature: Install EnvironmentInstance

    Scenario: EnvironmentInstance Tomcat 6 + Postgresql8.4
    Given OS:
    |OS Name| OSType | Version |
    | Windows XP | 67 | 7 |
    | Debian | 95 | 9 |
    And default attributes:
    | Key | Value | Description |
    | key1 | value1 | description1 |
    | key2 | value2 | description2 |
    And the transitable releases:
    | Product | Version |
    Given the following product releases added to the SDC Catalog
    | Product     | Version   |
    | postgresql | 8.4 |
    | tomcat | 6 |
    And following product instances are installed
      | Product     | Version   |
      | postgresql | 8.4 |
      | tomcat | 6 |
    When I update an environment Instance with description "desc modified"  products:
      | Product     | Version   |
      | postgresql | 8.4 |
      | tomcat | 6 |
    Then I get an environment Instance with new description
