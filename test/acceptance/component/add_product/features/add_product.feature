# -*- coding: utf-8 -*-
Feature: Add a new product in the catalogue
    As a fi-ware user
    I want to be able to add a new products in the catalogue
    so that I can build more complex systems

  @happy_path
  Scenario Outline: Add a new product with the basic parameters

    Given a product with name "<product_name>" with description "<description>"
    When I add the new product with accept parameter "<accept_parameter>" response
    Then the product is created

    Examples:

    | product_name      | description                   | accept_parameter  |
    | testing_product01 | product with testing purposes | application/json  |
    | testing_product02 | product with testing purposes | application/xml   |
    | @testing          | product with testing purposes | application/xml   |


  Scenario Outline: Add new product with one attribute

    Given a product with name "<product_name>" with description "<description>"
    And the following attributes
    | key       | value     | description         |
    | username  | postgres  | Username application|
    When I add the new product with attributes, with accept parameter "<accept_parameter>" response
    Then the product is created

    Examples:

    | product_name          | description                   | accept_parameter  |
    | testing_attributes_01 | product with testing purposes | application/json  |
    | testing_attributes_02 | product with testing purposes | application/xml   |

  Scenario Outline: Add new product with several attributes

    Given a product with name "<product_name>" with description "<description>"
    And the following attributes
    | key       | value     | description         |
    | username  | postgres  | Username application|
    | password  | pwd       | password application|
    When I add the new product with attributes, with accept parameter "<accept_parameter>" response
    Then the product is created

    Examples:

    | product_name          | description                   | accept_parameter  |
    | testing_attributes_11 | product with testing purposes | application/json  |
    | testing_attributes_12 | product with testing purposes | application/xml   |


  Scenario Outline: Add new product with one metadata

    Given a product with name "<product_name>" with description "<description>"
    And the following metadatas
    | key         | value     | description         |
    | mobile      | iOS       | installation type   |
    When I add the new product with metadatas, with accept parameter "<accept_parameter>" response
    Then the product is created

    Examples:

    | product_name          | description                   | accept_parameter  |
    | testing_attributes_23 | product with testing purposes | application/json  |
    | testing_attributes_24 | product with testing purposes | application/xml   |



  Scenario Outline: Add new product with several metadatas

    Given a product with name "<product_name>" with description "<description>"
    And the following metadatas
    | key         | value     | description         |
    | mobile      | iOS       | installation type   |
    | testing     | lettuce   | testing application |
    When I add the new product with metadatas, with accept parameter "<accept_parameter>" response
    Then the product is created

    Examples:

    | product_name           | description                   | accept_parameter  |
    | testing_attributes_123 | product with testing purposes | application/json  |
    | testing_attributes_124 | product with testing purposes | application/xml   |


  Scenario Outline: Add new product with one attribute and one metadata

    Given a product with name "<product_name>" with description "<description>"
    And the following metadatas
    | key         | value     | description         |
    | mobile      | iOS       | installation type   |
    And the following attributes
    | key       | value     | description         |
    | username  | postgres  | Username application|
    When I add the new product with all the parameters, with accept parameter "<accept_parameter>" response
    Then the product is created

    Examples:

    | product_name          | description                   | accept_parameter  |
    | testing_attributes_53 | product with testing purposes | application/json  |
    | testing_attributes_54 | product with testing purposes | application/xml   |


  Scenario Outline: Add new product with several attributes and several metadatas

    Given a product with name "<product_name>" with description "<description>"
    And the following metadatas
    | key         | value     | description         |
    | mobile      | iOS       | installation type   |
    | testing     | lettuce   | testing application |
    And the following attributes
    | key       | value     | description         |
    | username  | postgres  | Username application|
    | password  | pwd       | password application|
    When I add the new product with all the parameters, with accept parameter "<accept_parameter>" response
    Then the product is created

    Examples:

    | product_name          | description                   | accept_parameter  |
    | testing_attributes_63 | product with testing purposes | application/json  |
    | testing_attributes_64 | product with testing purposes | application/xml   |

  Scenario Outline: Add new product changing the default metadatas

    Given a product with name "<product_name>" with description "<description>"
    And the following metadatas
    | key         | value   | description         |
    | <key>       | <value> | None                |
    When I add the new product with metadatas, with accept parameter "<accept_parameter>" response
    Then the product is created

    Examples:

    | product_name           | description                   | accept_parameter  | key           | value                 |
    | testing_attributes_223 | product with testing purposes | application/json  | image         | vm_1                  |
    | testing_attributes_224 | product with testing purposes | application/xml   | cookbook_url  | http://www.test.org   |
    | testing_attributes_225 | product with testing purposes | application/json  | cloud         | yes                   |
    | testing_attributes_226 | product with testing purposes | application/json  | cloud         | no                    |
    | testing_attributes_227 | product with testing purposes | application/xml   | installator   | puppet                |
    | testing_attributes_228 | product with testing purposes | application/xml   | installator   | chef                  |
    | testing_attributes_229 | product with testing purposes | application/json  | open_ports    | 80 22 8080 8091       |
    | testing_attributes_230 | product with testing purposes | application/xml   | open_ports    | 80                    |
    | testing_attributes_231 | product with testing purposes | application/json  | public        | yes                   |
    | testing_attributes_232 | product with testing purposes | application/xml   | public        | no                    |
    | testing_attributes_233 | product with testing purposes | application/json  | dependencies  | tomcat                |
    | testing_attributes_234 | product with testing purposes | application/xml   | dependencies  | tomcat mysql nodejs   |

  @CLAUDIA-3747 @CLAUDIA-3746
  Scenario Outline: Add new product using incorrect metadatas

    Given a product with name "<product_name>" with description "<description>"
    And the following metadatas
    | key         | value   | description         |
    | <key>       | <value> | None                |
    When I add the new product with metadatas, with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name           | description                   | accept_parameter  | key           | value            | Error_code  |
    | testing_attributes_324 | product with testing purposes | application/xml   | cookbook_url  | hehehehe         | 400         |
    | testing_attributes_325 | product with testing purposes | application/json  | cloud         |                  | 400         |
    | testing_attributes_326 | product with testing purposes | application/json  | cloud         | test             | 400         |
    | testing_attributes_327 | product with testing purposes | application/xml   | installator   | test             | 400         |
    | testing_attributes_329 | product with testing purposes | application/json  | open_ports    | 111111111111111  | 400         |
    | testing_attributes_330 | product with testing purposes | application/xml   | open_ports    | test             | 400         |
    | testing_attributes_331 | product with testing purposes | application/json  | public        |                  | 400         |
    | testing_attributes_332 | product with testing purposes | application/xml   | public        | test             | 400         |
    | testing_attributes_333 | product with testing purposes | application/json  | dependencies  |                  | 400         |
    | testing_attributes_334 | product with testing purposes | application/xml   | dependencies  | df               | 400         |

  @CLAUDIA-3742 @CLAUDIA-3741 @CLAUDIA-3740
  Scenario Outline: Add a new product with incorrect parameters

    Given a product with name "<product_name>" with description "<description>"
    When I add the new product with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name      | description                   | accept_parameter  | Error_code  |
    |                   | product with testing purposes | application/json  | 400         |
    | LONG_ID           | product with testing purposes | application/xml   | 400         |

  Scenario Outline: Cause a Not acceptable error when I add a product into the catalogue with Accept header invalid

    Given a product with name "<product_name>" with description "<description>"
    When I add the new product with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name      | description                   | accept_parameter  | Error_code  |
    | testing_product01 | product with testing purposes | json              | 406         |
    | testing_product02 | product with testing purposes | testing           | 406         |
    | @testing          | product with testing purposes | xml               | 406         |


  Scenario Outline: Cause a Not acceptable error when I add a product into the catalogue with Content Type header invalid

    Given a product with name "<product_name>" with description "<description>"
    And incorrect "<content_type>" header
    When I add the new product with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name      | description                   | accept_parameter  | Error_code  | content_type        |
    | testing_product01 | product with testing purposes | application/json  | 415         | application/json1   |
    | testing_product02 | product with testing purposes | application/json  | 415         | application/testing |


  Scenario Outline: Add a new product with incorrect token

    Given a product with name "<product_name>" with description "<description>"
    And incorrect "<token>" authentication
    When I add the new product with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name      | description   | accept_parameter  | Error_code  | token                            |
    | testing_product01 | product token | application/json  | 401         | hello world                      |
    | testing_product01 | product token | application/json  | 401         | 891855f21b2f1567afb966d3ceee1295 |
    | testing_product01 | product token | application/json  | 401         |                                  |
