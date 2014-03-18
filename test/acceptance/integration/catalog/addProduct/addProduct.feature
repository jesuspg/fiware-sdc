# -*- coding: utf-8 -*-
Feature: Add a new product in the catalogue
    As a fi-ware user
    I want to be able to add a new product in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario: add a new product in the catalogue
        Given the sdc is up and properly configured
        When I add a new product "Product_test_0001" in the catalog
        Then I receive an "OK" response with a "add Product" with one item

    @length
    Scenario: try to add a new product with name length major than 256 characters into of the catalog
        Given the sdc is up and properly configured
        When I add a new product "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" in the catalog
        Then  I receive an "Not Found" response with an "exception" error messages

    Scenario: try to add a new product with empty name  into of the catalog
        Given the sdc is up and properly configured
        When I add a new product "" in the catalog
        Then  I receive an "Not Found" response with an "exception" error messages