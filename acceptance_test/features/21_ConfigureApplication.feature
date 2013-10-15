Feature: Configure Application

  Scenario: Configure SCD 1.0.0
    Given "tomcat" "6" added to the SDC Catalog
    And "postgresql" "8.4" added to the SDC Catalog
    And "sdc" "1.0.0" added to the SDC Applications Catalog
    And "tomcat" "6" installed
    And "postgresql" "8.4" installed
    And application "sdc" "1.0.0" installed on products:
      | Product    | Version |
      | postgresql | 8.4     |
      | tomcat     | 6       |
    When I configure the application "sdc" "1.0.0" with "context" "app-conf"
    Then I get the application "sdc" "1.0.0" configured with "context" "app-conf"
