Feature: Delete Product Release Cookbook From Chef Server

    Scenario: Delete tomcat 7
    Given OS:
    |OS Name| Version |
    | deleteCookbookOS1 | 7 |
    | deleteCookbookOS2 | 9 |
    And default attributes:
    | Key | Value | Description |
    | key1 | value1 | description1 |
    | key2 | value2 | description2 |
    And the transitable releases:
    | Product | Version |
    And following Products Releases added to the SDC Catalog:
    | Product| Version | Description |
    | deleteCookbook | 7 | deleteCookbook 7 release |
    | deleteCookbook | 6 | deleteCookbook 6 release |
    
    When I delete product release "deleteCookbook" "6" and "7" from the catalog
    Then There is not any product release cookbook "deleteCookbook" in the chef-server