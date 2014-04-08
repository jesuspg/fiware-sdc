# -*- coding: utf-8 -*-
Feature: Get details a product in the catalogue
    As a fi-ware user
    I want to be able to get details of a product in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario: Get details of a product in the catalogue with xml content in the response
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I get details of a product "Product_test_0001" in the catalog with "xml" content in the response
        Then I receive an "OK" response with a "Product details XML" with some items


    Scenario: Get details of a product in the catalogue with json content in the response
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I get details of a product "Product_test_0001" in the catalog with "json" content in the response
        Then I receive an "OK" response with a "Product details JSON" with some items

    @400_error @CLAUDIA-3733
    Scenario: Try to get details of a non-existent product in the catalogue
        Given the sdc is up and properly configured
        When I get details of a product "gfhfgh" in the catalog with "xml" content in the response
        Then I receive an "Bad Request" response with an "exception" error messages

    @401_error
    Scenario Outline: When list Products in the catalogue the token is not used
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I request unauthorized errors "<errorType>" when "getDetails" in the catalog
               |errorType|
              |<errorType>|
        Then I receive an "unauthorized" response with an "exception" error messages
    Examples:
      | errorType |
      |wrong|
      |empty|

    @404_error
    Scenario: Cause an Not Found path error when get details of a product in the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I request a wrong path when get details of a product "Product_test_0001" in the catalogue
        Then I receive an "Not Found" response with an "exception" error messages

    @405_error
    Scenario Outline: launch un bad method error get details of a product in the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I try to get details of a product "Product_test_0001" in the catalog with request method is "<method>"
            |method|
            |<method>|
        Then I receive an "Bad Method" response with an "exception" error messages
    Examples:
        | method |
        |PUT|
        |POST|

    @406_error
    Scenario: launch a Not acceptable error when I show  products details of the catalogue with Accept header invalid
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I get details of a product "Product_test_0001" in the catalog with "dfgdfg" content in the response
        Then I receive an "Not Acceptable" response with an "exception" error messages