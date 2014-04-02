# -*- coding: utf-8 -*-
Feature: Install a product in a VM
    As a fi-ware user
    I want to be able to install a  product in a VM
    so that they become more functional and useful

    @happy_path
    Scenario Outline: install a  product in a VM with xml content
       Given I add a new product "product_new_0001" and release "1.2.3" with "metadata_installator" and "<installator>" in the catalog with "<content>" content in the response
        When Install a product "product_new_0001" with release "1.2.3" in a VM "130.206.81.135" with hostname "testingIvanQA-ivan0001tier-1" and OS "94" with "xml" content
        Then I receive an "OK" response with a "install Product XML" with one item
    Examples:
      |content|instalator|
      |xml|puppet|


