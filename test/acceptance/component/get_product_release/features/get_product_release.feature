# -*- coding: utf-8 -*-
Feature: Get a product release from the catalogue
    As a fi-ware user
    I want to be able to get the product release information
    so that I can install it

    @happy_path
    Scenario Outline: Get product release from product
      Given a created product with name "<product_name>" and release "<product_release>"
      When I retrieve the product release "<product_release>" information assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then the product release information is received

      Examples:

      | product_name      | product_release | accept_parameter  |
      | testing_release01 | 1.0.0           | application/json  |
      | testing_release02 | 1.0.0           | application/xml   |

    @skip @CLAUDIA-3754
    Scenario Outline: Get product release from non existent product
      Given a created product with name "<product_name>" and release "<product_release>"
      When I retrieve the product release "<another_product_release>" information assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | product_release | accept_parameter  | another_product_release | Error_code  |
      | testing_release01 | 1.0.0           | application/json  | 1.0.1                   | 404         |
      | testing_release02 | 1.0.0           | application/xml   | 1.0                     | 404         |


    @skip @CLAUDIA-3733
    Scenario Outline: Get a non existent product release in the catalogue
      Given a created product with name "<product_name>" and release "<product_release>"
      When I retrieve the product release "<product_release>" information assigned to the "<another_product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | product_release | accept_parameter  | another_product_name  | Error_code  |
      | testing_release01 | 1.0.0           | application/json  | not_exist             | 404         |
      | testing_release02 | 1.0.0           | application/xml   | follow_me             | 404         |


    @auth
    Scenario Outline: Get a product release with incorrect token
      Given a created product with name "<product_name>" and release "<product_release>"
      And incorrect "<token>" authentication
      When I retrieve the product release "<product_release>" information assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name     | accept_parameter  | Error_code  | token                            | product_release |
      | testing_delete01 | application/json  | 401         | hello world                      | 1.0.0           |
      | testing_delete02 | application/json  | 401         | 891855f21b2f1567afb966d3ceee1295 | 1.0.0           |
      | testing_delete03 | application/json  | 401         |                                  | 1.0.0           |

    @skip @CLAUDIA-4115
    Scenario Outline: Cause a Not acceptable error when I get product release with Content Type header invalid
      Given a created product with name "<product_name>" and release "<product_release>"
      And incorrect "<content_type>" header
      When I retrieve the product release "<product_release>" information assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

    Examples:

    | product_name      | accept_parameter  | Error_code  | content_type        | product_release |
    | testing_delete01  | application/json  | 415         | application/json1   | 1.0.0           |
    | testing_delete05  | application/json  | 415         | application/testing | 1.0.0           |


     Scenario Outline: Get product release with incorrect accept parameter
      Given a created product with name "<product_name>" and release "<product_release>"
      When I retrieve the product release "<product_release>" information assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | product_release | accept_parameter    | Error_code  |
      | testing_release01 | 1.0.0           | application/json1   | 406         |
      | testing_release02 | 1.0.0           | application/sdfdfsd | 406         |


