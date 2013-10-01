Feature: Delete Product From Catalog

  Scenario: Delete Postgresql 8
    Given a product "postgresql" "8" present in the Catalog
    When I delete "postgresql" "8"
    Then there is not "postgresql" "8" in the catalog
