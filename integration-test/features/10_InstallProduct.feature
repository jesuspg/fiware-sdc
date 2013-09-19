Feature: Install Product

    Scenario: Install Tomcat 6
    Given "tomcat" "6" added to the SDC Catalog
    When I install "tomcat" "6" in vm
    Then I get "tomcat" "6" installed in vm
