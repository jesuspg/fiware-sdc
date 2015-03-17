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
    | key       | value     | description         | type  |
    | username  | postgres  | Username application| Plain |
    When I add the new product with attributes, with accept parameter "<accept_parameter>" response
    Then the product is created

    Examples:

    | product_name          | description                   | accept_parameter  |
    | testing_attributes_01 | product with testing purposes | application/json  |
    | testing_attributes_02 | product with testing purposes | application/xml   |

  Scenario Outline: Add new product with several attributes

    Given a product with name "<product_name>" with description "<description>"
    And the following attributes
    | key       | value     | description         | type  |
    | username  | postgres  | Username application| Plain |
    | password  | pwd       | password application| Plain |
    When I add the new product with attributes, with accept parameter "<accept_parameter>" response
    Then the product is created

    Examples:

    | product_name          | description                   | accept_parameter  |
    | testing_attributes_11 | product with testing purposes | application/json  |
    | testing_attributes_12 | product with testing purposes | application/xml   |


  @release_4_1
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


  @release_4_1
  Scenario Outline: Add new product with several metadatas

    Given a product with name "<product_name>" with description "<description>"
    And the following metadatas
    | key         | value     | description             |
    | mobile      | iOS       | installation type       |
    | testing     | lettuce   | testing application     |
    | nid         | 12345     | NID - GE identification |
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
    | key       | value     | description         | type  |
    | username  | postgres  | Username application| Plain |
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
    | key       | value     | description         | type  |
    | username  | postgres  | Username application| Plain |
    | password  | pwd       | password application| Plain |
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

    | product_name           | description                   | accept_parameter  | key           | value                                        |
    | testing_attributes_223 | product with testing purposes | application/json  | image         | vm_1                                         |
    | testing_attributes_224 | product with testing purposes | application/json  | image         |                                              |
    | testing_attributes_225 | product with testing purposes | application/xml   | cookbook_url  | http://www.test.org                          |
    | testing_attributes_226 | product with testing purposes | application/json  | cookbook_url  |                                              |
    | testing_attributes_227 | product with testing purposes | application/json  | cloud         | yes                                          |
    | testing_attributes_228 | product with testing purposes | application/json  | cloud         | no                                           |
    | testing_attributes_229 | product with testing purposes | application/xml   | installator   | puppet                                       |
    | testing_attributes_230 | product with testing purposes | application/xml   | installator   | chef                                         |
    | testing_attributes_231 | product with testing purposes | application/json  | open_ports    | 80 22 8080 8091                              |
    | testing_attributes_232 | product with testing purposes | application/xml   | open_ports    | 80                                           |
    | testing_attributes_233 | product with testing purposes | application/json  | open_ports    |                                              |
    | testing_attributes_250 | product with testing purposes | application/json  | open_ports    | 80-90                                        |
    | testing_attributes_251 | product with testing purposes | application/json  | open_ports    | 20 80-90                                     |
    | testing_attributes_252 | product with testing purposes | application/json  | open_ports    | 0 80-90 65535                                |
    | testing_attributes_234 | product with testing purposes | application/json  | public        | yes                                          |
    | testing_attributes_235 | product with testing purposes | application/xml   | public        | no                                           |
    | testing_attributes_236 | product with testing purposes | application/json  | dependencies  | testing_attributes_223                       |
    | testing_attributes_237 | product with testing purposes | application/xml   | dependencies  | testing_attributes_224 testing_attributes_225|
    | testing_attributes_238 | product with testing purposes | application/json  | dependencies  |                                              |
    | testing_attributes_239 | product with testing purposes | application/json  | tenant_id     | 00000000000000000000000000055                |
    | testing_attributes_240 | product with testing purposes | application/json  | tenant_id     | 00000000000000000000000000055 0000005548     |
    | testing_attributes_241 | product with testing purposes | application/json  | tenant_id     |                                              |


  Scenario Outline: Add new product changing the default metadatas: public and tenant_id

    Given a product with name "<product_name>" with description "<description>"
    And the following metadatas
    | key         | value             | description         |
    | <key>       | <value>           | None                |
    | tenant_id   | <value_tenant_id> | None                |
    When I add the new product with metadatas, with accept parameter "<accept_parameter>" response
    Then the product is created
    And the product visibility is "<visibility>"

    Examples:

    | product_name           | description                   | accept_parameter  | key           | value | value_tenant_id              | visibility  |
    | testing_attributes_242 | product with testing purposes | application/xml   | public        | yes   | CURRENT                      | visible     |
    | testing_attributes_243 | product with testing purposes | application/xml   | public        | yes   | 000000000000000022           | visible     |
    | testing_attributes_244 | product with testing purposes | application/xml   | public        | yes   | 000000000000000022 CURRENT   | visible     |
    | testing_attributes_245 | product with testing purposes | application/json  | public        | yes   |                              | visible     |
    | testing_attributes_246 | product with testing purposes | application/xml   | public        | no    | CURRENT                      | visible     |
    | testing_attributes_247 | product with testing purposes | application/xml   | public        | no    | 000000000000000022           | hidden      |
