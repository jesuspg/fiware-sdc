# -*- coding: utf-8 -*-
Feature: Update a specific metadata of a product
    As a fi-ware user
    I want to be able to update metadatas by the key
    so that I manage SDC product metadatas with its own API resource


  @happy_path
  Scenario: Update metadata value and description. Product with releases and one metadata.
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_00" and those metadatas
    When  I update the metadata "mobile" of the product "testing_metadatas_00"
          | key         | value     | description         |
          | mobile      | new value | new description     |
    Then  the metadata is updated
    And   the other metadatas are still present in the product


  Scenario Outline: Update metadata. Product with a custom metadata.
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "<product_name>" and those metadatas
    When  I update the metadata "mobile" of the product "<product_name>"
          | key   | value   | description   |
          | <key> | <value> | <description> |
    Then  the metadata is updated
    And   the other metadatas are still present in the product

    Examples:
          | product_name          | key             | value           | description         |
          | testing_metadatas_01a | [MISSING_PARAM] | new_value       | [MISSING_PARAM]     |
          | testing_metadatas_01b | [MISSING_PARAM] | [MISSING_PARAM] | new_desc            |
          | testing_metadatas_01c | [MISSING_PARAM] | new_value       | new_desc            |
          | testing_metadatas_01d | mobile          | new_value       | [MISSING_PARAM]     |
          | testing_metadatas_01e | mobile          | [MISSING_PARAM] | new_desc            |
          | testing_metadatas_01f | mobile          | new_value       | new_desc            |
          | testing_metadatas_01g | [MISSING_PARAM] | iOS             | [MISSING_PARAM]     |
          | testing_metadatas_01h | [MISSING_PARAM] | [MISSING_PARAM] | installation type   |
          | testing_metadatas_01i | [MISSING_PARAM] | iOS             | installation type   |
          | testing_metadatas_01j | mobile          | iOS             | [MISSING_PARAM]     |
          | testing_metadatas_01k | mobile          | [MISSING_PARAM] | installation type   |
          | testing_metadatas_01l | mobile          | iOS             | installation type   |


  Scenario Outline: Update metadata key with different key value
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "<product_name>" and those metadatas
    When  I update the metadata "mobile" of the product "<product_name>"
          | key   | value   | description   |
          | <key> | <value> | <description> |
    Then  I obtain an http error code "400"

    Examples:
          | product_name          | key        | value            | description       |
          | testing_metadatas_02a | mobile_new | new_value        | [MISSING_PARAM]   |
          | testing_metadatas_02b | mobile_new | [MISSING_PARAM]  | new_desc          |
          | testing_metadatas_02c | mobile_new | new_value        | new_desc          |


  Scenario: Update metadata with empty value and description. Product with releases and one metadata.
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_03" and those metadatas
    When  I update the metadata "mobile" of the product "testing_metadatas_03"
          | key         | value     | description         |
          | mobile      |           |                     |
    Then  the metadata is updated
    And   the other metadatas are still present in the product


  Scenario: Update a default metadata.
    Given a created product with name "testing_metadatas_04"
    When  I update the metadata "open_ports" of the product "testing_metadatas_04"
          | key         | value     |
          | open_ports  | 123 2222  |
    Then  the metadata is updated
    And   the other metadatas are still present in the product


  Scenario: Update metadata value. Product with releases and one metadata.
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_05", release "0.0.1" and those metadatas
    When  I update the metadata "mobile" of the product "testing_metadatas_05"
          | key         | value     |
          | mobile      | value02   |
    Then  the metadata is updated
    And   the other metadatas are still present in the product



  Scenario: Update metadata. Product with several metadatas (1/2).
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
          | mobile2     | testing   | testing description |
    And   a created product with name "testing_metadatas_06" and those metadatas
    When  I update the metadata "mobile" of the product "testing_metadatas_06"
          | key         | value     | description         |
          | mobile      | new value | new description     |
    Then  the metadata is updated
    And   the other metadatas are still present in the product


  Scenario: Update metadatas. Product with several metadatas (2/2).
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
          | mobile2     | testing   | testing description |
    And   a created product with name "testing_metadatas_07" and those metadatas
    When  I update the metadata "mobile2" of the product "testing_metadatas_07"
          | key         | value     | description         |
          | mobile2     | new value | new description     |
    Then  the metadata is updated
    And   the other metadatas are still present in the product


  Scenario: Update metadata of a not existing product
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_08" and those metadatas
    When  I update the metadata "mobile" of the product "not_existing"
          | key         | value     | description         |
          | mobile      | new value | new description     |
    Then  I obtain an http error code "404"


  Scenario: Update non-existent metadata
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_09" and those metadatas
    When  I update the metadata "not_existing" of the product "testing_metadatas_09"
          | key         | value     | description         |
          | mobile      | new value | new description     |
    Then  I obtain an http error code "404"


  Scenario: Update metadata of a product with attributes
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   the following attributes
          | key         | value     | description         | type  |
          | mobile      | postgres  | Username application| Plain |
    And   a created product with name "testing_metadatas_10" and those metadatas
    When  I update the metadata "mobile" of the product "testing_metadatas_10"
          | key         | value     | description         |
          | mobile      | new value | new description     |
    Then  the metadata is updated
    And   the other metadatas are still present in the product


  Scenario: Update metadata value and description with different representations.
    Given accept header value "<accept_header>"
    And   a created product with name "<product_name>" and those metadatas
    When  I update the metadata "image" of the product "<product_name>"
          | key         | value               | description         |
          | image       | 1asfsdf-sdfsdf-sdfs | new description     |
    Then  the metadata is updated
    And   the other metadatas are still present in the product

    Examples:
          | product_name          | accept_header    |
          | testing_metadatas_11  | application/json |
          | testing_metadatas_12  | application/xml  |
