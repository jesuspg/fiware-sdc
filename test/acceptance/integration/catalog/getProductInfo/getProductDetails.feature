# -*- coding: utf-8 -*-
Feature: Get details a product in the catalogue
    As a fi-ware user
    I want to be able to get details of a product in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario: Get details of a product in the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I get details of a product "Product_test_0001" in the catalog
        Then I receive an "OK" response with a "Product details" with some items

    @skip @CLAUDIA-3733
    Scenario: Try to get details of a non-existent product in the catalogue
        Given the sdc is up and properly configured
        When I get details of a product "gfhfgh" in the catalog
        Then I receive an "Internal Server Error" response with an "Product does not exist" error messages

    @test
    Scenario: Cause an Not Found path error when get details of a product in the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I request a wrong path when get details of a product "Product_test_0001" in the catalogue
        Then I receive an "Not Found" response with an "exception" error messages

    Scenario Outline: launch un bad method error get details of a product in the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When I try to get details of a product "Product_test_0001" in the catalog with request method is "<method>"
            |method|
            |<method>|
        Then I receive an "bad Method" response with an "exception" error messages
    Examples:
        | method |
        |PUT|
        |POST|