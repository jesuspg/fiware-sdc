# -*- coding: utf-8 -*-
Feature: Delete a product in the catalogue
    As a fi-ware user
    I want to be able to delete a product in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario: Delete a product in the catalogue with xml content in the response
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I delete a product "Product_test_0001" in the catalog with "xml" content in the response
        Then I receive an "No Content" response with a "delete Product" with any item

    @happy_path
    Scenario: Delete a product in the catalogue with json content in the response
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I delete a product "Product_test_0001" in the catalog with "json" content in the response
        Then I receive an "No Content" response with a "delete Product" with any item

    Scenario: Delete a product with name very long (255 characters) in the catalogue
        Given The "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345" has been created and the sdc is up and properly configured
        When I delete a product "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345" in the catalog with "xml" content in the response
        Then I receive an "No Content" response with a "delete Product" with any item

    @400_error @CLAUDIA-3733
    Scenario: Try to delete a product that does no exist in the catalogue
        Given the sdc is up and properly configured
        When I delete a product "gfhfgh" in the catalog with "xml" content in the response
        Then I receive an "Bad Request" response with an "exception" error messages

    @401_error
    Scenario Outline: When delete a Product of the catalogue the token is not used or it is wrong
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I request unauthorized errors "<errorType>" when delete a product Product_test_0001
               |errorType|
              |<errorType>|
        Then I receive an "unauthorized" response with an "exception" error messages
    Examples:
      | errorType |
      |wrong|
      |empty|

    @404_error
    Scenario: Cause an Not Found path error when delete a product in the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I request a wrong path when delete a product "Product_test_0001" in the catalogue
        Then I receive an "Not Found" response with an "exception" error messages

    @415_error
    Scenario: Cause a Not acceptable error when I delete a product into the catalogue with Content-Type header invalid
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I delete a product "Product_test_0001" in the catalog with "error in Content-Type" content in the response
        Then I receive an "Unsupported Media Type" response with an "exception" error messages