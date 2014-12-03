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

from lettuce import step, world
from commons.fabric_utils import *
from commons.rest_utils import RestUtils
from commons.provisioning_steps import ProvisioningSteps
from commons.product_steps import ProductSteps
from commons.constants import *
from commons.configuration import CONFIG_PRODUCT_NAME_CHEF, CONFIG_PRODUCT_NAME_PUPPET, CONFIG_PRODUCT_VERSION_CHEF, \
    CONFIG_PRODUCT_VERSION_PUPPET, CONFIG_PRODUCT_NAME_CHEF_2, CONFIG_PRODUCT_NAME_PUPPET_2
from commons.configuration import CONFIG_VM_HOSTNAME
from commons.utils import response_body_to_dict, wait_for_task_finished
from nose.tools import assert_equals, assert_true, assert_in, assert_false, assert_not_in
import re
import time

api_utils = RestUtils()
provisioning_steps = ProvisioningSteps()
product_steps = ProductSteps()


@step(u'a configuration management with "(.*)"')
def configuration_management_with_group1(step, cm_tool):
    """ Set the 'installator' value to be used in the test execution: 'chef' or 'puppet' """
    world.cm_tool = cm_tool


@step(u'a node name "(.*)"')
def a_node_name_group1(step, node_name):
    """ Set the 'installator' value to be used in the test execution: 'chef' or 'puppet' """
    world.node_name = CONFIG_VM_HOSTNAME if CONFIG_FILE in node_name else node_name


@step(u'a node registered in Chef-Server and Puppet-Master')
def a_node_registered_in_the_server(step):
    """ Launch agents (chef and puppet) in the VM """
    a_node_registered_in_chef_server(step)
    a_node_registered_in_puppet_master(step)


@step(u'a node registered in Chef-Server$')
def a_node_registered_in_chef_server(step):
    """ Launch Chef agent in the VM """
    execute_chef_client()
    world.agents_running.append('chef')

    # Wait for registration
    time.sleep(5)


@step(u'a node registered in Puppet-Master$')
def a_node_registered_in_puppet_master(step):
    """ Launch Puppet agent in the VM """
    execute_puppet_agent()
    world.agents_running.append('puppet')

    # Wait for registration
    time.sleep(5)


@step(u'a virtual machine with these parameters:')
def and_a_vm_with_this_parameters(step):
    """ Set a VM configuration to be used in the test execution """
    provisioning_steps.and_a_vm_with_this_parameters(step)


@step(u'accept header value "([^"]*)"')
def accept_header_value_group1(step, accept_header):
    """ Set Accept header value to be used in requests """
    world.headers[ACCEPT_HEADER] = accept_header


@step(u'a created and installed product with name "([^"]*)" and release "([^"]*)"')
def a_created_product_with_name_group1_and_release_group2(step, product_name, product_version):
    """ Creates and installs a product. Get values from configuration file if 'CONFIG_FILE' wildcard
    value is in dataset """
    world.product_name = product_name
    world.product_version = product_version

    if product_name == CONFIG_FILE:
        world.product_name = CONFIG_PRODUCT_NAME_CHEF if (world.cm_tool is not None and world.cm_tool == 'chef') \
            else CONFIG_PRODUCT_NAME_PUPPET

    if product_version == CONFIG_FILE:
        world.product_version = CONFIG_PRODUCT_VERSION_CHEF if (world.cm_tool is not None and world.cm_tool == 'chef') \
            else CONFIG_PRODUCT_VERSION_PUPPET

    metadata_list = DEFAULT_METADATA[METADATA]
    for metadata in metadata_list:
        if metadata["key"] in "installator":
            metadata["value"] = world.cm_tool
            break

    product_steps.a_created_product_with_name_and_release_with_metadatas(step=step, product_name=world.product_name,
                                                                         product_version=world.product_version,
                                                                         metadatas=metadata_list)

    provisioning_steps.i_install_the_product_in_the_vm(step)
    provisioning_steps.task_is_created(step)
    the_task_is_performed(step)

    world.list_of_installed_products.append({"product_name":  world.product_name,
                                             "product_version":  world.product_version})


