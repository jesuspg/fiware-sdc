Feature: Install Application

    Scenario: Install SCD 1.0.0
    Given "tomcat" "6" added to the SDC Catalog
    And "tomcat" "6" installed
    And "postgresql" "8.4" installed
    When I install the application "sdc" "1.0.0" on products:
      | Product     | Version   |
      | postgresql | 8.4 |
      | tomcat | 6 |
    Then I get the application "sdc" "1.0.0" installed in vm
