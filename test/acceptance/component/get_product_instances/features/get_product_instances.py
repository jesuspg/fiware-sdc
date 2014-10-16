__author__ = 'arobres, jfernandez'

# -*- coding: utf-8 -*-
from lettuce import step, world
from commons.rest_utils import RestUtils
from commons.product_steps import ProductSteps
from commons.provisioning_steps import ProvisioningSteps
from commons.constants import *
from commons.utils import response_body_to_dict, generate_product_instance_id
from nose.tools import assert_equals, assert_true

api_utils = RestUtils()
product_steps = ProductSteps()
provisioning_steps = ProvisioningSteps()


@step(u'a created product but not installed')
def a_created_product_but_not_installed(step):
    world.product_name = step.hashes[0]['product_name']
    world.product_version = step.hashes[0]['version']
    world.vm_ip = step.hashes[0]['ip']
    world.vm_hostname = step.hashes[0]['hostname']
    world.vm_fqn = step.hashes[0]['fqn']
    world.cm_tool = step.hashes[0]['cm_tool']

    product_steps.a_created_product_with_name_and_release(step, world.product_name, world.product_version)


@step(u'a created and installed product')
def a_created_and_installed_product(step):
    a_created_product_but_not_installed(step)

    provisioning_steps.i_install_the_product_in_the_vm(step)


@step(u'I get all product instances')
def i_get_all_product_instances(step):
    world.response = api_utils.retrieve_product_instance_list(headers=world.headers, vdc_id=world.tenant_id)


@step(u'the product instance is in the returned list')
def the_product_instance_is_returned_in_the_list(step):
    instance_id = generate_product_instance_id(world.vm_fqn, world.product_name, world.product_version)
    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response))
    
    #response_body = xml_to_dict(world.response.content)[PRODUCT_INSTANCE_LIST][PRODUCT_INSTANCE_RES]
    response_body = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER],
                                          xml_root_element_name=PRODUCT_INSTANCE_LIST)
    response_body = response_body
    assert_true(len(response_body) != 0)
    
    product_instance = None
    for product_ins in response_body:
        if instance_id in product_ins[PRODUCT_INSTANCE_NAME]:
            product_instance = product_ins
            break

    assert_true(product_instance is not None)
    assert_equals(product_instance[PRODUCT_INSTANCE_NAME], instance_id)
    assert_true(product_instance[PRODUCT_INSTANCE_STATUS] != "")
    assert_equals(product_instance[PRODUCT_INSTANCE_VM][PRODUCT_INSTANCE_VM_IP], world.vm_ip)
    assert_equals(product_instance[PRODUCT_INSTANCE_VM][PRODUCT_INSTANCE_VM_FQN], world.vm_fqn)
    assert_equals(product_instance[PRODUCT_INSTANCE_VM][PRODUCT_INSTANCE_VM_HOSTNAME], world.vm_hostname)
    
    assert_equals(product_instance[PRODUCT_RELEASE][VERSION], world.product_version)
    assert_equals(product_instance[PRODUCT_RELEASE][PRODUCT][PRODUCT_NAME], world.product_name)

@step(u'I get the product instance details')
def i_get_the_product_instance_details(step):
    world.instance_id = generate_product_instance_id(world.vm_fqn, world.product_name, world.product_version)
    world.response = api_utils.retrieve_product_instance(headers=world.headers, vdc_id=world.tenant_id,
                                                         product_instance_id=world.instance_id)

@step(u'the product instance is returned')
def the_product_instance_is_returned(step):
    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response))

    #response_body = xml_to_dict(world.response.content)[PRODUCT_INSTANCE_RES]
    response_body = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER],
                                          xml_root_element_name=PRODUCT_INSTANCE_RES)

    assert_equals(response_body[PRODUCT_INSTANCE_NAME], world.instance_id)
    assert_true(response_body[PRODUCT_INSTANCE_STATUS] != "")
    assert_equals(response_body[PRODUCT_INSTANCE_VM][PRODUCT_INSTANCE_VM_IP], world.vm_ip)
    assert_equals(response_body[PRODUCT_INSTANCE_VM][PRODUCT_INSTANCE_VM_FQN], world.vm_fqn)
    assert_equals(response_body[PRODUCT_INSTANCE_VM][PRODUCT_INSTANCE_VM_HOSTNAME], world.vm_hostname)

    assert_equals(response_body[PRODUCT_RELEASE][VERSION], world.product_version)
    assert_equals(response_body[PRODUCT_RELEASE][PRODUCT][PRODUCT_NAME], world.product_name)


@step(u'the HTTP response code is (.*)')
def the_http_response_code_is_group1(step, http_status_code):
    assert_equals(world.response.status_code, int(http_status_code))
