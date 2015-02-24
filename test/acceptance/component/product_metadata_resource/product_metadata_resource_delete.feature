# -*- coding: utf-8 -*-
Feature: Delete a specific metadata of a product
    As a fi-ware user
    I want to be able to delete metadatas by the key
    so that I manage SDC product metadatas with its own API resource


  @happy_path
  Scenario: Delete metadata. Product with one metadata.
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_01" and those metadatas
    When  I delete the metadata "mobile" of the product "testing_metadatas_01"
    Then  the metadata is deleted
    And   the other metadatas are still present in the product


  Scenario: Delete default metadata
    Given a created product with name "testing_metadatas_01b"
    When  I delete the metadata "open_ports" of the product "testing_metadatas_01b"
    Then  the metadata is deleted
    And   the other metadatas are still present in the product


  Scenario: Delete metadata. Product with releases and one metadata.
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_02", release "0.0.1" and those metadatas
    When  I delete the metadata "mobile" of the product "testing_metadatas_02"
    Then  the metadata is deleted
    And   the other metadatas are still present in the product


  Scenario: Delete metadata. Several metadatas (1/2).
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
          | mobile2     | testing   | testing description |
    And   a created product with name "testing_metadatas_03" and those metadatas
    When  I delete the metadata "mobile" of the product "testing_metadatas_03"
    Then  the metadata is deleted
    And   the other metadatas are still present in the product


  Scenario: Delete metadata. Several metadatas (2/2).
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
          | mobile2     | testing   | testing description |
    And   a created product with name "testing_metadatas_04" and those metadatas
    When  I delete the metadata "mobile2" of the product "testing_metadatas_04"
    Then  the metadata is deleted
    And   the other metadatas are still present in the product


  Scenario: Delete metadatas of a product with attributes
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   the following attributes
          | key         | value     | description         | type  |
          | mobile      | postgres  | Username application| Plain |
    And   a created product with name "testing_metadatas_07" and those metadatas
    When  I delete the metadata "mobile" of the product "testing_metadatas_07"
    Then  the metadata is deleted
    And   the other metadatas are still present in the product


  Scenario: Delete metadata of a not existing product
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_05" and those metadatas
    When  I delete the metadata "mobile" of the product "testing_metadatas_not_existing"
    Then  I obtain an http error code "404"


  Scenario: Delete non-existent metadata
    Given the following metadatas
          | key         | value     | description         |
          | mobile      | iOS       | installation type   |
    And   a created product with name "testing_metadatas_06" and those metadatas
    When  I delete the metadata "not_existing" of the product "testing_metadatas_06"
    Then  I obtain an http error code "404"

