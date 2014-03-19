# -*- coding: utf-8 -*-
Feature: Delete a product in the catalogue
    As a fi-ware user
    I want to be able to delete a product in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario: Delete a product in the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I delete a product "Product_test_0001" in the catalog
        Then I receive an "No Content" response with a "delete Product" with any item

    @very_long
    Scenario: Delete a product with name very long (255 characters) in the catalogue
        Given The "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345" has been created and the sdc is up and properly configured
        When I delete a product "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345" in the catalog
        Then I receive an "No Content" response with a "delete Product" with any item

    @skip @CLAUDIA-3733
    Scenario: Try to delete a product that does no exist in the catalogue
        Given the sdc is up and properly configured
        When I delete a product "gfhfgh" in the catalog
        Then I receive an "Not Found" response with an "Product does not exist" error messages

    Scenario: Cause an Not Found path error when delete a product in the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I request a wrong path when delete a product "Product_test_0001" in the catalogue
        Then I receive an "Not Found" response with an "exception" error messages
