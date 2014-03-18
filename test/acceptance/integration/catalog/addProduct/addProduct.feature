# -*- coding: utf-8 -*-
Feature: Add a new product in the catalogue
    As a fi-ware user
    I want to be able to add a new product in the catalogue
    so that they become more functional and useful

    @happy_path
    Scenario: add a new product in the catalogue
        Given the sdc is up and properly configured
        When I add a new product "Product_test_0001" with "only name" in the catalog
        Then I receive an "OK" response with a "add Product only name" with one item

    @happy_path_2
    Scenario Outline: add a new product  with attributes and all metadatas in the catalogue
        Given the sdc is up and properly configured
        When I add a new product "Product_test_0001" with "<label>" in the catalog
        Then I receive an "OK" response with a "<response>" with one item
    Examples:
      | label |response|
      |attributes|add Product only attributes|
      |attributes_and_all_metadatas|add Product attr and metadatas|


    @skip @CLAUDIA-3742
    Scenario: try to add a new product in the catalogue without name label (mandatory)
        Given the sdc is up and properly configured
        When I add a new product "Product_test_0001" with "Without Name Label" in the catalog
        Then I receive an "Not Found" response with an "exception" error messages

    @name_errors @CLAUDIA-3741  @CLAUDIA-3740
    Scenario Outline: try to add a new product with name errors into of the catalog
        Given the sdc is up and properly configured
        When I add a new product "<name>" with "<error>" in the catalog
        Then  I receive an "Not Found" response with an "exception" error messages
    Examples:
      | error | name |
      | length major than 256 characters |12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567|
      |  empty name | |

    @metadatas
    Scenario Outline:  add a new product with metadatas
          Given the sdc is up and properly configured
          When With metadatas I add a new product "Product_test_0001" with "<name>" and "<value>" in the catalog
          Then I receive an "OK" response with a "<response>" with one item
    Examples:
        | name | value | response |
        |metadata_image|df44f62d-9d66-4dc5-b084-2d6c7bc4cfe4|metadata image - one value|
        |metadata_image|df44f62d-9d66-4dc5-b084-2d6c7bc4cfe4 df44f62d-9d66-1111-2222-2d6c7bc4cfe4|metadata image - several values|
        |metadata_cloud|Yes|metadata_cloud - yes|
        |metadata_cloud|No|metadata_cloud - no|
        |metadata_installator|chef|metadata_installator - chef|
        |metadata_installator|puppet|metadata_installator - puppet|
        |metadata_open_ports|22|metadata open_ports - one value|
        |metadata_open_ports|22 80 11156|metadata open_ports - several values|
        |metadata_repository|svn|metadata repository - one value|
        |metadata_public|Yes|metadata_public - yes|
        |metadata_public|No|metadata_public - no|
        |metadata_dependencies|Tomcat|metadata dependencies - one value|
        |metadata_dependencies|Tomcat mysql nodejs|metadata dependencies - several values|


    @metadatas_errors
    Scenario Outline:  try to add a new product with wrong metadatas
          Given the sdc is up and properly configured
          When With metadatas I add a new product "Product_test_0001" with "<name>" and "<value>" in the catalog
          Then I receive an "Not Found" response with an "exception" error messages
    Examples:
        | name | value |
        |metadata_cloud|ty|
        |metadata_cloud||
        |metadata_installator|pepe|
        |metadata_open_ports|11111111|
        |metadata_open_ports|retretret|
        |metadata_public|hyu|
        |metadata_public||
        |metadata_dependencies|gdfgdfgdfg|



