# -*- coding: utf-8 -*-
Feature: Add a new product release in the catalogue
    As a fi-ware user
    I want to be able to add a new product release in the catalogue
    so that I can create version to products


    @happy_path
    Scenario Outline: Add a new product release in the catalogue
      Given a created product with name "<product_name>"
      When I create the product release "<product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then the product release is created

      Examples:

      | product_name      | product_release | accept_parameter  |
      | testing_release01 | 1.0.0           | application/json  |
      | testing_release02 | 1.0.0           | application/xml   |
      | testing_release03 | 0.0.0           | application/json  |
      | testing_release04 | testing         | application/json  |
      | testing_release05 | 1.1             | application/json  |
      | testing_release06 | 1.a             | application/json  |
      | testing_release07 | ABCND           | application/json  |
      #| testing_release08 |                 | application/json  |
      | testing_release09 | 00000           | application/json  |

    Scenario Outline: Add a new product release in the catalogue from product with only attributes
      Given a created product with attributes and name "<product_name>"
      When I create the product release "<product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then the product release is created

      Examples:

      | product_name      | product_release | accept_parameter  |
      | testing_release01 | 1.0.0           | application/json  |
      | testing_release02 | 1.0.0           | application/xml   |


    Scenario Outline: Add a new product release in the catalogue from product with all data
      Given a created product with all data and name "<product_name>"
      When I create the product release "<product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then the product release is created

      Examples:

      | product_name      | product_release | accept_parameter  |
      | testing_release01 | 1.0.0           | application/json  |
      | testing_release02 | 1.0.0           | application/xml   |


    Scenario Outline: add a new release with non-existent product in the catalogue
      Given a created product with all data and name "<product_name>"
      When I create the product release "<product_release>" assigned to the "<another_product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | product_release | accept_parameter  | another_product_name  | Error_code  |
      | testing_release01 | 1.0.0           | application/json  | not_exist             | 404         |
      | testing_release02 | 1.0.0           | application/xml   | follow_me             | 404         |


    Scenario Outline: Add a new product release in the catalogue with incorrect parameters
      Given a created product with all data and name "<product_name>"
      When I create the product release "<product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | product_release | accept_parameter  | Error_code  |
      | testing_release01 | LONG_ID         | application/json  | 400         |
      | testing_release02 | LONG_ID         | application/xml   | 400         |
      | testing_release03 |                 | application/xml   | 400         |


    Scenario Outline: Create a new release with version already registered for that product
      Given a created product with all data and name "<product_name>"
      And a created product release "<product_release>" assigned to the "<product_name>")
      When I create the product release "<another_product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<error_code>"

      Examples:

      | product_name      | product_release | another_product_release  | accept_parameter  | error_code  |
      | testing_release01 | 1.0.0           | 1.0.0                    | application/json  | 409         |
      | testing_release02 | 2.0.0           | 2.0.0                    | application/xml   | 409         |

    @auth
    Scenario Outline: Add a new product release in the catalogue a product with incorrect token

      Given a created product with all data and name "<product_name>"
      And incorrect "<token>" authentication
      When I create the product release "<product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name     | accept_parameter  | Error_code  | token                            | product_release |
      | testing_delete01 | application/json  | 401         | hello world                      | 1.0.0           |
      | testing_delete02 | application/json  | 401         | 891855f21b2f1567afb966d3ceee1295 | 1.0.0           |
      | testing_delete03 | application/json  | 401         |                                  | 1.0.0           |

    Scenario Outline: Cause a Not acceptable error when I add a new product release in the catalogue with Content Type header invalid

      Given a created product with all data and name "<product_name>"
      And incorrect "<content_type>" header
      When I create the product release "<product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

    Examples:

    | product_name      | accept_parameter  | Error_code  | content_type        | product_release |
    | testing_delete01  | application/json  | 415         | application/json1   | 1.0.0           |
    | testing_delete05  | application/json  | 415         | application/testing | 1.0.0           |


     Scenario Outline: Add a new product release in the catalogue from product with all data with accept parameter incorrect
      Given a created product with all data and name "<product_name>"
      When I create the product release "<product_release>" assigned to the "<product_name>" with accept parameter "<accept_parameter>" response
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | product_release | accept_parameter    | Error_code  |
      | testing_release01 | 1.0.0           | application/json1   | 406         |
      | testing_release02 | 1.0.0           | application/sdfdfsd | 406         |
