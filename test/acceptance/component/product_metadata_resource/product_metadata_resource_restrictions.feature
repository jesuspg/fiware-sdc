# -*- coding: utf-8 -*-
Feature: API implements restrictions, security and correct errors messages in its operations
    As a fi-ware user
    I want to be able to receive error messages when I use incorrectly the API
    so that I can use safely this API resource


  Scenario: Get metadata using valid representations
    Given a created product with name "testing_metadatas_32"
    And   accept header value "<accept_header>"
    When  I request the metadata "open_ports" of the product "testing_metadatas_32"
    Then  the metadata is retrieved

    Examples:
          | accept_header     |
          | application/xml   |
          | application/json  |


  Scenario: Get metadata using wrong representations
    Given a created product with name "testing_metadatas_33"
    And   accept header value "<accept_header>"
    When  I request the metadata "open_ports" of the product "testing_metadatas_33"
    Then  I obtain an http error code "<error_code>"

    Examples:
          | accept_header     | error_code  |
          | application/xml1  | 406         |
          | application/json1 | 406         |
          | application/test  | 406         |


  Scenario Outline: Unsupported HTTP methods
    Given a created product with name "testing_metadatas_34"
    When  I request the metadata "open_ports" of the product "testing_metadatas_34" using a invalid HTTP "<http_method>" method
    Then  I obtain an http error code "<error_code>"

    Examples:
          | http_method | error_code  |
          | post        | 405         |


  @auth
  Scenario Outline: Get metadata with incorrect authentication: token
    Given a created product with name "testing_metadatas_35"
    And   the authentication token "<token>":
    When  I request the metadata "open_ports" of the product "testing_metadatas_35"
    Then  I obtain an http error code "401"

    Examples:
          | token                            |
          | hello world                      |
          | 891855f21b2f1567afb966d3ceee1295 |
          |                                  |


  @auth
  Scenario Outline: Get metadata with incorrect authentication: tenant-id

    Given a created product with name "testing_metadatas_36"
    And   the authentication tenant-id "<tenant_id>"
    When  I request the metadata "open_ports" of the product "testing_metadatas_36"
    Then  I obtain an http error code "401"

    Examples:
        | tenant_id                        |
        | hello world                      |
        | 00001231234112                   |
        |                                  |