@step(u'other created and installed product with name "([^"]*)" and release "([^"]*)"')
def other_created_product_with_name_group1_and_release_group2(step, product_name, product_version):
    """ Creates and installs other product. Get values from configuration file if 'CONFIG_FILE' wildcard
    value is in dataset """
    if world.cm_tool == 'chef':
        product_name = CONFIG_PRODUCT_NAME_CHEF_2 if CONFIG_FILE in product_name else product_name
    else:
        product_name = CONFIG_PRODUCT_NAME_PUPPET_2 if CONFIG_FILE in product_name else product_name
    print
    print product_name
    print
    a_created_product_with_name_group1_and_release_group2(step, product_name, product_version)


@step(u'I remove the node')
def i_remove_the_node_group1(step):
    """ Kill all agents in VM and remove node using SDC API """
    # Kill agents in VM before deleting
    execute_chef_client_stop()
    execute_puppet_agent_stop()
    remove_chef_client_cert_file()
    remove_puppet_agent_cert_file()

    # Execute remove request
    world.response = api_utils.delete_node(world.headers, world.tenant_id, world.node_name)


@step(u'the task is created')
def the_task_is_created(step):
    """ Assertions to check if TASK is created with the expected data """
    assert_true(world.response.ok, 'RESPONSE BODY: {}'.format(world.response.content))

    response_headers = world.response.headers
    assert_equals(response_headers[CONTENT_TYPE], world.headers[ACCEPT_HEADER],
                  'RESPONSE HEADERS: {}'.format(world.response.headers))

    response_body = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER], with_attributes=True,
                                          xml_root_element_name=TASK)

    assert_in(world.node_name, response_body[DESCRIPTION])
    assert_equals(world.tenant_id, response_body[TASK_VDC])

    m = re.search('/task/(.*)$', response_body[TASK_HREF])
    world.task_id = m.group(1)


@step(u'the task is performed')
def the_task_is_performed(step):
    """ Assertions to check if TASK has finished with status SUCCESS """
    finished = wait_for_task_finished(vdc_id=world.tenant_id, task_id=world.task_id,
                                      status_to_be_finished=TASK_STATUS_VALUE_SUCCESS, headers=world.headers)
    assert_true(finished, 'Task is not in the correct status. EXPECTED STATUS: {}'.format(TASK_STATUS_VALUE_SUCCESS))


@step(u'the product is not instantiated')
def the_product_is_not_instantiated(step):
    """ Assertions to check if productInstance does not exist in SDC """
    world.instance_id = "{}_{}_{}".format(world.vm_fqn, world.product_name, world.product_version)
    response = api_utils.retrieve_product_instance(headers=world.headers, vdc_id=world.tenant_id,
                                                   product_instance_id=world.instance_id)

    assert_false(response.ok, 'RESPONSE BODY: {}'.format(world.response.content))


@step(u'all installed products are not instantiated')
def all_installed_products_are_not_instantiated(step):
    """ Assertions to check if all productInstances do not exist in SDC """
    for product in world.list_of_installed_products:
        world.product_name = product['product_name']
        world.product_version = product['product_version']
        the_product_is_not_instantiated(step)


@step(u'the node is not registered in Chef-Server')
def the_node_is_not_registered_in_chef_server(step):
    """ Assertions to check it the node is not registered in Chef-Server """
    response = get_chef_node_info_from_server(world.node_name)
    assert_equals(response, None, "Node has not been unregister from Chef-Server")


@step(u'the node is not registered in Puppet-Master')
def the_node_is_not_registered_in_puppet_master(step):
    """ Assertions to check it the node is not registered in Puppet Master (cert and puppetdb) """
    response = get_puppet_node_cert_from_server(world.node_name)
    assert_equals(response, None, "Certs have been found. Node has not been unregister from Puppet-Master")

    puppetdb_node_list_request = api_utils.retrieve_puppetdb_node_list()
    for node in puppetdb_node_list_request.json():
        assert_not_in(world.node_name, node['name'], "Node has not been unregister from Puppetdb")


@step(u'I obtain a "([^"]*)" HTTP error')
def i_obtain_a_group1_http_error(step, error_code):
    """ Assertions to check if HTTP response status has got the expected error code """
    assert_equals(str(world.response.status_code), error_code, 'RESPONSE BODY: {}'.format(world.response.content))
