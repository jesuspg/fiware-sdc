=================
Sagitta Test Plan
=================

This section describes the Test Plan designed for the new Software Deployment and Configuration features to test its functionality.

Test Plan for previous features can be found in the `Fiware Wiki <https://forge.fi-ware.org/plugins/mediawiki/wiki/fiware/index.php/Software_Deployment_and_Configuration_-_Unit_Testing_Plan>`_

Release 4.1
---------------------------

The features involved in this release are:

+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Features                                                                                                                                                                                      |
+===============================================================================================================================================================================================+
| `FIWARE.Feature.Cloud.SwDeployConfig.GEIdentification <http://forge.fi-ware.org/plugins/mediawiki/wiki/fiware/index.php/FIWARE.Feature.Cloud.SwDeployConfig.GEIdentification>`_               |
| `FIWARE.Feature.Cloud.SwDeployConfig.ConfigurationManagement <http://forge.fi-ware.org/plugins/mediawiki/wiki/fiware/index.php/FIWARE.Feature.Cloud.SwDeployConfig.ConfigurationManagement>`_ |
+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+

**Component Test Cases**

You can execute component test cases for this release executing this command: ::

  vagrant@precise32:/vagrant$  lettuce_tools -ft add_product.feature,install_product.feature -ts comp --tags=release_4_1

Test results: ::

	
	1 feature (1 passed)
	22 scenarios (22 passed)
	88 steps (88 passed)

	1 feature (1 passed)
	7 scenarios (7 passed)
	52 steps (52 passed)

**E2E Test Cases**

You can execute E2E test cases for this release executing this command: ::

  vagrant@precise32:/vagrant$  lettuce_tools -ft install_product.feature -ts e2e --tags=release_4_1

Test results: ::

	1 feature (1 passed)
	6 scenarios (6 passed)
	54 steps (54 passed)
----------------------------

**FIWARE.Feature.Cloud.SwDeployConfig.GEIdentification** ::

   The Sagitta product catalogue should identify those products which corresponds to GEs in FIWARE.
   This can be done by the introduction of a metadata, called NID, which identify the FIWARE GE 
   to be used.

Test Cases involved in this feature should validate that Sagitta can add product to the catalog using custom metadata values.
They are defined in `component/add_product_features/add_product.feature <../acceptance/component/add_product/features/add_product.feature>`_:

- Scenario Outline: Add new product with one metadata
- Scenario Outline: Add new product with several metadatas


**FIWARE.Feature.Cloud.SwDeployConfig.ConfigurationManagement** ::

   The software in the product catalogue should have a way to store their attribute configurations. 
   It can be an attribute entity which also incorporates the type of attribute for complex 
   configuration.

Test Cases involved in this feature should validate that Sagitta can manage attribute types for products.
They are defined in:

> `component/add_product/features/add_product.feature <../acceptance/component/add_product/features/add_product.feature>`_:

- Scenario Outline: Add new product with valid attribute 'type'
- Scenario Outline: Add new product with invalid value for attribute 'type'

> `component/install_product_features/install_product.feature <../acceptance/component/install_product/features/install_product.feature>`_:

- Scenario Outline: Install a new product with instance attributes (with valid type)
- Scenario Outline: Install a new product with invalid type for instance attributes
- Scenario Outline: Install a new product with instance attributes: missing type

> `e2e/install_product/install_product.feature <../acceptance/e2e/install_product/install_product.feature>`_:

- Scenario Outline: Install a new product with "installation attributes" (Plain and IP types)
- Scenario Outline: Install a new product with "installation attributes" (IPALL type, single IP)
- Scenario Outline: Install a new product with "installation attributes" (IPALL type, IP list)
