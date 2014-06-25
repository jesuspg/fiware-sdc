# -*- coding: utf-8 -*-
Feature: Delete a product in the catalogue
    As a fi-ware user
    I want to be able to delete a new products in the catalogue
    so that I can eliminate not useful products

  @happy_path
  Scenario Outline: Delete a product created before

    Given a created product with name "<product_name>"
    When I delete the product "<product_name>" with accept parameter "<accept_parameter>" response
    Then the product is deleted

    Examples:

    | product_name      | accept_parameter  |
    | testing_delete01  | application/json  |
    | testing_delete02  | application/xml   |

  Scenario Outline: Delete a product with attributes

    Given a created product with attributes and name "<product_name>"
    When I delete the product "<product_name>" with accept parameter "<accept_parameter>" response    T
    Then the product is deleted

    Examples:

    | product_name      | accept_parameter  |
    | testing_delete01  | application/json  |
    | testing_delete02  | application/xml   |

  Scenario Outline: Delete a product with metadata

    Given a created product with metadatas and name "<product_name>"
    When I delete the product "<product_name>" with accept parameter "<accept_parameter>" response
    Then the product is deleted

    Examples:

    | product_name      | accept_parameter  |
    | testing_delete01  | application/json  |
    | testing_delete02  | application/xml   |

  Scenario Outline: Delete a product with all data

    Given a created product with all data and name "<product_name>"
    When I delete the product "<product_name>" with accept parameter "<accept_parameter>" response
    Then the product is deleted

    Examples:

    | product_name      | accept_parameter  |
    | testing_delete01  | application/json  |
    | testing_delete02  | application/xml   |

  @CLAUDIA-3733
  Scenario Outline: Delete a not existent product

    Given a created product with all data and name "<product_name>"
    When I delete the product "<another_product_name>" with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name      | accept_parameter  | another_product_name  | Error_code  |
    | testing_delete01  | application/json  | testing_delete        | 404         |
    | testing_delete02  | application/xml   | testing_delete02      | 404         |


  Scenario Outline: Cause a Not acceptable error when I delete a product with Content Type header invalid

    Given a created product with all data and name "<product_name>"
    And incorrect "<content_type>" header
    When I delete the product "<product_name>" with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name      | accept_parameter  | Error_code  | content_type        |
    | testing_delete01  | application/json  | 415         | application/json1   |
    | testing_delete05  | application/json  | 415         | application/testing |


  Scenario Outline: Delete a product with incorrect token

    Given a created product with all data and name "<product_name>"
    And incorrect "<token>" authentication
    When I delete the product "<product_name>" with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name     | accept_parameter  | Error_code  | token                            |
    | testing_delete01 | application/json  | 401         | hello world                      |
    | testing_delete02 | application/json  | 401         | 891855f21b2f1567afb966d3ceee1295 |
    | testing_delete03 | application/json  | 401         |                                  |

    #TODO DELETE PRODUCT WITH RELEASE ASSIGNED

