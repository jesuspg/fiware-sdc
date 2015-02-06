# -*- coding: utf-8 -*-
Feature: Install product
    As a fi-ware user
    I want to be able to install a product release in a Virtual Machine
    so that I can use it


    @happy_path
    Scenario Outline: Install a new product release

      Given a created product with name "<product_name>" and release "<product_release>"
      And a virtual machine with these parameters:
	  | fqn                 | hostname    | ip             | ostype    |
	  | qa-test-vm.testing  | qa-test-vm  | 130.206.81.137 | <ostype>  |
      When I install the product in the VM
      Then the task is created
      And the product is instantiated

      Examples:

      | product_name      | product_release | ostype    |
      | testing_prov_20   | 9.0.0           | 95        |
      | testing_prov_20   | 10.0.0          | ubuntu    |
      | testing_prov_21   | 9.0.0           | ubuntu    |


    @happy_path
    Scenario Outline: Install a new product release with different installator

      Given a configuration management with "<cm_tool>"
      And a created product with name "<product_name>" and release "<product_release>"
      And a virtual machine with these parameters:
	  |fqn                | hostname    | ip              | ostype    |
	  |qa-test-vm.testing | qa-test-vm  | 111.111.111.111 | 95        |
      When I install the product in the VM
      Then the task is created
      And the product is instantiated

      Examples:

      | product_name      | product_release | cm_tool |
      | testing_prov_30	  | 0.0.1 	        | chef    |
      | testing_prov_30	  | 1.2.3 	        | chef    |
      | testing_prov_31	  | 0.0.1 	        | puppet  |
      | testing_prov_31	  | 1.0.1 	        | puppet  |


    @happy_path
    Scenario Outline: Install a new product release with instance attributes

      Given a configuration management with "<cm_tool>"
      And a created product with name "<product_name>" and release "<product_release>"
      And a virtual machine with these parameters:
	  |fqn                | hostname    | ip              | ostype    |
	  |qa-test-vm.testing | qa-test-vm  | 111.111.111.111 | 95        |
      And the following instance attributes:
      | key       | value       | description |
      | att_key_1 | att_value_1 | Att 1       |
      | att_key_2 | att_value_2 | Att 2       |
      | att_key_3 | att_value_3 | Att 3       |
      When I install the product in the VM
      Then the task is created
      And the product is instantiated

      Examples:

      | product_name      | product_release | cm_tool |
      | testing_prov_30	  | 0.0.1 	        | chef    |
      | testing_prov_31	  | 0.0.1 	        | puppet  |


    Scenario Outline: Install the same product in different VM

      Given a created product with name "testing_prov_40" and release "1.0.0"
      And a virtual machine with these parameters:
	  |fqn   | hostname    | ip   | ostype    |
	  |<fqn> | <hostname>  | <ip> | <ostype>  |
      When I install the product in the VM
      Then the task is created
      And the product is instantiated

      Examples:

      | hostname      | ostype    | ip              | fqn                   |
      | qa-test-vm    | 95        | 130.206.81.137  | qa-test-vm.testing    |
      | qa-test-a-vm  | ubuntu    | 130.206.81.138  | qa-test-a-vm.testing  |
      | qa-test-b-vm  | ubuntu    | 130.206.81.139  | qa-test-b-vm.testing  |
      | qa-test-c-vm  | ubuntu    | 130.206.81.140  | qa-test-c-vm.testing  |
      | qa-test-d-vm  | ubuntu    | 130.206.81.141  | qa-test-d-vm.testing  |
      | qa-test-e-vm  | ubuntu    | 130.206.81.142  | qa-test-e-vm.testing  |


    Scenario Outline: Install a new product release using different representations

      Given a created product with name "testing_prov_50" and release "0.0.1"
      And a virtual machine with these parameters:
	  |fqn                | hostname    | ip              | ostype    |
	  |qa-test-vm.testing | qa-test-vm  | 111.111.111.111 | 95        |
      And content type header values:
      | content_type          | accept          |
      | <content_type_header> | <accept_header> |
      When I install the product in the VM
      Then the task is created
      And the product is instantiated

      Examples:

      | content_type_header | accept_header     |
      | application/xml     | application/xml   |
      | application/xml     | application/json  |
      | application/json    | application/xml   |
      | application/json    | application/json  |


  	Scenario Outline: Install a new product with empty param

      Given a configuration management with "<cm_tool>"
	  And a created product with name "<product_name>" and release "<product_release>"
      And a virtual machine with these parameters:
	  |fqn                | hostname    | ip              | ostype    |
	  |qa-test-vm.testing | qa-test-vm  | 111.111.111.111 | 95        |
      When I try to install the product with empty params "<empty_params>"
      Then the task is not created
      And the product is not instantiated

      Examples:

      | product_name	| product_release 	| cm_tool 	| empty_params 								|
      | testing_prov_10	| 0.0.1 	        | chef  	| product_name 								|
      | testing_prov_10	| 0.0.1 	        | chef  	| release									|
      | testing_prov_10	| 0.0.1 	        | chef  	| hostname     								|
      | testing_prov_11	| 0.0.1 	        | puppet  	| hostname     								|
      | testing_prov_10	| 0.0.1 	        | chef  	| fqn          								|
      | testing_prov_10	| 0.0.1 	        | chef  	| product_name, release, fqn		 		|
      | testing_prov_11	| 0.0.1 	        | puppet  	| product_name, release, ip, hostname, fqn 	|


    Scenario Outline: Install a new product by fqn and hostname
      Given a created product with name "<product_name>" and release "<product_release>"
      And a virtual machine with these parameters:
	  |fqn   | hostname    |
	  |<fqn> | <hostname>  |
      When I install the product in the VM
      Then the task is created
      And the product is instantiated

      Examples:

      | product_name      | product_release | hostname    | fqn           |
      | testing_prov_61   | 9.0.0           | qa-test-vm  | fqn.testing   |
      | testing_prov_61   | 10.0.0          | qa-test-vm  | fqn_testing   |


    Scenario: Install a new product release in the catalogue with missing VM parameters: FQN, IP, OSTYPE

      Given a created product with name "testing_prov_62" and release "1.0.0"
      And a virtual machine with these parameters:
	  | hostname    |
	  | qa-test-vm |
      When I install the product in the VM
      Then the task is not created
      And I obtain an "400"
      And the product is not instantiated


    Scenario: Install a new product release in the catalogue with missing VM parameters: FQN, OSTYPE
      Given a created product with name "testing_prov_63" and release "1.0.0"
      And a virtual machine with these parameters:
	  |ip              | hostname    |
	  |111.111.111.111 | qa-test-vm  |
      When I install the product in the VM
      Then the task is not created
      And I obtain an "400"
      And the product is not instantiated


    Scenario: Install a new product release in the catalogue with missing VM parameters: FQN, IP

      Given a created product with name "testing_prov_64" and release "1.0.0"
      And a virtual machine with these parameters:
	  |ostype   | hostname    |
	  |95       | qa-test-vm  |
      When I install the product in the VM
      Then the task is not created
      And I obtain an "400"
      And the product is not instantiated


    Scenario: Install a new product release in the catalogue with missing VM parameters: HOSTNAME

      Given a created product with name "testing_prov_64" and release "1.0.0"
      And a virtual machine with these parameters:
	  |ostype   | ip              | fqn         |
	  |95       | 111.111.111.111 | fqn.testing |
      When I install the product in the VM
      Then the task is not created
      And I obtain an "400"
      And the product is not instantiated


    @release_4_1
    Scenario Outline: Install a new product with instance attributes (with valid type)

      Given a configuration management with "<cm_tool>"
      And a created product with name "<product_name>" and release "<release>"
      And the following instance attributes:
      | key           | value                   | description         | type   |
      | custom_att_01 | new_val_att_1           | Testing attributes  | Plain  |
      | custom_att_02 | 192.168.1.1             | Testing attributes  | IP     |
      | custom_att_03 | 192.168.1.1,192.168.1.1 | Testing attributes  | IPALL  |
      And a virtual machine with these parameters:
      | fqn                | hostname    | ip              |
	  | qa-test-vm.testing | qa-test-vm  | 111.111.111.111 |
      When I install the product in the VM
      Then the task is created
      And the product is instantiated

      Examples:

      | product_name    | release	| cm_tool 	|
      | testing_prov_71 | 1.2.3 	| chef  	|
      | testing_prov_72 | 1.2.3 	| puppet  	|


    @release_4_1
    Scenario Outline: Install a new product with invalid type for instance attributes

      Given a configuration management with "<cm_tool>"
      And a created product with name "<product_name>" and release "<release>"
      And the following instance attributes:
      | key           | value         | description         | type              |
      | custom_att_01 | new_val_att_1 | Testing attributes  | <attribute_type>  |
      | custom_att_02 | new_val_att_3 | Testing attributes  | Plain             |
      And a virtual machine with these parameters:
      | fqn            | hostname        | ip              |
	  | qa-test-vm.testing | qa-test-vm  | 111.111.111.111 |
      When I install the product in the VM
      Then the task is not created
      And I obtain an "400"
      And the product is not instantiated

      Examples:

      | product_name    | release	| cm_tool 	| attribute_type |
      | testing_prov_81 | 1.2.3 	| chef  	| Plai           |
      | testing_prov_82 | 1.2.3 	| puppet  	| lalala         |
      | testing_prov_83 | 1.2.3 	| puppet  	| IPAL           |


    @release_4_1
    Scenario Outline: Install a new product with instance attributes: missing type

      Given a configuration management with "<cm_tool>"
      And a created product with name "<product_name>" and release "<release>"
      And the following instance attributes:
      | key           | value         | description         |
      | custom_att_01 | missing_type  | Testing attributes  |
      And a virtual machine with these parameters:
      | fqn            | hostname        | ip              |
      | qa-test-vm.testing | qa-test-vm  | 111.111.111.111 |
      When I install the product in the VM
      Then the task is created
      And the product is instantiated

      Examples:

      | product_name    | release	| cm_tool 	|
      | testing_prov_84 | 1.2.3 	| chef  	|
      | testing_prov_85 | 1.2.3 	| puppet  	|


    Scenario Outline: Install a not existent product

      Given a non existent product with name "<product_name>"
      And a VM with hostname "qa-test-vm" and fqn "qa-test-vm.testing"
      When I install the product in the VM
      Then I obtain an "<error_code>"

      Examples:

      | product_name      | error_code |
      | TEST_MODULE       | 404        |
      | Test_module       | 404        |
      | Test_Module       | 404        |
      | testmodule        | 404        |
      | Test module       | 404        |
      | Test-module       | 404        |
      | not existant      | 404        |


    Scenario: Install a non existent product release

      Given a existent product with name "testing_prov_64" and no product release
      And a VM with hostname "qa-test-vm" and fqn "qa-test-vm.testing"
      When I install the product in the VM
      Then I obtain an "404"


    Scenario Outline: Install a product with incorrect headers

      Given a created product with name "<product_name>" and release "<product_release>"
      And a VM with hostname "qa-test-vm" and fqn "qa-test-vm.testing"
      And And the accept header "<accept_header>"
      When I install the product in the VM
      Then I obtain an "<error_code>"

       Examples:

      | product_name    | product_release | accept_header     | error_code  |
      | testing_prov_62 | 4.0.0           | application/json1 | 406         |
      | testing_prov_62 | 5.0.0           | application/test  | 406         |


    @auth
    Scenario Outline: Install a product with incorrect token

      Given a created product with name "<product_name>" and release "<product_release>"
      And a VM with hostname "qa-test-vm" and fqn "qa-test-vm.testing"
      And incorrect "<token>" authentication
      When I install the product in the VM
      Then I obtain an "<error_code>"

       Examples:

      | product_name  | product_release | error_code  | token                            |
      | test_module   | 4.0.0           | 401         | hello world                      |
      | test_module   | 5.0.0           | 401         | 891855f21b2f1567afb966d3ceee1295 |
      | test_module   | 5.0.0           | 401         |                                  |
