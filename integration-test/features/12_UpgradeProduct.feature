Feature: Upgrade Product

    Scenario: Upgrade Tomcat 6 to Tomcat 5.5
    Given "tomcat" "6" installed in vm and vdc
    When I updrade to "tomcat" "5.5"
    Then I get "tomcat" upgraded from version "6" to "5.5" in VM
