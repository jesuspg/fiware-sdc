# -*- coding: utf-8 -*-
Feature: List Products in the catalogue
    As a fi-ware user
    I want to be able to List Products in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario Outline: Get a List of Products in the catalogue with xml content in the response
       Given The "<product>" has been created and the sdc is up and properly configured
        When I request the list of existing products in the catalog with "xml" content in the response
        Then I check an "OK" response with a "Products list XML" with "<product>" with "xml" content
    Examples:
      | product         |
      |Product_test_0001|
      |Product_test_0002|
      |Product_test_0003|
      |Product_test_0004|
      |Product_test_0005|
      |Product_test_0006|

    @happy_path
    Scenario Outline: Get a List of Products in the catalogue with json content in the response
       Given The "<product>" has been created and the sdc is up and properly configured
        When I request the list of existing products in the catalog with "json" content in the response
        Then I check an "OK" response with a "Products list JSON" with "<product>" with "json" content
    Examples:
      | product         |
      |Product_test_0001|
      |Product_test_0002|
      |Product_test_0003|
      |Product_test_0004|
      |Product_test_0005|
      |Product_test_0006|

    @401_error
    Scenario Outline: When list Products in the catalogue the token is not used
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I request unauthorized errors "<errorType>" when "getProductList" in the catalog
               |errorType|
              |<errorType>|
        Then I receive an "unauthorized" response with an "exception" error messages
    Examples:
      | errorType |
      |wrong|
      |empty|

    @404_error
    Scenario: Cause an Not Found path error when list Products in the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I request a wrong path when list of existing products in the catalog
        Then I receive an "Not Found" response with an "exception" error messages

    @405_error
    Scenario Outline: launch a bad method error when I show products of the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I list existing products in the catalog with request method is "<method>"
            |method|
            |<method>|
        Then I receive an "Bad Method" response with an "exception" error messages
    Examples:
        | method |
        |DELETE|
        |PUT|

    @406_error
    Scenario: launch a Not acceptable error when I show  products of the catalogue with Accept header invalid
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I request the list of existing products in the catalog with "error in Accept" content in the response
        Then I receive an "Not Acceptable" response with an "exception" error messages






