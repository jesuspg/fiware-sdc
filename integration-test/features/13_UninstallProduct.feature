Feature: Uninstall Product

    Scenario: Uninstall Tomcat 6
    Given "tomcat" "6" installed
    When I uninstall "tomcat" "6" from vdc
    Then I get "tomcat" "6" uninstalled
