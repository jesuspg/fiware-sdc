__author__ = 'arobres'

# -*- coding: utf-8 -*-
from lettuce import step, world
from commons.rest_utils import RestUtils
from commons.product_steps import ProductSteps
from commons.provisioning_steps import ProvisioningSteps
from commons.utils import wait_for_task_finished, response_body_to_dict
from commons.constants import *
from commons.configuration import CONFIG_VM_HOSTNAME, CONFIG_VM_IP, CONFIG_VM_FQN, CONFIG_PRODUCT_NAME_CHEF, CONFIG_PRODUCT_NAME_PUPPET, \
    CONFIG_PRODUCT_VERSION_CHEF, CONFIG_PRODUCT_VERSION_PUPPET
from nose.tools import assert_equals, assert_true, assert_false
from lettuce_tools.dataset_utils.dataset_utils import DatasetUtils
import time

api_utils = RestUtils()
product_steps = ProductSteps()
provisioning_steps = ProvisioningSteps()
dataset_utils = DatasetUtils()


@step(u'a configuration management with "(.*)"')
def configuration_management_with_group1(step, cm_tool):
    world.cm_tool = cm_tool


@step(u'a created product with name "([^"]*)" and release "([^"]*)"')
def a_created_product_with_name_group1(step, product_id, product_release):

    world.product_name = product_id
    world.product_description = "QA Test"
    world.product_version = product_release

    if product_id == CONFIG_FILE:
        world.product_name = CONFIG_PRODUCT_NAME_CHEF if (world.cm_tool is not None and world.cm_tool == 'chef') \
            else CONFIG_PRODUCT_NAME_PUPPET

    if product_release == CONFIG_FILE:
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


@step(u'a non existent product with name "([^"]*)"')
def given_a_not_existent_product_with_name_group1(step, product_name):
    world.product_name = product_name
    world.product_version = '1.0.0'


@step(u'a existent product with name "([^"]*)" and no product release')
def given_a_existent_product_with_name_group1_and_no_product_release(step, product_name):

    world.product_name = product_name
    world.product_version = '50.0.0'

    product_steps.a_created_product_with_name(step, product_name=product_name)


@step(u'a virtual machine with these parameters:')
def and_a_vm_with_this_parameters(step):
    provisioning_steps.and_a_vm_with_this_parameters(step)


@step(u'a VM with hostname "([^"]*)"')
def a_vm_with_hostname_group1(step, hostname):
    world.vm_hostname = CONFIG_VM_HOSTNAME if CONFIG_FILE == hostname else hostname


@step(u'a VM with hostname "([^"]*)" and ip "([^"]*)"')
def a_vm_with_hostname_group1_and_ip_group2(step, hostname, ip):
    a_vm_with_hostname_group1(step, hostname)
    world.vm_ip = CONFIG_VM_IP if CONFIG_FILE == ip else ip


@step(u'a VM with fqn "([^"]*)"')
def a_vm_with_fqn_group1(step, fqn):
    world.vm_fqn = CONFIG_VM_FQN if CONFIG_FILE == fqn else fqn


@step(u'a VM with hostname "([^"]*)" and fqn "([^"]*)"')
def a_vm_with_hostname_group1_and_fqn_group2(step, vm_hostname, vm_fqn):
    a_vm_with_hostname_group1(step, vm_hostname)
    a_vm_with_fqn_group1(step, vm_fqn)


@step(u'the following instance attributes:')
def the_following_instance_attributes(step):
    world.instance_attributes = []
    for row in step.hashes:
        row = dict(dataset_utils.prepare_data(row))
        world.instance_attributes.append(row)


@step(u'the following product attributes:')
def the_following_product_attributes(step):
    world.attributes = []
    for row in step.hashes:
        row = dict(dataset_utils.prepare_data(row))
        world.attributes.append(row)


@step(u'content type header values:')
def content_type_header_values(step):
    world.headers[CONTENT_TYPE] = step.hashes[0]["content_type"]
    world.headers[ACCEPT_HEADER] = step.hashes[0]["accept"]


