__author__ = 'arobres'

# -*- coding: utf-8 -*-
from lettuce import step, world
from commons.rest_utils import RestUtils
from commons.product_steps import ProductSteps
from commons.provisioning_steps import ProvisioningSteps
from commons.constants import *
from nose.tools import assert_equals
from commons.configuration import *


api_utils = RestUtils()
product_steps = ProductSteps()
provisioning_steps = ProvisioningSteps()


def _assign_product_data(product_name, product_release):
    world.product_name = product_name
    world.product_version = product_release

    if product_name == CONFIG_FILE:
        world.product_name = CONFIG_PRODUCT_NAME_CHEF if (world.cm_tool is not None and world.cm_tool == 'chef') \
            else CONFIG_PRODUCT_NAME_PUPPET

    if product_release == CONFIG_FILE:
        world.product_version = CONFIG_PRODUCT_VERSION_CHEF if (world.cm_tool is not None and world.cm_tool == 'chef') \
            else CONFIG_PRODUCT_VERSION_PUPPET


@step(u'a configuration management with "(.*)"')
def configuration_management_with_group1(step, cm_tool):
    world.cm_tool = cm_tool


@step(u'a virtual machine with these parameters:')
def and_a_vm_with_this_parameters(step):
    provisioning_steps.and_a_vm_with_this_parameters(step)


@step(u'a created product with name "([^"]*)" and release "([^"]*)"')
def a_created_product_with_name_group1(step, product_id, product_release):
    _assign_product_data(product_id, product_release)
    metadata_list = DEFAULT_METADATA[METADATA]
    for metadata in metadata_list:
        if metadata["key"] in "installator":
            metadata["value"] = world.cm_tool
            break

    product_steps.a_created_product_with_name_and_release_with_metadatas(step=step, product_name=world.product_name,
                                                                         product_version=world.product_version,
                                                                         metadatas=metadata_list)


@step(u'a installed product with name "([^"]*)" and release "([^"]*)"')
def a_installed_product_with_name_group1_and_release_group2(step, product_name, product_version):
    a_created_product_with_name_group1(step, product_name, product_version)
    provisioning_steps.i_install_the_product_in_the_vm(step)


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


@step(u'I uninstall the installed product "([^"]*)" and release "([^"]*)"')
def i_uninstall_the_installed_product_group1_and_release_group2(step, product_name, product_release):
    _assign_product_data(product_name, product_release)
    provisioning_steps.i_uninstall_a_installed_product_and_release(step)


@step(u'the task is created')
def task_is_created(step):
    provisioning_steps.task_is_created(step)


@step(u'the task has finished with status "(RUNNING|SUCCESS|ERROR)"')
def the_task_has_finished_with_status_group1(step, status):
    provisioning_steps.the_task_has_finished_with_status_group1(step, status)


@step(u'the product is instantiated')
def the_product_is_instantiated(step):
    provisioning_steps.the_product_is_instantiated(step)


@step(u'I obtain an "([^"]*)"')
def i_obtain_an_group1(step, error_code):
    assert_equals(str(world.response.status_code), error_code, world.response.content)









