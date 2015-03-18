# -*- coding: utf-8 -*-
Feature: Uninstall product with E2E validations
    As a fi-ware user
    I want to be able to uninstall a product release in a Virtual Machine
    so that I can delete it

    @happy_path
    Scenario Outline: Uninstall a installed product release

      Given a configuration management with "<cm_tool>"
      And a virtual machine with these parameters:
      | fqn            | hostname        | ip              |
      | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      And a successful installed product with name "<product_name>" and release "<product_release>"
      When I uninstall the installed product "<product_name>" and release "<product_release>"
      Then the task is created
      And the task is performed
      And the product installation status is "UNINSTALLED"
      And the product is uninstalled

      Examples:

      | product_name              | product_release | cm_tool |
      | qa-test-product-chef-01   | 1.2.3           | chef    |
      | qa-test-product-puppet-01 | 1.2.3           | puppet  |


    Scenario Outline: Uninstall a product with several releases installed

      Given a configuration management with "<cm_tool>"
      And a virtual machine with these parameters:
      | fqn            | hostname        | ip              |
      | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      And a successful installed product with name "<product_name>" and release "<product_release>"
      And a successful installed product with name "<product_name>" and release "<other_product_release>"
      When I uninstall the installed product "<product_name>" and release "<product_release>"
      Then the task is created
      And the task is performed
      And the product "<product_name>" and release "<product_release>" is uninstalled
      And the product "<product_name>" and release "<other_product_release>" remains installed

      Examples:

      | product_name              | product_release | other_product_release | cm_tool |
      | qa-test-product-chef-01   | 1.2.3           | 1.2.4                 | chef    |
      | qa-test-product-puppet-01 | 1.2.3           | 1.2.4                 | puppet  |


    Scenario: Uninstall a product uninstalled before

      Given a virtual machine with these parameters:
      | fqn            | hostname        | ip              |
      | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      Given a uninstalled product with name "${CONFIG_FILE}" and release "${CONFIG_FILE}"
      When I uninstall the installed product "${CONFIG_FILE}" and release "${CONFIG_FILE}"
      Then the task is created
      And the task has finished with status "ERROR"
