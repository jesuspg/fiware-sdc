__author__ = 'jfernandez'

from commons.terrain_steps import *
from commons.provisioning_steps import ProvisioningSteps
from commons.rest_utils import RestUtils
from commons.configuration import CONFIG_VM_HOSTNAME
from commons.fabric_utils import execute_chef_client, execute_puppet_agent, remove_chef_client_cert_file, \
    execute_chef_client_stop, execute_puppet_agent_stop, remove_puppet_agent_cert_file

provisioning_steps = ProvisioningSteps()
rest_utils = RestUtils()


@before.each_feature
def before_each_feature(feature):
    """
    Hook: Will be executed before each feature. Configures global vars and gets token from keystone.
    Launch agents (puppet and chef) in the target VM
    """
    setup_feature(feature)
    execute_chef_client()
    execute_puppet_agent()


@before.each_scenario
def before_each_scenario(scenario):
    """ hook: Will be executed before each Scenario. Setup Scenario and initialize World vars """
    setup_scenario(scenario)


@before.outline
def before_outline(param1, param2, param3, param4):
    """ Hook: Will be executed before each Scenario Outline. Same behaviour as 'before_each_scenario'"""
    setup_outline(param1, param2, param3, param4)


@after.all
def after_all(scenario):
    """
    Hook: Will be executed after all Scenarios and Features.
    Removes Feature data and cleans the system. Kills all agents running in the VM.
    """
    tear_down(scenario)
    rest_utils.delete_node(world.headers, world.tenant_id, CONFIG_VM_HOSTNAME)
    execute_chef_client_stop()
    execute_puppet_agent_stop()
    remove_chef_client_cert_file()
    remove_puppet_agent_cert_file()
