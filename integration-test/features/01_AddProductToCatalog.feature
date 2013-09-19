Feature: Add Product To Catalog

    Scenario: Add Postgresql 8
    Given OS:
    |OS Name| Version |
    | windows | 7 |
    | debian | 9 |
    And default attributes:
    | Key | Value | Description |
    | key1 | value1 | description1 |
    | key2 | value2 | description2 |
    And the transitable releases:
    | Product | Version |

    When I add product "postgresql" "8" ("a java web server")
    Then I get product "postgresql" "8" in the catalog