@step(u'And the accept header "([^"]*)"')
def and_the_accept_header_group1(step, accept_content):
    world.headers[ACCEPT_HEADER] = accept_content


@step(u'And incorrect "([^"]*)" authentication')
def incorrect_token(step, new_token):
    world.headers[AUTH_TOKEN_HEADER] = new_token


@step(u'I install the product in the VM')
def i_install_the_product_in_the_vm(step):
    provisioning_steps.i_install_the_product_in_the_vm(step)


@step(u'I try to install the product with empty params "(.*)"')
def i_try_to_install_the_product_with_empty_param_group1(step, empty_params):
    world.product_name = "" if "product_name" in empty_params else world.product_name
    world.product_version = "" if "release" in empty_params else world.product_version
    world.vm_hostname = "" if "hostname" in empty_params else world.vm_hostname
    world.vm_fqn = "" if "fqn" in empty_params else world.vm_fqn
    world.vm_ip = "" if "ip" in empty_params else world.vm_ip
    world.vm_ostype = "" if "ostype" in empty_params else world.vm_ostype

    i_install_the_product_in_the_vm(step)


@step(u'the task is created')
def task_is_created(step):
    provisioning_steps.task_is_created(step)


@step(u'the task has finished with status "(RUNNING|SUCCESS|ERROR)"$')
def the_task_has_finished_with_status_group1(step, status):
    finished = wait_for_task_finished(vdc_id=world.tenant_id, task_id=world.task_id,
                                      status_to_be_finished=status, headers=world.headers)
    assert_true(finished, 'Task is not in the correct status. Expected: {}'.format(status))


@step(u'the task has the minor error code "(.*)"')
def the_task_has_the_minor_error_code_group1(step, error_minor_code):

    response = api_utils.retrieve_task(headers=world.headers, vdc_id=world.tenant_id, task_id=world.task_id)
    assert_true(response.ok, 'RESPONSE: {}'.format(response.content))

    world.task_response_body = response_body_to_dict(response, world.headers[ACCEPT_HEADER], with_attributes=True,
                                                     xml_root_element_name=TASK)

    assert_equals(world.task_response_body[TASK_ERROR][TASK_ERROR_MINOR_CODE], error_minor_code)


@step(u'the task is not created')
def task_is_not_created(step):
    assert_false(world.response.ok, 'RESPONSE: {}'.format(world.response.content))


@step(u'the product is instantiated')
def the_product_is_instantiated(step):
    # Wait for product instance (5s).
    time.sleep(5)
    
    provisioning_steps.the_product_is_instantiated(step)


@step(u'the product is not instantiated')
def the_product_is_not_instantiated(step):
    world.instance_id = "{}_{}_{}".format(world.vm_fqn, world.product_name, world.product_version)
    response = api_utils.retrieve_product_instance(headers=world.headers, vdc_id=world.tenant_id,
                                                   product_instance_id=world.instance_id)

    assert_false(response.ok, 'RESPONSE: {}'.format(world.response.content))


@step(u'the product installation status is "(ERROR|INSTALLED|UNINSTALLED)"')
def the_product_installation_status_is(step, status):
    provisioning_steps.the_product_installation_status_is(step, status)


@step(u'the product has the correct attributes in the catalog')
def the_product_has_the_correct_attributes_in_the_catalog(step):

    response = api_utils.retrieve_product(headers=world.headers, product_id=world.product_name)
    assert_true(response.ok, 'RESPONSE: {}'.format(response.content))


    response_body = response_body_to_dict(response, world.headers[ACCEPT_HEADER], xml_root_element_name=PRODUCT)
    assert_equals(response_body[PRODUCT_NAME], world.product_name)
    assert_equals(response_body[PRODUCT_DESCRIPTION], world.product_description)

    if len(world.attributes) == 1:
        world.attributes = world.attributes[0]
    assert_equals(world.attributes, response_body[PRODUCT_ATTRIBUTES])


@step(u'I obtain an "([^"]*)"')
def i_obtain_an_group1(step, error_code):
    assert_equals(str(world.response.status_code), error_code, world.response.content)