#    | testing_attributes_248 | product with testing purposes | application/xml   | public        | no    | 000000000000000022 CURRENT   | visible     | # @CLAUDIA-4130
    | testing_attributes_249 | product with testing purposes | application/json  | public        | no    |                              | hidden      |


  Scenario Outline: Add new product using incorrect metadatas

    Given a product with name "<product_name>" with description "<description>"
    And the following metadatas
    | key         | value   | description         |
    | <key>       | <value> | None                |
    When I add the new product with metadatas, with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name           | description                   | accept_parameter  | key           | value            | Error_code  |
    | testing_attributes_325 | product with testing purposes | application/json  | cloud         |                  | 400         |
    | testing_attributes_326 | product with testing purposes | application/json  | cloud         | test             | 400         |
    | testing_attributes_327 | product with testing purposes | application/xml   | installator   | test             | 400         |
    | testing_attributes_336 | product with testing purposes | application/json  | installator   |                  | 400         |
    | testing_attributes_329 | product with testing purposes | application/json  | open_ports    | 111111111111111  | 400         |
    | testing_attributes_335 | product with testing purposes | application/json  | open_ports    | 80 test          | 400         |
    | testing_attributes_340 | product with testing purposes | application/json  | open_ports    | -90              | 400         |
    | testing_attributes_341 | product with testing purposes | application/json  | open_ports    | 90-              | 400         |
    | testing_attributes_342 | product with testing purposes | application/json  | open_ports    | 90- 33           | 400         |
    | testing_attributes_343 | product with testing purposes | application/json  | open_ports    | 22 -90-          | 400         |
    | testing_attributes_344 | product with testing purposes | application/json  | open_ports    | 22 -90           | 400         |
    | testing_attributes_345 | product with testing purposes | application/json  | open_ports    | 22 -90- 55       | 400         |
    | testing_attributes_347 | product with testing purposes | application/json  | open_ports    | 65536            | 400         |
    | testing_attributes_330 | product with testing purposes | application/xml   | open_ports    | test             | 400         |
    | testing_attributes_331 | product with testing purposes | application/json  | public        |                  | 400         |
    | testing_attributes_332 | product with testing purposes | application/xml   | public        | test             | 400         |
    | testing_attributes_334 | product with testing purposes | application/xml   | dependencies  | df               | 400         |


  @release_4_1
  Scenario Outline: Add new product with valid attribute 'type'

    Given a product with name "<product_name>" with description "<description>"
    And the following attributes
    | key       | value     | description         | type    |
    | username  | postgres  | Username application| <type>  |
    When I add the new product with attributes, with accept parameter "<accept_parameter>" response
    Then the product is created

    Examples:

    | product_name          | description                   | type            | accept_parameter  |
    | testing_attributes_01 | product with testing purposes | Plain           | application/json  |
    | testing_attributes_02 | product with testing purposes | Plain           | application/xml   |
    | testing_attributes_03 | product with testing purposes | IP              | application/json  |
    | testing_attributes_04 | product with testing purposes | IP              | application/xml   |
    | testing_attributes_05 | product with testing purposes | IPALL           | application/json  |
    | testing_attributes_06 | product with testing purposes | IPALL           | application/xml   |
    | testing_attributes_07 | product with testing purposes | [MISSING_PARAM] | application/json  |
    | testing_attributes_08 | product with testing purposes | [MISSING_PARAM] | application/xml   |


  @release_4_1
  Scenario Outline: Add new product with invalid value for attribute 'type'

    Given a product with name "<product_name>" with description "<description>"
    And the following attributes
    | key       | value     | description         | type    |
    | username  | postgres  | Username application| <type>  |
    When I add the new product with attributes, with accept parameter "<accept_parameter>" response
    Then I obtain an "400"

    Examples:

    | product_name          | description                   | type                    | accept_parameter  |
    | testing_attributes_01 | product with testing purposes |                         | application/json  |
    | testing_attributes_02 | product with testing purposes | Plaint                  | application/json  |
    | testing_attributes_03 | product with testing purposes | Plai                    | application/json  |
    | testing_attributes_04 | product with testing purposes | nt                      | application/json  |
    | testing_attributes_05 | product with testing purposes | I                       | application/json  |
    | testing_attributes_06 | product with testing purposes | I p                     | application/json  |
    | testing_attributes_07 | product with testing purposes | PAll                    | application/json  |
    | testing_attributes_08 | product with testing purposes | IPAL                    | application/json  |
    | testing_attributes_09 | product with testing purposes | [STRING_WITH_LENGTH_4]  | application/json  |
    | testing_attributes_10 | product with testing purposes | [STRING_WITH_LENGTH_257]| application/json  |


  Scenario Outline: Add a new product with incorrect parameters

    Given a product with name "<product_name>" with description "<description>"
    When I add the new product with accept parameter "<accept_parameter>" response
    Then I obtain an "<Error_code>"

    Examples:

    | product_name      | description                   | accept_parameter  | Error_code  |
    |                   | product with testing purposes | application/json  | 400         |
    | LONG_ID           | product with testing purposes | application/xml   | 400         |


  Scenario Outline: Add new product with name already registered

    Given a created product with name "<product_name>" with description "<description>"
    And another product with name "<another_product_name>" with description "<description>"
    When I add the new product with accept parameter "<accept_parameter>" response
    Then I obtain an "<error_code>"

    Examples:

    | product_name          | another_product_name  | description                   | accept_parameter  | error_code  |
    | testing_attributes_01 | testing_attributes_01 | product with testing purposes | application/json  | 409         |
    | testing_attributes_02 | testing_attributes_02 | product with testing purposes | application/xml   | 409         |


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

  @auth
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


  Scenario Outline: Add a new product with invalid open_ports_udp metadata
    Given a product with name "<product_name>" with description "QA Product - Testing UDP metadata"
    And the following metadatas
          | key             | value                   | description     |
          | open_ports_udp  | <open_ports_udp_value>  | Open Ports UDP  |
    When I add the new product with metadatas, with accept parameter "<accept_parameter>" response
    Then I obtain an "400"

    Examples:
        | product_name         | accept_parameter  | open_ports_udp_value     |
        | testing_metadatas_01 | application/json  | text                     |
        | testing_metadataa_02 | application/xml   | 8080 LE                  |
        | testing_metadatas_03 | application/xml   | 8080-LE                  |
        | testing_metadatas_04 | application/json  | LE 8080-8090             |
        | testing_metadatas_05 | application/xml   | 80 8080-                 |
        | testing_metadatas_06 | application/json  | -8080                    |
        | testing_metadatas_07 | application/json  | -8080-                   |
        | testing_metadatas_08 | application/json  | 8080-20-12               |
        | testing_metadatas_09 | application/json  | 8080 -22                 |
        | testing_metadatas_11 | application/json  | 65536                    |


  Scenario Outline: Add a new product with open_ports_udp metadata
    Given a product with name "<product_name>" with description "QA Product - Testing UDP metadata"
    And the following metadatas
          | key             | value                   | description     |
          | open_ports_udp  | <open_ports_udp_value>  | Open Ports UDP  |
    When I add the new product with metadatas, with accept parameter "<accept_parameter>" response
    Then the product is created

    Examples:
        | product_name         | accept_parameter  | open_ports_udp_value     |
        | testing_metadatas_01 | application/json  | 8080                     |
        | testing_metadatsa_02 | application/xml   | 8080 20                  |
        | testing_metadatsa_03 | application/xml   | 8080-8090                |
        | testing_metadatsa_04 | application/json  | 80 8080-8090             |
        | testing_metadatsa_05 | application/xml   | 0 8080-8090 65535        |
        | testing_metadatsa_06 | application/json  | 8080 25 26 27 8080-8090  |
