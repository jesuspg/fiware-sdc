Feature: Delete Environment Instance

    Scenario: Delete Environment Instance Tomcat 6 + Postgresql8.4
    Given an environment instance present in the Catalog with products
    | Product     | Version   |
    | postgresql | 8.4 |
    | tomcat | 6 |
    When I delete the environment instance
    Then there is not "postgresql" "8" in the catalog
