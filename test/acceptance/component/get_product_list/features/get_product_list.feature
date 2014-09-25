# -*- coding: utf-8 -*-
Feature: Get all products from catalog
    As a fi-ware user
    I want to be able to get all products in the catalogue
    so that I can use the products.

  @happy_path
  Scenario Outline: Retrieve a product list with products created before

    Given a created product with name "<product_name>"
    When I retrieve the list product with accept parameter "<accept_parameter>" response
    Then the product is returned in the list

    Examples:

    | product_name      | accept_parameter  |
    | testing_list01    | application/json  |
    | testing_list02    | application/xml   |

  Scenario Outline: Retrieve a product list with products created before with attributes

    Given a created product with attributes and name "<product_name>"
    When I retrieve the list product with accept parameter "<accept_parameter>" response
    Then the product is returned in the list

    Examples:

    | product_name    | accept_parameter  |
    | testing_list03  | application/json  |
    | testing_list04  | application/xml   |

  Scenario Outline: Retrieve a product list with products created before with metadatas

    Given a created product with metadatas and name "<product_name>"
    When I retrieve the list product with accept parameter "<accept_parameter>" response
    Then the product is returned in the list

    Examples:

    | product_name    | accept_parameter  |
    | testing_list05  | application/json  |
    | testing_list06  | application/xml   |

  Scenario Outline: Retrieve a product list with products created before with all data

    Given a created product with all data and name "<product_name>"
    When I retrieve the list product with accept parameter "<accept_parameter>" response
    Then the product is returned in the list

    Examples:

    | product_name    | accept_parameter  |
    | testing_list07  | application/json  |
    | testing_list08  | application/xml   |

  @auth
  Scenario Outline: Retrieve list products with incorrect token

    Given a created product with all data and name "<product_name>"
    And incorrect "<token>" authentication
    When I retrieve the list product with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name   | accept_parameter  | Error_code  | token                            |
    | testing_list11 | application/json  | 401         | hello world                      |
    | testing_list12 | application/json  | 401         | 891855f21b2f1567afb966d3ceee1295 |
    | testing_list13 | application/json  | 401         |                                  |
