# -*- coding: utf-8 -*-
Feature: Add a new product release in the catalogue
    As a fi-ware user
    I want to be able to add a new product release in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario: add a new product release in the catalogue with xml content in the response
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I add a new release "1.2.3" with description "version 1.2.3 for test" associated to product "Product_test_0001" in the catalog with "xml" content in the response
        Then I receive an "OK" response with a "add Product release XML" with one item

    @happy_path
    Scenario: add a new product release in the catalogue with json content in the response
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I add a new release "1.2.3" with description "version 1.2.3 for test" associated to product "Product_test_0001" in the catalog with "json" content in the response
        Then I receive an "OK" response with a "add Product release JSON" with one item

    @400_errors @name_errors
    Scenario Outline: try to add a new product release with name errors into of the catalog
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I add a new release "<version>" with description "<error>" associated to product "Product_test_0001" in the catalog
        Then I receive an "Bad Request" response with a "invalid value in product release table" with one item

    Examples:
      | error | version |
      | length major than 128 characters |123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789|
      #| empty name                       | |



