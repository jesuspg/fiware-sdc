# -*- coding: utf-8 -*-
Feature: Get product attributes
    As a fi-ware user
    I want to be able to retrieve the attributes about product
    so that I can use it.

  @happy_path
  Scenario Outline: Retrieve a product metadatas without attributes

    Given a created product with name "<product_name>"
    When I retrieve the product metadatas "<product_name>" with accept parameter "<accept_parameter>" response
    Then the metadatas product contain default metadatas

    Examples:

    | product_name           | accept_parameter  |
    | testing_information01  | application/json  |
    | testing_information02  | application/xml   |

  Scenario Outline: Retrieve a product metadatas with attributes

    Given a created product with attributes and name "<product_name>"
    When I retrieve the product metadatas "<product_name>" with accept parameter "<accept_parameter>" response
    Then the metadatas product contain default metadatas

  Examples:

    | product_name           | accept_parameter  |
    | testing_information01  | application/json  |
    | testing_information02  | application/xml   |

  Scenario Outline: Retrieve a product metadatas attributes with only metadata

    Given a created product with metadatas and name "<product_name>"
    When I retrieve the product metadatas "<product_name>" with accept parameter "<accept_parameter>" response
    Then the metadatas product are retrieved


    Examples:

    | product_name           | accept_parameter  |
    | testing_information01  | application/json  |
    | testing_information02  | application/xml   |

  Scenario Outline: Retrieve a product metadatas with all data

    Given a created product with all data and name "<product_name>"
    When I retrieve the product metadatas "<product_name>" with accept parameter "<accept_parameter>" response
    Then the metadatas product are retrieved

    Examples:

    | product_name           | accept_parameter  |
    | testing_information01  | application/json  |
    | testing_information02  | application/xml   |


  Scenario Outline: Retrieve metadatas from a not existent product

    Given a created product with all data and name "<product_name>"
    When I retrieve the product metadatas "<another_product_name>" with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name           | accept_parameter  | another_product_name       | Error_code  |
    | testing_information01  | application/json  | testing_information        | 404         |
    | testing_information02  | application/xml   | Testing_information02      | 404         |

  @skip @CLAUDIA-4115
  Scenario Outline: Cause a Not acceptable error when I Retrieve a product metadatas with Content Type header invalid

    Given a created product with all data and name "<product_name>"
    And incorrect "<content_type>" header
    When I retrieve the product metadatas "<product_name>" with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name           | accept_parameter  | Error_code  | content_type        |
    | testing_information01  | application/json  | 415         | application/json1   |
    | testing_information05  | application/json  | 415         | application/testing |

  @auth
  Scenario Outline: Retrieve a product metadatas with incorrect token

    Given a created product with all data and name "<product_name>"
    And incorrect "<token>" authentication
    When I retrieve the product metadatas "<product_name>" with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name          | accept_parameter  | Error_code  | token                            |
    | testing_information01 | application/json  | 401         | hello world                      |
    | testing_information02 | application/json  | 401         | 891855f21b2f1567afb966d3ceee1295 |
    | testing_information03 | application/json  | 401         |                                  |
