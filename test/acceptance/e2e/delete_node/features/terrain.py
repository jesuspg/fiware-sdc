__author__ = 'jfernandez'

from commons.terrain_steps import *
from commons.provisioning_steps import ProvisioningSteps
from commons.rest_utils import RestUtils
from commons.fabric_utils import execute_chef_client_stop, execute_puppet_agent_stop, \
    remove_chef_client_cert_file, remove_puppet_agent_cert_file

provisioning_steps = ProvisioningSteps()
rest_utils = RestUtils()


@before.each_feature
def before_each_feature(feature):
    """ Hook: Will be executed before each feature. Configures global vars and gets token from keystone """
    setup_feature(feature)


@before.each_scenario
def before_each_scenario(scenario):
    """ Hook: Will be executed before each Scenario. Setup Scenario and initialize World vars """
    setup_scenario(scenario)
    world.agents_running = list()
    world.list_of_installed_products = list()


@after.each_scenario
def after_each_scenario(scenario):
    """
    Hook: Will be executed after each Scenario.
    Removes Test data and cleans the system. Kills all agents running in the VM
    """
    if world.node_name is not None:
        rest_utils.delete_node(world.headers, world.tenant_id, world.node_name)
        execute_chef_client_stop()
        execute_puppet_agent_stop()
        remove_chef_client_cert_file()
        remove_puppet_agent_cert_file()


@before.outline
def before_outline(param1, param2, param3, param4):
    """ Hook: Will be executed before each Scenario Outline. Same behaviour as 'before_each_scenario'"""
    setup_outline(param1, param2, param3, param4)


@after.all
def after_all(scenario):
    """ Hook: Will be executed after all Scenarios and Features. Removes Feature data and cleans the system  """
    tear_down(scenario)
