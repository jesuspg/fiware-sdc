# -*- coding: utf-8 -*-
Feature: Delete a product in the catalogue
    As a fi-ware user
    I want to be able to delete a product in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario: Delete a product release in the catalogue
       Given A new product "Product_test_0002" with release "1.2.3" with description "version 1.2.3 for test" in the catalog
        When I delete a product "Product_test_0002" with release "1.2.3" in the catalog
        Then I receive an "No Content" response with a "delete Product release" with any item

    Scenario: Delete a product release with name very long (127 characters) in the catalogue
       Given A new product "Product_test_0002" with release "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" with description "version 1.2.3 for test" in the catalog
        When I delete a product "Product_test_0002" with release "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" in the catalog
        Then I receive an "No Content" response with a "delete Product release" with any item

    @404_error
    Scenario: Try to delete a product release that does no exist in the catalogue
       Given A new product "Product_test_0002" with release "1.2.3" with description "version 1.2.3 for test" in the catalog
        When I delete a product "Product_test_0002" with release "fdgdfgdfg" in the catalog
        Then I receive an "Not Found" response with a "Product does not exist" with any item

    @404_error
    Scenario: Try to delete a product release with a product that does no exist in the catalogue
       Given A new product "Product_test_0002" with release "1.2.3" with description "version 1.2.3 for test" in the catalog
        When I delete a product "Product_test_0002_error" with release "fdgdfgdfg" in the catalog
        Then I receive an "Not Found" response with a "Product does not exist" with any item

    @404_error
    Scenario: Cause an Not Found path error when delete a product release in the catalogue
       Given A new product "Product_test_0002" with release "1.2.3" with description "version 1.2.3 for test" in the catalog
        When I request a wrong path when delete a product "Product_test_0002" with release "1.2.3" in the catalogue
        Then I receive an "Not Found" response with an "exception" error messages


