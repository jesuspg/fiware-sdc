# -*- coding: utf-8 -*-
Feature: Get all product and release information using the correct SDC resource for that
  As a fi-ware user
  I want to be able to obtain a 'product and release' list for all products managed by SDC
  so that I can know about all the products with its releases

  @happy_path
  Scenario Outline: Get 'product and release' list from the catalog
    Given a created product with name "<product_name>" and release "<product_release>"
    When I retrieve the product list with its releases
    Then the list is returned
    And the product with its release is in the list

    Examples:
    | product_name          | product_release |
    | testing_prodandrel01  | 1.0.0           |
    | testing_prodandrel02  | 1.a.b           |
    | testing_prodandrel03  | 1-a             |

  @happy_path
  Scenario Outline: Get 'product and release' list from the catalog using products with default attributes
    Given default product attributes
    And a created product with name "<product_name>" and release "<product_release>"
    When I retrieve the product list with its releases
    Then the list is returned
    And the product with its release is in the list

    Examples:
    | product_name          | product_release |
    | testing_prodandrel01  | 1.0.0           |
    | testing_prodandrel02  | 1.a.b           |
    | testing_prodandrel03  | 1-a             |

  Scenario: Get 'product and release' list from the catalog with products without releases
    Given a created product with this name "testing_prodandrel01"
    When I retrieve the product list with its releases
    Then the list is returned
    And the product is not in the list

  Scenario: Get 'product and release' list from the catalog with products with more than one release
    Given a created product with name "testing_prodandrel01" and releases:
    | release |
    | 1.0.0   |
    | 1.0.1   |
    | 1.1     |
    | 1-a.b   |
    When I retrieve the product list with its releases
    Then the list is returned
    And the product with all its releases is in the list

  Scenario: Get 'product and release' list from the catalog using different representations
    Given a created product with name "testing_prodandrel01" and release "1.0.0"
    And accept header value "<accept_header>"
    When I retrieve the product list with its releases
    Then the list is returned
    And the product with its release is in the list

    Examples:
    | accept_header     |
    | application/xml   |
    | application/json  |

  Scenario: Get 'product and release' list from the catalog using wrong representations
    Given a created product with name "testing_prodandrel01" and release "1.0.0"
    And accept header value "<accept_header>"
    When I retrieve the product list with its releases
    Then I obtain an http error code "<error_code>"

    Examples:
    | accept_header     | error_code  |
    | application/xml1  | 406         |
    | application/json1 | 406         |
    | application/test  | 406         |

  Scenario: Unsupported HTTP methods for 'product and release' list request
    Given a created product with name "testing_prodandrel01" and release "1.0.0"
    When I use a invalid HTTP "<http_method>" method
    Then I obtain an http error code "<error_code>"

    Examples:
    | http_method | error_code  |
    | post        | 405         |
    | put         | 405         |
    | delete      | 405         |

    @auth
    Scenario Outline: Get 'product and release' list with incorrect authentication: token

      Given a created product with name "testing_delete01" and release "1.2.3"
      And the authentication token "<token>":
      When I retrieve the product list with its releases
      Then I obtain an http error code "401"

      Examples:

      | token                            |
      | hello world                      |
      | 891855f21b2f1567afb966d3ceee1295 |
      |                                  |

    @auth
    Scenario Outline: Get 'product and release' list with incorrect authentication: tenant-id

      Given a created product with name "testing_delete01" and release "1.2.3"
      And the authentication tenant-id "<tenant_id>"
      When I retrieve the product list with its releases
      Then I obtain an http error code "401"

      Examples:

      | tenant_id                        |
      | hello world                      |
      | 00001231234112                   |
      |                                  |
