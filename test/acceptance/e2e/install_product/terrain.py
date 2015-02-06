# -*- coding: utf-8 -*-
# Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U
#
# This file is part of FI-WARE project.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
#
# You may obtain a copy of the License at:
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#
# See the License for the specific language governing permissions and
# limitations under the License.
#
# For those usages not covered by the Apache version 2.0 License please
# contact with opensource@tid.es

__author__ = 'jfernandez'

from lettuce import world, before, after
from commons.terrain_steps import setup_feature, setup_scenario, setup_outline, tear_down
from commons.provisioning_steps import ProvisioningSteps
from commons.rest_utils import RestUtils
from commons.configuration import CONFIG_VM_HOSTNAME
from commons.fabric_utils import execute_chef_client, execute_puppet_agent, remove_chef_client_cert_file, \
    remove_puppet_agent_cert_file, execute_chef_client_stop, execute_puppet_agent_stop, \
    remove_all_generated_test_files, remove_puppet_agent_catalog

provisioning_steps = ProvisioningSteps()
rest_utils = RestUtils()


@before.each_feature
def before_each_feature(feature):
    """
    Hook: Will be executed before each feature. Configures global vars and gets token from keystone.
    Launch agents (puppet and chef) in the target VM
    """
    setup_feature(feature)


@before.each_scenario
def before_each_scenario(scenario):
    """
    Hook: Will be executed before each Scenario.
    Setup Scenario: initialize World vars and launch agents (puppet and chef) in the target VM
    """
    setup_scenario(scenario)
    execute_chef_client()
    execute_puppet_agent()


@before.outline
def before_outline(param1, param2, param3, param4):
    """ Hook: Will be executed before each Scenario Outline. Same behaviour as 'before_each_scenario'"""
    setup_outline(param1, param2, param3, param4)
    remove_all_generated_test_files()
    remove_puppet_agent_catalog()


@after.each_scenario
def after_each_scenario(scenario):
    """
    Hook: Will be executed after all each scenario
    Removes Feature data and cleans the system. Kills all agents running in the VM.
    """
    execute_chef_client_stop()
    execute_puppet_agent_stop()
    remove_chef_client_cert_file()
    remove_puppet_agent_cert_file()
    remove_all_generated_test_files()
    remove_puppet_agent_catalog()
    rest_utils.delete_node(world.headers, world.tenant_id, CONFIG_VM_HOSTNAME)


@after.all
def after_all(scenario):
    """
    Hook: Will be executed after all each scenario
    Removes Feature data and cleans the system. Kills all agents running in the VM.
    """
    after_each_scenario(scenario)
    tear_down(scenario)
