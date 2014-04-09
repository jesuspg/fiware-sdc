# -*- coding: utf-8 -*-
Feature: List Products releases in the catalogue
    As a fi-ware user
    I want to be able to List Products in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario Outline: Get a release List of a Products in the catalogue with xml content in the response
       Given I add a new release "<release>" with description "version for test" associated to product "Product_test_0002" in the catalog with "xml" content in the response
        When I request releases list associated to a product "Product_test_0002" in the catalog with "xml" content in the response
        Then I receive an "OK" response with a "Product releases list XML" with "<release>" with "xml" content
    Examples:
      | release |
      |1.1.1    |
      |2.2.2    |
      |3.3.3    |
      |4.4.4    |
      |5.5.5    |
      |6.6.6    |

    @happy_path
    Scenario Outline: Get a release List of a Products in the catalogue with json content in the response
       Given I add a new release "<release>" with description "version for test" associated to product "Product_test_0002" in the catalog with "json" content in the response
        When I request releases list associated to a product "Product_test_0002" in the catalog with "json" content in the response
        Then I receive an "OK" response with a "Product releases list JSON" with "<release>" with "json" content
    Examples:
      | release |
      |1.1.1    |
      |2.2.2    |
      |3.3.3    |
      |4.4.4    |
      |5.5.5    |
      |6.6.6    |

    @400_error
    Scenario: Try to show all releases associated to a non-existent Product in the catalogue
        Given I add a new release "1.2.3" with description "version for test" associated to product "Product_test_0002" in the catalog with "xml" content in the response
        When  I request releases list associated to a product "Product_test_0002_error" in the catalog with "xml" content in the response
        Then I receive an "Not Found" response with a "exception" with "1.2.3" with "Any" content

    @401_error
    Scenario Outline: When list Product release in the catalogue the token is wrong or empty
        Given I add a new release "1.2.3" with description "version for test" associated to product "Product_test_0002" in the catalog with "xml" content in the response
        When I request unauthorized errors "<errorType>" when "getProductReleaseList" in the catalog
               |errorType|
              |<errorType>|
        Then I receive an "unauthorized" response with an "exception" error messages
    Examples:
      | errorType |
      |wrong|
      |empty|

    @404_error
    Scenario: Cause an Not Found path error when list Products releases in the catalogue
        Given I add a new release "1.2.3" with description "version for test" associated to product "Product_test_0002" in the catalog with "xml" content in the response
        When I request a wrong path when list of existing products releases in the catalog
        Then I receive an "Not Found" response with a "exception" with "1.2.3" with "Any" content








