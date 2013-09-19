Feature: Delete Environment From Catalog

    Scenario: Delete  Tomcat 6 Environment
    Given a environment "tomcat-6_" present in the Catalog
    When I delete environment "tomcat-6_"
    Then there is not environment  "tomcat-6_" in the catalog
