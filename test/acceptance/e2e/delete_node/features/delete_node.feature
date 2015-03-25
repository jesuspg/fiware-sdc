# -*- coding: utf-8 -*-
Feature: Remove nodes from chef/puppet servers with E2E validations
    As a fi-ware user
    I want to be able to remove nodes from chef/puppet server
    so that I can reuse these node names in my vm


    @happy_path
    Scenario: Remove node, that has been installed using Chef (registered in Chef-Server and Puppet-Master)

      Given a configuration management with "chef"
      And a node name "${CONFIG_FILE}"
      And a node registered in Chef-Server and Puppet-Master
      And a virtual machine with these parameters:
	  | fqn            | hostname        | ip              |
	  | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      And a created and installed product with name "${CONFIG_FILE}" and release "${CONFIG_FILE}"
      When I remove the node
      Then the task is created
      And the task is performed
      And the product is not instantiated
      And the node is not registered in Chef-Server


    @happy_path
    Scenario: Remove node, that has been installed using Puppet (registered in Chef-Server and Puppet-Master)

      Given a configuration management with "puppet"
      And a node name "${CONFIG_FILE}"
      And a node registered in Chef-Server and Puppet-Master
      And a virtual machine with these parameters:
	  | fqn            | hostname        | ip              |
	  | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      And a created and installed product with name "${CONFIG_FILE}" and release "${CONFIG_FILE}"
      When I remove the node
      Then the task is created
      And the task is performed
      And the product is not instantiated
      And the node is not registered in Puppet-Master


    Scenario: Remove node from Chef Server and Puppet Master without product instances

      Given a node name "${CONFIG_FILE}"
      And a node registered in Chef-Server and Puppet-Master
      When I remove the node
      Then the task is created
      And the task is performed
      And the node is not registered in Chef-Server
      And the node is not registered in Puppet-Master


    Scenario Outline: Remove node from Chef Server and Puppet Master without product instances using different representations

      Given a node name "${CONFIG_FILE}"
      And a node registered in Chef-Server and Puppet-Master
      And accept header value "<accept_header>"
      When I remove the node
      Then the task is created
      And the task is performed
      And the node is not registered in Chef-Server
      And the node is not registered in Puppet-Master

    Examples:
    | accept_header     |
    | application/xml   |
    | application/json  |


    Scenario: Remove node only registered in Chef-Server with product instances

      Given a configuration management with "chef"
      And a node name "${CONFIG_FILE}"
      And a node registered in Chef-Server
      And a virtual machine with these parameters:
	  | fqn            | hostname        | ip              |
	  | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      And a created and installed product with name "${CONFIG_FILE}" and release "${CONFIG_FILE}"
      When I remove the node
      Then the task is created
      And the task is performed
      And the product is not instantiated
      And the node is not registered in Chef-Server


    Scenario: Remove node only registered in Puppet-Master with product instances

      Given a configuration management with "puppet"
      And a node name "${CONFIG_FILE}"
      And a node registered in Puppet-Master
      And a virtual machine with these parameters:
	  | fqn            | hostname        | ip              |
	  | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      And a created and installed product with name "${CONFIG_FILE}" and release "${CONFIG_FILE}"
      When I remove the node
      Then the task is created
      And the task is performed
      And the product is not instantiated
      And the node is not registered in Puppet-Master


    Scenario Outline: Remove node from Chef Server and Puppet Master with more than one product instance

      Given a configuration management with "<cm_tool>"
      And a node name "${CONFIG_FILE}"
      And a node registered in Chef-Server and Puppet-Master
      And a virtual machine with these parameters:
	  | fqn            | hostname        | ip              |
	  | ${CONFIG_FILE} | ${CONFIG_FILE}  | ${CONFIG_FILE}  |
      And a created and installed product with name "${CONFIG_FILE}" and release "${CONFIG_FILE}"
      And other created and installed product with name "${CONFIG_FILE}" and release "${CONFIG_FILE}"
      When I remove the node
      Then the task is created
      And the task is performed
      And all installed products are not instantiated
      And the node is not registered in Chef-Server
      And the node is not registered in Puppet-Master

      Examples:

      | cm_tool |
      | chef    |
      | puppet  |


    Scenario: Remove non-existent node

      Given a configuration management with "<cm_tool>"
      And a node name "nonexistent_node"
      When I remove the node
      Then I obtain a "404" HTTP error
