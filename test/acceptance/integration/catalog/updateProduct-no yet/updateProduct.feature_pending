# -*- coding: utf-8 -*-
Feature: Update a product in the catalogue
    As a fi-ware user
    I want to be able to update a product in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario: update the name in a product in the catalogue
        Given The "Product_test_0001" has been created and the sdc is up and properly configured
        When update the name "prueba" in a product "Product_test_0001" in the catalog
        Then I receive an "OK" response with a "update name Product" with one item

