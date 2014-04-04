# -*- coding: utf-8 -*-
Feature: Add a new product release in the catalogue
    As a fi-ware user
    I want to be able to add a new product release in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario: add a new product release in the catalogue with xml content in the response
        Given The "Product_test_0002" has been created and the sdc is up and properly configured
        When I add a new release "1.2.3" with description "version 1.2.3 for test" associated to product "Product_test_0002" in the catalog with "xml" content in the response
        Then I receive an "OK" response with a "add Product release XML" with one item

    @happy_path
    Scenario: add a new product release in the catalogue with json content in the response
       Given The "Product_test_0002" has been created and the sdc is up and properly configured
        When I add a new release "1.2.3" with description "version 1.2.3 for test" associated to product "Product_test_0002" in the catalog with "json" content in the response
        Then I receive an "OK" response with a "add Product release JSON" with one item

    @skip @CLAUDIA-3754
    Scenario: add a new release with non-existent product in the catalogue
        Given the sdc is up and properly configured
        When I add a new release "1.2.3" with description "version 1.2.3 for test" associated to product "Product_test_0002_error" in the catalog with "xml" content in the response
        Then I receive an "OK" response with a "add product release without product" with one item

    @400_error
    Scenario Outline: try to add a new product release with name errors into of the catalog
        Given The "Product_test_0002" has been created and the sdc is up and properly configured
         When I add a new release "<version>" with description "<error>" associated to product "Product_test_0002" in the catalog with "xml" content in the response
         Then I receive an "Bad Request" response with a "invalid value in product release table" with one item

    Examples:
      | error | version |
      | length major than 128 characters |123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789|
      #| empty name                       | |

    @401_error
    Scenario Outline: When add Product release in the catalogue the token is not used or it is wrong
        Given the sdc is up and properly configured
        When I request unauthorized errors "<errorType>" when add a new product "Product_test_0002" with release "1.2.3" in the catalog with xml content in the response
               |errorType|
              |<errorType>|
        Then I receive an "unauthorized" response with an "exception" error messages
    Examples:
      | errorType |
      |wrong|
      |empty|

    @404_error
    Scenario: Cause an Not Found path error when I add a new product release in the catalogue
        Given The "Product_test_0002 has been created and the sdc is up and properly configured
        When I request a wrong path when I add a new product release in the catalog
        Then I receive an "Not Found" response with an "exception" error messages

    @406_error
    Scenario: launch a Not acceptable error when I add a new product release into the catalogue with Accept header invalid
        Given The "Product_test_0002" has been created and the sdc is up and properly configured
        When  I add a new release "1.2.3" with description "version 1.2.3 for test" associated to product "Product_test_0002" in the catalog with "error in Accept" content in the response
        Then I receive an "Not Acceptable" response with an "exception" error messages

    @415_error
    Scenario: launch a Not acceptable error when I add a new product release into the catalogue with Content-Type header invalid
        Given The "Product_test_0002" has been created and the sdc is up and properly configured
        When  I add a new release "1.2.3" with description "version 1.2.3 for test" associated to product "Product_test_0002" in the catalog with "error in Content-Type" content in the response
        Then I receive an "Unsupported Media Type" response with an "exception" error messages



