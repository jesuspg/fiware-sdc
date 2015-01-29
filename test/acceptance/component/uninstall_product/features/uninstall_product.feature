# -*- coding: utf-8 -*-
Feature: Uninstall product
    As a fi-ware user
    I want to be able to uninstall a product release in a Virtual Machine
    so that I can delete it

    @happy_path
    Scenario Outline: Uninstall a installed product release

      Given a virtual machine with these parameters:
	  | fqn                 | hostname    | ip              | ostype    |
	  | qa-test-vm.testing  | qa-test-vm  | 111.111.111.111 | 95        |
      And a installed product with name "<product_name>" and release "<product_release>"
      When I uninstall the installed product "<product_name>" and release "<product_release>"
      Then the task is created
      And the product is instantiated

      Examples:
      | product_name    | product_release |
      | testing_prov_80 | 1.2.3           |
      | testing_prov_80 | 1.2.4           |


    Scenario Outline: Uninstall a installed product release with different installators

      Given a configuration management with "<cm_tool>"
      And a virtual machine with these parameters:
	  | fqn                 | hostname    | ip              | ostype    |
	  | qa-test-vm.testing  | qa-test-vm  | 111.111.111.111 | 95        |
      And a installed product with name "<product_name>" and release "<product_release>"
      When I uninstall the installed product "<product_name>" and release "<product_release>"
      Then the task is created
      And the product is instantiated

      Examples:
      | product_name    | product_release | cm_tool |
      | testing_prov_81 | 1.0.0           | chef    |
      | testing_prov_82 | 1.0.0           | puppet  |


    Scenario Outline: Uninstall a installed product release with different representations

      Given a virtual machine with these parameters:
	  | fqn                 | hostname    | ip              | ostype    |
	  | qa-test-vm.testing  | qa-test-vm  | 111.111.111.111 | 95        |
      And a installed product with name "<product_name>" and release "<product_release>"
      And content type header values:
      | content_type          | accept          |
      | <content_type_header> | <accept_header> |
      When I uninstall the installed product "<product_name>" and release "<product_release>"
      Then the task is created
      And the product is instantiated

      Examples:
      | product_name    | product_release | content_type_header | accept_header     |
      | testing_prov_80 | 1.2.3           | application/xml     | application/xml   |
      | testing_prov_80 | 1.2.4           | application/xml     | application/json  |


    Scenario Outline: Uninstall a non existent product release

      Given a virtual machine with these parameters:
	  | fqn                 | hostname    | ip              | ostype    |
	  | qa-test-vm.testing  | qa-test-vm  | 111.111.111.111 | 95        |
      And a installed product with name "<product_name>" and release "<product_release>"
      When I uninstall the installed product "<product_name>" and release "<another_product_release>"
      Then I obtain an "<error_code>"

      Examples:
      | product_name    | product_release | another_product_release | error_code  |
      | testing_prov_80 | 1.2.3           | testing                 | 404         |
      | testing_prov_80 | 1.2.4           | 0.0.0                   | 404         |


    Scenario Outline: Uninstall a non existent product

      Given a virtual machine with these parameters:
	  | fqn                 | hostname    | ip              | ostype    |
	  | qa-test-vm.testing  | qa-test-vm  | 111.111.111.111 | 95        |
      And a installed product with name "<product_name>" and release "<product_release>"
      When I uninstall the installed product "<another_product_name>" and release "<product_release>"
      Then I obtain an "<error_code>"

      Examples:
      | product_name      | product_release | another_product_name  | error_code  |
      | testing_prov_80   | 1.2.3           | testing               | 404         |
      | testing_prov_80   | 1.2.4           | qa                    | 404         |


    Scenario Outline: Uninstall a product with incorrect headers

      Given a virtual machine with these parameters:
	  | fqn                 | hostname    | ip              | ostype    |
	  | qa-test-vm.testing  | qa-test-vm  | 111.111.111.111 | 95        |
      And a installed product with name "testing_prov_80" and release "1.2.3 "
      And the accept header "<accept>"
      When I uninstall the installed product "<product_name>" and release "<product_release>"
      Then I obtain an "<error_code>"

      Examples:
      | accept  | error_code  |
      | json    | 406         |
      | testing | 406         |


    @auth
    Scenario Outline: Uninstall a product with incorrect token

      Given a virtual machine with these parameters:
	  | fqn                 | hostname    | ip             | ostype    |
	  | qa-test-vm.testing  | qa-test-vm  | 111.111.111.111 | 95        |
      And a installed product with name "testing_prov_80" and release "1.2.3"
      And incorrect "<token>" authentication
      When I uninstall the installed product "<product_name>" and release "<product_release>"
      Then I obtain an "<error_code>"

       Examples:
      | error_code  | token                             |
      | 401         | 891855f21b2f1567afb966d3ceee1295  |
      | 401         | hello world                       |
      | 401         |                                   |

