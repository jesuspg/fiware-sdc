Feature: Upgrade Application

  Scenario: Upgrade SCD from  1.0.0 to 1.1.0
    Given "tomcat" "6" added to the SDC Catalog
    And "postgresql" "8.4" added to the SDC Catalog
    And "sdc" "1.0.0" added to the SDC Applications Catalog
    And "tomcat" "6" installed
    And "postgresql" "8.4" installed
    And application "sdc" "1.0.0" installed on products:
      | Product    | Version |
      | postgresql | 8.4     |
      | tomcat     | 6       |
    When I updrade to application "sdc" "1.1.0"
    Then I get application "sdc" upgraded from version "1.0.0" to "1.1.0" in VM
