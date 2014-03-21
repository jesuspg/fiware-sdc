# -*- coding: utf-8 -*-
Feature: List Products releases in the catalogue
    As a fi-ware user
    I want to be able to List Products in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario Outline: Get a release List of a Products in the catalogue with xml content in the response
       Given I add a new release "<release>" with description "version for test" associated to product "Product_test_0001" in the catalog with "xml" content in the response
        When I request releases list associated to a product "Product_test_0001" in the catalog with "xml" content in the response
        Then I receive an "OK" response with a "Product releases list XML" with "<release>" items with "xml" content
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
       Given I add a new release "<release>" with description "version for test" associated to product "Product_test_0001" in the catalog with "json" content in the response
        When I request releases list associated to a product "Product_test_0001" in the catalog with "json" content in the response
        Then I receive an "OK" response with a "Product releases list JSON" with "<release>" items with "json" content
    Examples:
      | release |
      |1.1.1    |
      |2.2.2    |
      |3.3.3    |
      |4.4.4    |
      |5.5.5    |
      |6.6.6    |

    @test
    Scenario: Cause an Not Found path error when list Products in the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I request a wrong path when list of existing products in the catalog
        Then I receive an "Not Found" response with an "exception" error messages

    #Scenario Outline: When list Products in the catalogue the token is not used
    #    Given The "Product_test_0001" has been created and the sdc is up and properly configured
    #    When I request unauthorized errors "<errorType>" when list of existing products in the catalog
    #           |errorType|
    #           |<errorType>|
    #    Then I receive an "unauthorized" response with an "exception" error messages
    #Examples:
    #  | errorType |
    #  |wrong|
    #  |empty|

    @skip @CLAUDIA-3732
    Scenario Outline: launch un bad method error when list Products in the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I list existing products in the catalog with request method is "<method>"
            |method|
            |<method>|
        Then I receive an "bad Method" response with an "exception" error messages
    Examples:
        | method |
        |DELETE|
        |PUT|





