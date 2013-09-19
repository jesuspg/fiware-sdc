Feature: Configure Product

    Scenario: Configure Tomcat 6
    Given "tomcat" "6" added to the SDC Catalog
    And "tomcat" "6" installed
    When I configure the product with "port" "8083"
    Then I get the product configured with "port" "8083"
