# -*- coding: utf-8 -*-
Feature: Uninstall product
    As a fi-ware user
    I want to be able to uninstall a product release in a Virtual Machine
    so that I can delete it


    Scenario Outline: Uninstall a installed product release
      Given a installed product with name "<product_name>" and release "<product_release>" in the hostname "<hostname>"
      And the accept header "<accept>"
      When I uninstall the installed product "<product_name>" and release "<product_release>"
      Then the product "<product_name>" and release "<product_release>" is uninstalled

      Examples:
      |  product_name | product_release | hostname  | accept          |
      | test_module   | 1.0.0           | testing   | application/json|
      | test_module   | 2.0.0           | testing   | application/xml |

    Scenario Outline: Uninstall a product with several releases installed
      Given a installed product with name "<product_name>" and release "<product_release>" in the hostname "<hostname>"
      And a installed product with name "<product_name>" and release "<product_release_2>" in the hostname "<hostname>"
      And the accept header "<accept>"
      When I uninstall the installed product "<product_name>" and release "<product_release>"
      Then the product "<product_name>" and release "<product_release>" is uninstalled
      Then the product "<product_name>" and release "<product_release_2>" remains installed

      Examples:
      |  product_name | product_release | hostname  | accept          | product_release_2 |
      | test_module   | 1.0.0           | testing   | application/json| 10.0.0            |
      | test_module   | 2.0.0           | testing   | application/xml | 20.0.0            |


    Scenario Outline: Uninstall a not existant product release

      Given a installed product with name "<product_name>" and release "<product_release>" in the hostname "<hostname>"
      And the accept header "<accept>"
      When I uninstall the installed product "<product_name>" and release "<another_product_release>"
      Then I obtain an "<Error_code>"

      Examples:
      |  product_name | product_release | hostname  | accept          | another_product_release | Error_code  |
      | test_module   | 1.0.0           | testing   | application/json| testing                 | 404         |
      | test_module   | 2.0.0           | testing   | application/xml | 0.0.0                   | 404         |

    Scenario Outline: Uninstall a not existent product

      Given a installed product with name "<product_name>" and release "<product_release>" in the hostname "<hostname>"
      And the accept header "<accept>"
      When I uninstall the installed product "<another_product_name>" and release "<product_release>"
      Then I obtain an "<Error_code>"

      Examples:
      |  product_name | product_release | hostname  | accept          | another_product_name  | Error_code  |
      | test_module   | 1.0.0           | testing   | application/json| testing               | 404         |
      | test_module   | 2.0.0           | testing   | application/xml | qa                    | 404         |


    Scenario Outline: Uninstall a product uninstalled before

      Given a uninstalled product with name "<product_name>" and release "<product_release>" in the hostname "<hostname>"
      And the accept header "<accept>"
      When I uninstall the installed product "<product_name>" and release "<product_release>"
      Then I receive an error in the result

      Examples:
      |  product_name | product_release | hostname  | accept          |
      | test_module   | 1.0.0           | testing   | application/json|
      | test_module   | 2.0.0           | testing   | application/xml |

    Scenario Outline: Uninstall a product with incorrect headers
      Given a installed product with name "<product_name>" and release "<product_release>" in the hostname "<hostname>"
      And the accept header "<accept>"
      When I uninstall the installed product "<product_name>" and release "<product_release>"
      Then I obtain an "<Error_code>"

      Examples:
      |  product_name | product_release | hostname  | accept  | Error_code  |
      | test_module   | 1.0.0           | testing   | json    | 406         |
      | test_module   | 2.0.0           | testing   | testing | 406         |


    Scenario Outline: Uninstall a product with incorrect token

      Given a installed product with name "<product_name>" and release "<product_release>" in the hostname "<hostname>"
      And the accept header "<accept>"
      And incorrect "<token>" authentication
      When I uninstall the installed product "<product_name>" and release "<product_release>"
      Then I obtain an "<Error_code>"

       Examples:
      |  product_name | product_release | hostname  | accept          | Error_code  | token                             |
      | test_module   | 1.0.0           | testing   | application/json| 401         | 891855f21b2f1567afb966d3ceee1295  |
      | test_module   | 2.0.0           | testing   | application/xml | 401         | hello world                       |
      | test_module   | 3.0.0           | testing   | application/xml | 401         |                                   |

