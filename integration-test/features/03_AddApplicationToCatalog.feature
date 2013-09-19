Feature: Add Application To Catalog

    Scenario: Add sdcTest 1.0
    Given application default attributes:
    | Key | Value | Description |
    | key1 | value1 | description1 |
    | key2 | value2 | description2 |
    And the application transitable releases:
    | Application | Version |
    And the application supported product release
    |Product Name | Version |
    | postgresql | 8 |
    | tomcat | 6 |
    When I add application "sdcTest" "1.0.0" of type "war" ("our own application sdc")
    Then I get application "sdcTest" "1.0.0" in the catalog
