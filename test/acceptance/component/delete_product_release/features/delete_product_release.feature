# -*- coding: utf-8 -*-
Feature: Delete a product release in the catalogue
    As a fi-ware user
    I want to be able to delete a product release in the catalogue
    so that I can clean up my system

    Scenario Outline: Delete a new product release in the catalogue
      Given a created product with name "<product_name>" and release "<product_release>"
      When I delete the product release "<product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then the product release is deleted

      Examples:

      | product_name      | product_release | accept_parameter  |
      | testing_release01 | 1.0.0           | application/json  |
      | testing_release02 | 1.0.0           | application/xml   |


    Scenario Outline: Delete a non existent product release in the catalogue
      Given a created product with name "<product_name>" and release "<product_release>"
      When I delete the product release "<another_product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | product_release | accept_parameter  | another_product_release | Error_code  |
      | testing_release01 | 1.0.0           | application/json  | 1.0.1                   | 404         |
      | testing_release02 | 1.0.0           | application/xml   | 1.0                     | 404         |


    @skip @CLAUDIA-3754
    Scenario Outline: Delete a non existent product release in the catalogue
      Given a created product with name "<product_name>" and release "<product_release>"
      When I delete the product release "<product_release>" assigned to the "<another_product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | product_release | accept_parameter  | another_product_name  | Error_code  |
      | testing_release01 | 1.0.0           | application/json  | not_exist             | 404         |
      | testing_release02 | 1.0.0           | application/xml   | follow_me             | 404         |



    Scenario Outline: Delete a product release with incorrect token

      Given a created product with name "<product_name>" and release "<product_release>"
      And incorrect "<token>" authentication
      When I delete the product release "<product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name     | accept_parameter  | Error_code  | token                            | product_release |
      | testing_delete01 | application/json  | 401         | hello world                      | 1.0.0           |
      | testing_delete02 | application/json  | 401         | 891855f21b2f1567afb966d3ceee1295 | 1.0.0           |
      | testing_delete03 | application/json  | 401         |                                  | 1.0.0           |

    Scenario Outline: Cause a Not acceptable error when I delete release with Content Type header invalid

      Given a created product with name "<product_name>" and release "<product_release>"
      And incorrect "<content_type>" header
      When I delete the product release "<product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

    Examples:

    | product_name      | accept_parameter  | Error_code  | content_type        | product_release |
    | testing_delete01  | application/json  | 415         | application/json1   | 1.0.0           |
    | testing_delete05  | application/json  | 415         | application/testing | 1.0.0           |


     Scenario Outline: Add a new product release in the catalogue from product with all data
      Given a created product with name "<product_name>" and release "<product_release>"
      When I delete the product release "<product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | product_release | accept_parameter    | Error_code  |
      | testing_release01 | 1.0.0           | application/json1   | 406         |
      | testing_release02 | 1.0.0           | application/sdfdfsd | 406         |


