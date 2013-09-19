Feature: Add Environment To Catalog

    Scenario: Insert Tomcat 6 Environment
    Given the following product release in the Catalog:
    |Product Name| Version |
    | tomcat | 6 |
    When I insert "tomcat-6_" Environment with description "new description"
    Then I get "tomcat-6_" in the catalog