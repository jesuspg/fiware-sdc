Feature: Update Environment To Catalog

    Scenario: Update Tomcat 6 Environment
    Given the environment "tomcat-6_"  in the Catalog
    When I update "tomcat-6_" Environment with the new description "new desc"
    Then I get "tomcat-6_" updated in the catalog