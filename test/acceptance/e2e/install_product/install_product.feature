# -*- coding: utf-8 -*-
Feature: Install product with E2E validations
    As a fi-ware user
    I want to be able to install a product release in a Virtual Machine
    so that I can use it


    @happy_path
    Scenario Outline: Install a new product release

      Given a configuration management with "<cm_tool>"
      And a created product with name "${CONFIG_FILE}" and release "${CONFIG_FILE}"
      And a virtual machine with these parameters:
	  | fqn            | hostname        | ip              |
	  | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      When I install the product in the VM
      Then the task is created
      And the task is performed
      And the product installation status is "INSTALLED"
      And the product is installed

      Examples:

      | cm_tool |
      | chef    |
      | puppet  |


    Scenario Outline: Install a new product with different releases

      Given a configuration management with "<cm_tool>"
      And a created product with name "<product_name>" and release "<product_version>"
      And a virtual machine with these parameters:
	  | fqn            | hostname        | ip              |
	  | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      When I install the product in the VM
      Then the task is created
      And the task is performed
      And the product installation status is "INSTALLED"
      And the product is installed

      Examples:

      | product_name              | product_version | cm_tool |
      | qa-test-product-chef-01   | 1.2.3           | chef    |
      | qa-test-product-chef-01   | 1.2.4           | chef    |
      | qa-test-product-puppet-01 | 1.2.3           | puppet  |
      | qa-test-product-puppet-01 | 1.2.4           | puppet  |


    @skip @slow @CLAUDIA-4165 @CLAUDIA-4163
    Scenario Outline: Install a new product when cookbook does not exist

	  Given a configuration management with "<cm_tool>"
      And a created product with name "<product_name>" and release "<release>"
      And a virtual machine with these parameters:
	  | fqn            | hostname        | ip              |
	  | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      When I install the product in the VM
      Then the task is created
      And the task has finished with status "ERROR" after "805" checks
      And the task has the minor error code "NodeExecutionException"
      And the product installation status is "UNINSTALLED"

      Examples:

      | product_name                    | release	| cm_tool 	|
      | testing_prov_no_cookbook_chef   | 1.0.0 	| chef  	|
      | testing_prov_no_cookbook_puppet | 1.0.1 	| puppet  	|

    @slow
    Scenario Outline: Install a existing product when VM hostname is not registered

	  Given a configuration management with "<cm_tool>"
      And a created product with name "${CONFIG_FILE}" and release "${CONFIG_FILE}"
      And a virtual machine with these parameters:
	  | fqn                 | hostname    | ip              |
	  | not_exists.testing	| not_exists  | 111.111.111.111 |
      When I install the product in the VM
      Then the task is created
      And the task has finished with status "ERROR"
      And the task has the minor error code "<minor_code>"
      And the product installation status is "UNINSTALLED"

      Examples:

      | cm_tool | minor_code          |
      | chef    | SdcRuntimeException |
      | puppet  | SdcRuntimeException |


    @release_4_1
    Scenario Outline: Install a new product with "installation attributes" (Plain and IP types)

      Given a configuration management with "<cm_tool>"
      And the following instance attributes:
      | key           | value           | description         | type   |
      | custom_att_01 | new_val_att_1   | Testing attributes  | Plain  |
      | custom_att_02 | 192.168.1.1     | Testing attributes  | IP     |
      And a created product with name "<product_name>" and release "<release>"
      And a virtual machine with these parameters:
      | fqn            | hostname        | ip              |
      | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      When I install the product in the VM
      Then the task is created
      And the task is performed
      And the product installation status is "INSTALLED"
      And the product with attributes is installed

      Examples:

      | product_name                  | release	| cm_tool 	|
      | qa-test-product-chef-att-01   | 1.2.3 	| chef  	|
      | qa-test-product-puppet-att-01 | 1.2.3 	| puppet  	|


    @release_4_1
    Scenario Outline: Install a new product with "installation attributes" (IPALL type, single IP)

      Given a configuration management with "<cm_tool>"
      And the following instance attributes:
      | key           | value     | description         | type   |
      | custom_att_01 | <ip_all>  | Testing attributes  | IPALL  |
      And a created product with name "<product_name>" and release "<release>"
      And a virtual machine with these parameters:
      | fqn            | hostname        | ip              |
      | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      When I install the product in the VM
      Then the task is created
      And the task is performed
      And the product installation status is "INSTALLED"
      And the product with attributes is installed

      Examples:

      | product_name                  | release	| cm_tool 	| ip_all                  |
      | qa-test-product-chef-att-01   | 1.2.3 	| chef  	| 10.20.125.1,10.20.125.2 |
      | qa-test-product-puppet-att-01 | 1.2.3 	| puppet  	| 10.20.125.3,10.20.125.4 |


    @release_4_1
    Scenario Outline: Install a new product with "installation attributes" (IPALL type, IP list)

      Given a configuration management with "<cm_tool>"
      And the following instance attributes:
      | key           | value     | description         | type   |
      | custom_att_01 | <ip_all>  | Testing attributes  | IPALL  |
      And a created product with name "<product_name>" and release "<release>"
      And a virtual machine with these parameters:
      | fqn            | hostname        | ip              |
      | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      When I install the product in the VM
      Then the task is created
      And the task is performed
      And the product installation status is "INSTALLED"
      And the product with attributes is installed

      Examples:

      | product_name                  | release	| cm_tool 	| ip_all                  |
      | qa-test-product-chef-att-01   | 1.2.3 	| chef  	| 10.20.125.1,10.20.125.2 |
      | qa-test-product-puppet-att-01 | 1.2.3 	| puppet  	| 10.20.125.3,10.20.125.4 |


    Scenario Outline: Install a new product with "product attributes" (catalog)

      Given a configuration management with "<cm_tool>"
      And the following product attributes:
      | key            | value             | description   | type   |
      | custom_att_01  | att_prod_value_1  | Prod att 1    | Plain  |
      | other_att_01   | att_prod_value_2  | Prod att 2    | Plain  |
      And a created product with name "<product_name>" and release "<release>"
      And a virtual machine with these parameters:
      | fqn            | hostname        | ip              |
      | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      When I install the product in the VM
      Then the task is created
      And the task is performed
      And the product installation status is "INSTALLED"
      And the product has the correct attributes in the catalog
      And the product instance has been installed without attributes

      Examples:

      | product_name                  | release	| cm_tool 	|
      | qa-test-product-chef-att-01   | 1.2.3   | chef  	|
      | qa-test-product-puppet-att-01 | 1.2.3   | puppet    |


    Scenario Outline: Install a new product release with product attributes (catalog) and instance attributes

      Given a configuration management with "<cm_tool>"
      And the following product attributes:
      | key             | value             | description   | type   |
      | custom_att_01   | att_prod_def_1    | Prod att 1    | Plain  |
      | custom_att_02   | att_prod_def_2    | Prod att 2    | Plain  |
      | other_att_01    | att_prod_def_other| Prod att 2    | Plain  |
      And a created product with name "<product_name>" and release "<product_release>"
      And a virtual machine with these parameters:
      | fqn            | hostname        | ip              |
      | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      And the following instance attributes:
      | key           | value           | description         | type   |
      | custom_att_01 | new_val_att_1   | Testing attributes  | Plain  |
      | custom_att_02 | new_val_att_2   | Testing attributes  | Plain  |
      When I install the product in the VM
      Then the task is created
      And the task is performed
      And the product installation status is "INSTALLED"
      And the product has the correct attributes in the catalog
      And the product with attributes is installed

      Examples:

      | product_name                  | product_release | cm_tool |
      | qa-test-product-chef-att-01   | 1.2.3 	        | chef    |
      | qa-test-product-puppet-att-01 | 1.2.3 	        | puppet  |


    Scenario Outline: Install a product release installed

      Given a configuration management with "<cm_tool>"
      And a virtual machine with these parameters:
	  | fqn            | hostname        | ip              |
	  | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      And a installed product with name "${CONFIG_FILE}" and release "${CONFIG_FILE}"
      When I install the product in the VM
      Then the task is created
      And the task has finished with status "ERROR"
      And the task has the minor error code "AlreadyInstalledException"

      Examples:

      | cm_tool |
      | chef    |
      | puppet  |
