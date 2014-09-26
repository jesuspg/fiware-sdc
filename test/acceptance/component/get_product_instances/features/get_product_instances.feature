# -*- coding: utf-8 -*-
Feature: Get product instances
    As a fi-ware user
    I want to be able to retrieve information about the products installed
    so that I can manage it.

  @happy_path
  Scenario: Get all product instances
  	Given a created and installed product
  	| product_name  	| version 	| cm_tool | ip		 		| hostname		| fqn				 |
	| testing_prov_01 	| 0.0.1 	| chef    | 111.111.111.111 | qa-test-vm	| qa-test-vm.testing |
	When I get all product instances
	Then the product instance is in the returned list

  @happy_path
  Scenario: Get a product instance details
  	Given a created and installed product
  	| product_name  	| version 	| cm_tool | ip		 		| hostname		| fqn				 |
	| testing_prov_01 	| 0.0.1 	| chef    | 111.111.111.111 | qa-test-vm	| qa-test-vm.testing |
	When I get the product instance details
	Then the product instance is returned

  Scenario: Get a non existing product instance
  	Given a created product but not installed
  	| product_name  	        | version   | cm_tool   | ip		 		| hostname		| fqn				 |
	| testing_prov_no_install 	| 0.0.1     | chef      | 111.111.111.111 	| qa-test-vm	| qa-test-vm.testing |
	When I get the product instance details
	Then the HTTP response code is 404