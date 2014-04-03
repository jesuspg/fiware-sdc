# -*- coding: utf-8 -*-
Feature: Get details a product in the catalogue
    As a fi-ware user
    I want to be able to get details of a product in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario: Get details of a product release in the catalogue with xml content in the response
       Given I add a new release "1.2.3" with description "version for test" associated to product "Product_test_0002" in the catalog with "xml" content in the response
        When I get details of release "1.2.3" associated to a product "Product_test_0002" in the catalog with "xml" content in the response
        Then I receive an "OK" response with a "Product releases details XML" with "1.2.3" with "xml" content

    @happy_path
    Scenario: Get details of a product release in the catalogue with json content in the response
       Given I add a new release "1.2.3" with description "version for test" associated to product "Product_test_0002" in the catalog with "json" content in the response
        When I get details of release "1.2.3" associated to a product "Product_test_0002" in the catalog with "json" content in the response
        Then I receive an "OK" response with a "Product releases details JSON" with "1.2.3" with "json" content

    @401_error
    Scenario Outline: When list Product release in the catalogue the token is wrong or empty
        Given I add a new release "1.2.3" with description "version for test" associated to product "Product_test_0002" in the catalog with "xml" content in the response
        When I request unauthorized errors "<errorType>" when "getProductReleaseDetails" in the catalog
               |errorType|
              |<errorType>|
        Then I receive an "unauthorized" response with an "exception" error messages
    Examples:
      | errorType |
      |wrong|
      |empty|

    @400_error
    Scenario: Try to get details of a non-existent product release in the catalogue
       Given I add a new release "1.2.3" with description "version for test" associated to product "Product_test_0002" in the catalog with "json" content in the response
        When I get details of release "1.2.3.4.5.6" associated to a product "Product_test_0002" in the catalog with "json" content in the response
        Then I receive an "Bad Request" response with a "exception" with "1.2.3" with "Any" content


    @404_error
    Scenario: Cause an Not Found path error when show details of a Product release in the catalogue
        Given I add a new release "1.2.3" with description "version for test" associated to product "Product_test_0002" in the catalog with "xml" content in the response
        When I request a wrong path when list of existing products releases in the catalog
        Then I receive an "Not Found" response with a "exception" with "1.2.3" with "Any" content