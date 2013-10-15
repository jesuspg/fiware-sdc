Feature: Delete Application From Catalog

  Scenario: Delete sdcTest 1.0.0
    Given a application "sdcTest" "1.0.0" present in the Catalog
    When I delete application "sdcTest" "1.0.0"
    Then there is not application "sdcTest" "1.0.0" in the catalog
