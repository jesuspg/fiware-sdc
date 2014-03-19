# -*- coding: utf-8 -*-
Feature: List Products releases in the catalogue
    As a fi-ware user
    I want to be able to List Products in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario: Get a release List of a Products in the catalogue
        Given A new product "Product_test_0001" with release "1.2.3" with description "version 1.2.3 for test" in the catalog
        When I request releases list associated to a product "Product_test_0001" in the catalog
        Then I receive an "OK" response with a "Product release list" with one items

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





