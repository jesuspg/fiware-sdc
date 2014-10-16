# -*- coding: utf-8 -*-
Feature: Get product release list from the catalogue
    As a fi-ware user
    I want to be able to obtain a product release list from a product
    so that I can know about all releases of my product

    Scenario Outline: Retrieve a product release list from product in the catalog with only one release

      Given a created product with name "<product_name>" and release "<product_release>"
      When I retrieve the product release "<product_release>" list assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then the product release list is received

      Examples:

      | product_name      | product_release | accept_parameter  |
      | testing_release01 | 1.0.0           | application/json  |
      | testing_release02 | 1.0.0           | application/xml   |


    Scenario Outline: Retrieve a product release list from product in the catalog without releases

      Given a created non released product with name "<product_name>"
      When I retrieve the product release "<product_release>" list assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then no product releases are received

      Examples:

      | product_name      | product_release | accept_parameter  |
      | testing_release01 | 1.0.0           | application/json  |
      | testing_release02 | 1.0.0           | application/xml   |


    Scenario Outline: Get a product releases list from a non existent product release in the catalogue
      Given a created product with name "<product_name>" and release "<product_release>"
      When I retrieve the product release "<product_release>" list assigned to the "<another_product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | product_release | accept_parameter  | another_product_name  | Error_code  |
      | testing_release01 | 1.0.0           | application/json  | not_exist             | 404         |
      | testing_release02 | 1.0.0           | application/xml   | follow_me             | 404         |


    @auth
    Scenario Outline: Get releases list with incorrect token

      Given a created product with name "<product_name>" and release "<product_release>"
      And incorrect "<token>" authentication
      When I retrieve the product release "<product_release>" list assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name     | accept_parameter  | Error_code  | token                            | product_release |
      | testing_delete01 | application/json  | 401         | hello world                      | 1.0.0           |
      | testing_delete02 | application/json  | 401         | 891855f21b2f1567afb966d3ceee1295 | 1.0.0           |
      | testing_delete03 | application/json  | 401         |                                  | 1.0.0           |

    @skip @CLAUDIA-4115
    Scenario Outline: Cause a Not acceptable error when I get a release list with Content Type header invalid

      Given a created product with name "<product_name>" and release "<product_release>"
      And incorrect "<content_type>" header
      When I retrieve the product release "<product_release>" list assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

    Examples:

    | product_name      | accept_parameter  | Error_code  | content_type        | product_release |
    | testing_delete01  | application/json  | 415         | application/json1   | 1.0.0           |
    | testing_delete05  | application/json  | 415         | application/testing | 1.0.0           |


     Scenario Outline: Cause a error when I get a release list with Accept header invalid
      Given a created product with name "<product_name>" and release "<product_release>"
      When I retrieve the product release "<product_release>" list assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | product_release | accept_parameter    | Error_code  |
      | testing_release01 | 1.0.0           | application/json1   | 406         |
      | testing_release02 | 1.0.0           | application/sdfdfsd | 406         |


