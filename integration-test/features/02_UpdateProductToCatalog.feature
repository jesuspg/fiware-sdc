Feature: Update Product To Catalog

    Scenario: Update Postgresql 8
    Given a product "postgresql" "8" in the Catalog
    And OS:
    |OS Name| Version |
    | windows | 7 |
    | debian | 9 |
    And default attributes:
    | Key | Value | Description |
    | key1 | value1 | description1 |
    | key2 | value2 | description2 |
    And the transitable releases:
    | Product | Version |
    
    When I update "postgresql" "8" ("a three times modified java web server ")
    Then I get product "postgresql" "8" updated in the catalog