# -*- coding: utf-8 -*-
Feature: Install product
    As a fi-ware user
    I want to be able to install a product release in a Virtual Machine
    so that I can use it

    Scenario Outline: Install a new product release in the catalogue by hostname
      Given a created product with name "<product_name>" and release "<product_release>"
      And the accept header "<accept>"
      When I install the product in the VM with hostname "<hostname>"
      Then the product is installed

      Examples:

      | product_name      | product_release | hostname  | accept          |
      | test_module       | 1.0.0           | testing   | application/json|
      | test_module       | 2.0.0           | testing   | application/xml |


    Scenario Outline: Install a new product release in the catalogue by IP and hostname
      Given a created product with name "<product_name>" and release "<product_release>"
      And the accept header "<accept>"
      When I install the product in the VM with IP "<ip>" and hostname "<hostname>"
      Then the product is installed

      Examples:

      | product_name      | product_release | ip              | accept          | hostname  |
      | test_module       | 6.0.0           | 130.206.81.137  | application/json| testing   |
      | test_module       | 7.0.0           | 130.206.81.137  | application/xml | testing   |
      | test_module       | 8.0.0           | 130.206.81.138  | application/xml | testing   |
      | test_module       | 8.0.0           | 10.95.138.151   | application/xml | testing   |
      | test_module       | 8.0.0           | 266.95.138.151  | application/xml | testing   |
      | test_module       | 8.0.0           | 0.0.0.0         | application/xml | testing   |
      | test_module       | 8.0.0           | 255.255.255.255 | application/xml | testing   |
      | test_module       | 8.0.0           | hola don pepito | application/xml | testing   |


    Scenario Outline: Install a new product by fqn and hostname
      Given a created product with name "<product_name>" and release "<product_release>"
      And the accept header "<accept>"
      When I install the product in the VM with fqn "<fqn>" and hostname "<hostname>"
      Then the product is installed

      Examples:

      | product_name      | product_release | hostname  | accept          | fqn           |
      | test_module       | 9.0.0           | testing   | application/json| existant_fqn  |
      | test_module       | 10.0.0          | testing   | application/xml | not_existant  |


    Scenario Outline: Install a new product release by OSType and hostname

      Given a created product with name "<product_name>" and release "<product_release>"
      And the accept header "<accept>"
      When I install the product in the VM with OSType "<OStype>" and hostname "<hostname>"
      Then the product is installed

      Examples:

      | product_name      | product_release | hostname  | accept          | OStype    |
      | test_module       | 9.0.0           | testing   | application/json| 95        |
      | test_module       | 10.0.0          | testing   | application/xml | ubuntu    |


    Scenario Outline: Install a new product release with all data

      Given a created product with name "<product_name>" and release "<product_release>"
      And the accept header "<accept>"
      When I install the product with hostname "<hostname>", ip "<ip>", fqn "<fqn>" and OStype "<OStype>"
      Then the product is installed

      Examples:

      | product_name      | product_release | hostname  | accept          | OStype    | ip              | fqn           |
      | test_module       | 9.0.0           | testing   | application/json| 95        | 130.206.81.137  | existant      |
      | test_module       | 10.0.0          | testing   | application/xml | ubuntu    | 130.206.81.137  | not_existant  |

    @bug
    Scenario Outline: Install a not existant product

      Given a not existent product with name "<product_name>"
      And the accept header "<accept>"
      When I install the product in the VM with hostname "<hostname>"
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | hostname  | accept          | Error_code |
      | TEST_MODULE       | testing   | application/json| 404        |
      | Test_module       | testing   | application/xml | 404        |
      | Test_Module       | testing   | application/xml | 404        |
      | testmodule        | testing   | application/xml | 404        |
      | Test module       | testing   | application/xml | 404        |
      | Test-module       | testing   | application/xml | 404        |
      | not existant      | testing   | application/xml | 404        |

    @bug
    Scenario Outline: Install a not existant product release

      Given a existent product with name "<product_name>" and no product release
      And the accept header "<accept>"
      When I install the product in the VM with hostname "<hostname>"
      Then I obtain an "<Error_code>"

      Examples:

      | product_name      | hostname  | accept            | Error_code |
      | test_module       | testing   | application/json  | 404        |
      | test_module       | testing   | application/xml   | 404        |

    @bug
    Scenario Outline: Install a new product in not existant VM
      Given a created product with name "<product_name>" and release "<product_release>"
      And the accept header "<accept>"
      When I install the product in the VM with hostname "<hostname>"
      Then the product is not installed

      Examples:

      | product_name      | product_release | hostname      | accept          |
      | test_module       | 5.0.0           | not_existant  | application/json|
      | test_module       | 3.0.0           | @testing      | application/xml |

    @bug
    Scenario Outline: Install a product release installed

      Given a installed product with name "<product_name>" and release "<product_release>" in the hostname "<hostname>"
      And the accept header "<accept>"
      When I install the product in the VM with hostname "<hostname>"
      Then I obtain the error "<error_description>" in the task

      Examples:

      | product_name  | product_release | hostname  | accept            | error_description |
      | test_module   | 25.0.0          | testing   | application/json  | test              |


    Scenario Outline: Install a product installed but another release

      Given a installed product with name "<product_name>" and release "<product_release>" in the hostname "<hostname>"
      And a created product with name "<product_name>" and release "<new_product_release>"
      And the accept header "<accept>"
      When I install the release "<new_product_release>" in the VM with hostname "<hostname>"
      Then the product is installed

       Examples:

      | product_name  | product_release | hostname  | accept            | new_product_release |
      | test_module   | 4.0.0           | testing   | application/json  | 5.0.0               |
      | test_module   | 5.0.0           | testing   | application/json  | 1.0.0               |


    Scenario Outline: Install a product with incorrect headers

      Given a created product with name "<product_name>" and release "<product_release>"
      And the accept header "<accept>"
      When I install the product in the VM with hostname "<hostname>"
      Then I obtain an "<Error_code>"

       Examples:

      | product_name  | product_release | hostname  | accept    | Error_code  |
      | test_module   | 4.0.0           | testing   | json      | 406         |
      | test_module   | 5.0.0           | testing   | testing   | 406         |


    Scenario Outline: Install a product with incorrect token

      Given a created product with name "<product_name>" and release "<product_release>"
      And the accept header "<accept>"
      And incorrect "<token>" authentication
      When I install the product in the VM with hostname "<hostname>"
      Then I obtain an "<Error_code>"

       Examples:

      | product_name  | product_release | hostname  | accept            | Error_code  | token                            |
      | test_module   | 4.0.0           | testing   | application/json  | 401         | hello world                      |
      | test_module   | 5.0.0           | testing   | application/json  | 401         | 891855f21b2f1567afb966d3ceee1295 |
      | test_module   | 5.0.0           | testing   | application/json  | 401         |                                  |

