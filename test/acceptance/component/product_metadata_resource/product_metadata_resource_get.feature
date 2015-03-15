# -*- coding: utf-8 -*-
Feature: Get a specific metadata of a product
    As a fi-ware user
    I want to be able to get metadatas by the key
    so that I manage SDC product metadatas with its own API resource


  @happy_path
  Scenario: Get metadata. Product with one metadata.
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_01" and those metadatas
    When  I request the metadata "mobile" of the product "testing_metadatas_01"
    Then  the metadata is retrieved


  @happy_path
  Scenario: Get metadata. Product created without metadatas (default should be added)
    Given a created product with name "testing_metadatas_01b"
    When  I request the metadata "open_ports" of the product "testing_metadatas_01b"
    Then  the metadata is retrieved


  Scenario: Get metadata. Product with releases one metadata.
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_01", release "0.0.1" and those metadatas
    When  I request the metadata "mobile" of the product "testing_metadatas_01"
    Then  the metadata is retrieved


  Scenario: Get metadatas. Several metadatas (1/2).
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
          | mobile2     | testing   | testing description |
    And   a created product with name "testing_metadatas_02" and those metadatas
    When  I request the metadata "mobile" of the product "testing_metadatas_02"
    Then  the metadata is retrieved


  Scenario: Get metadatas. Several metadatas (2/2).
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
          | mobile2     | testing   | testing description |
    And   a created product with name "testing_metadatas_03" and those metadatas
    When  I request the metadata "mobile2" of the product "testing_metadatas_03"
    Then  the metadata is retrieved


  Scenario: Get metadata from a not existing product
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_04" and those metadatas
    When  I request the metadata "mobile" of the product "testing_metadatas_not_existing"
    Then  I obtain an http error code "404"


  Scenario: Get non-existent metadata
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_05" and those metadatas
    When  I request the metadata "not_existing" of the product "testing_metadatas_05"
    Then  I obtain an http error code "404"


  Scenario: Get metadatas of a product with attributes
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   the following attributes
          | key         | value     | description         | type  |
          | mobile      | postgres  | Username application| Plain |
    And   a created product with name "testing_metadatas_06" and those metadatas
    When  I request the metadata "mobile" of the product "testing_metadatas_06"
    Then  the metadata is retrieved


  Scenario Outline: Get default metadata with different value
    Given the following metadatas
          | key         | value   | description         |
          | <key>       | <value> | None                |
    And   a created product with name "<product_name>" and those metadatas
    When  I request the metadata "<key>" of the product "<product_name>"
    Then  the metadata is retrieved

    Examples:
          | product_name          | key           | value                                        |
          | testing_metadatas_07 | image         | vm_1                                         |
          | testing_metadatas_08 | image         |                                              |
          | testing_metadatas_09 | cookbook_url  | http://www.test.org                          |
          | testing_metadatas_10 | cookbook_url  |                                              |
          | testing_metadatas_11 | cloud         | yes                                          |
          | testing_metadatas_12 | cloud         | no                                           |
          | testing_metadatas_13 | installator   | puppet                                       |
          | testing_metadatas_14 | installator   | chef                                         |
          | testing_metadatas_15 | open_ports    | 80 22 8080 8091                              |
          | testing_metadatas_16 | open_ports    | 80                                           |
          | testing_metadatas_17 | open_ports    |                                              |
          | testing_metadatas_18 | public        | yes                                          |
          | testing_metadatas_19 | public        | no                                           |
          | testing_metadatas_22 | dependencies  |                                              |
          | testing_metadatas_23 | tenant_id     | 00000000000000000000000000055                |
          | testing_metadatas_24 | tenant_id     | 00000000000000000000000000055 0000005548     |
          | testing_metadatas_25 | tenant_id     |                                              |


  Scenario Outline: Get metadata with valid key values
    Given the following metadatas
          | key         | value   | description         |
          | <key>       | <value> | None                |
    And   a created product with name "<product_name>" and those metadatas
    When  I request the metadata "<key>" of the product "<product_name>"
    Then  the metadata is retrieved

    Examples:
          | product_name          | key      | value  |
          | testing_metadatas_26 | my_key   | vm_1   |
          | testing_metadatas_27 | my-key   | vm_1   |
          | testing_metadatas_29 | my:key   | vm_1   |
          | testing_metadatas_30 | my@key   | vm_1   |
          | testing_metadatas_31 | my(key)  | vm_1   |

  @test
  Scenario: Get metadata with different representations.
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   accept header value "<accept_header>"
    And   a created product with name "testing_metadatas_01" and those metadatas
    When  I request the metadata "mobile" of the product "testing_metadatas_01"
    Then  the metadata is retrieved

    Examples:
          | product_name          | accept_header    |
          | testing_metadatas_32  | application/json |
          | testing_metadatas_33  | application/xml  |
