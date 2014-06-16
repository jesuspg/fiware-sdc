__author__ = 'arobres'

# -*- coding: utf-8 -*-
from lettuce import step, world, before, after
from commons.authentication import get_token
from commons.rest_utils import RestUtils
from commons.product_body import default_product, create_product_release
from commons.utils import dict_to_xml, set_default_headers, wait_for_task_finished, wait_for_software_installed, \
    get_installation_response
from commons.constants import PRODUCT_NAME, ACCEPT_HEADER, AUTH_TOKEN_HEADER, KEY, VALUE, INSTALLATOR, \
    INSTALLATOR_VALUE, RUNNING, SUCCESS, ERROR, TASK_URL
from nose.tools import assert_equals, assert_true
from commons.installation_body import simple_installation_body


api_utils = RestUtils()

@before.each_feature
def setup_feature(feature):

    world.token_id, world.tenant_id = get_token()


@before.each_scenario
def setup_scenario(scenario):

    world.headers = set_default_headers(world.token_id, world.tenant_id)
    api_utils.delete_all_testing_products(world.headers)
    world.attributes = None
    world.metadatas = None
    world.fqn = ''


@step(u'a created product with name "([^"]*)" and release "([^"]*)"')
def given_a_created_product_with_name_group1(step, product_id, product_release):

    world.product_name = product_id
    world.product_version = product_release
    world.file_name = '{}_{}'.format(product_id, product_release)

    metadata = {KEY: INSTALLATOR, VALUE: INSTALLATOR_VALUE[0]}
    body = dict_to_xml(default_product(name=product_id, metadata=metadata))
    response = api_utils.add_new_product(headers=world.headers, body=body)
    assert_true(response.ok, response.content)
    world.product_id = response.json()[PRODUCT_NAME]
    body = dict_to_xml(create_product_release(version=product_release))
    response = api_utils.add_product_release(headers=world.headers, body=body, product_id=product_id)
    assert_true(response.ok, response.content)


@step(u'Given a not existent product with name "([^"]*)"')
def given_a_not_existent_product_with_name_group1(step, product_name):
    world.product_name = product_name
    world.product_version = '1.0.0'


@step(u'Given a existent product with name "([^"]*)" and no product release')
def given_a_existent_product_with_name_group1_and_no_product_release(step, product_name):

    world.product_name = product_name
    world.product_version = '50.0.0'
    metadata = {KEY: INSTALLATOR, VALUE: INSTALLATOR_VALUE[0]}
    body = dict_to_xml(default_product(name=product_name, metadata=metadata))
    response = api_utils.add_new_product(headers=world.headers, body=body)
    assert_true(response.ok, response.content)

@step(u'a installed product with name "([^"]*)" and release "([^"]*)" in the hostname "([^"]*)"')
def installed_product(step, product_name, product_version, hostname):

    world.product_name = product_name
    world.product_version = product_version
    metadata = {KEY: INSTALLATOR, VALUE: INSTALLATOR_VALUE[0]}
    body = dict_to_xml(default_product(name=product_name, metadata=metadata))
    response = api_utils.add_new_product(headers=world.headers, body=body)
    assert_true(response.ok, response.content)
    world.product_id = response.json()[PRODUCT_NAME]
    body = dict_to_xml(create_product_release(version=product_version))
    response = api_utils.add_product_release(headers=world.headers, body=body, product_id=product_name)
    assert_true(response.ok, response.content)
    body = dict_to_xml(simple_installation_body(product_name=product_name, product_version=product_version,
                                                hostname=hostname))
    request = api_utils.install_product(headers=world.headers, vdc_id=world.tenant_id, body=body)
    assert_true(request.ok, request.content)
    obtained_href, obtained_status, obtained_vdc = get_installation_response(request)

    assert_equals(obtained_status, RUNNING)
    assert_equals(obtained_vdc, world.tenant_id)
    assert_true(wait_for_task_finished(headers=world.headers, url=obtained_href, status_to_be_finished=SUCCESS),
                'During precondition, task is not performed. Task is: {}'.format(obtained_href))

@step(u'And the accept header "([^"]*)"')
def and_the_accept_header_group1(step, accept_content):

    world.headers[ACCEPT_HEADER] = accept_content


@step(u'When I install the product in the VM with hostname "([^"]*)"')
def when_i_install_the_product_in_the_vm_with_hostname_group1(step, hostname):

    body = dict_to_xml(simple_installation_body(product_name=world.product_name, product_version=world.product_version,
                                                hostname=hostname))

    world.request = api_utils.install_product(headers=world.headers, vdc_id=world.tenant_id, body=body)


@step(u'When I install the product in the VM with IP "([^"]*)" and hostname "([^"]*)"')
def when_i_install_the_product_in_the_vm_with_hostname_group1(step, ip, hostname):

    body = dict_to_xml(simple_installation_body(product_name=world.product_name, product_version=world.product_version,
                                                ip=ip, hostname=hostname))

    world.request = api_utils.install_product(headers=world.headers, vdc_id=world.tenant_id, body=body)


@step(u'When I install the product in the VM with fqn "([^"]*)" and hostname "([^"]*)"')
def when_i_install_the_product_in_the_vm_with_hostname_group1(step, fqn, hostname):

    world.fqn = fqn
    body = dict_to_xml(simple_installation_body(product_name=world.product_name, product_version=world.product_version,
                                                fqn=fqn, hostname=hostname))

    world.request = api_utils.install_product(headers=world.headers, vdc_id=world.tenant_id, body=body)


@step(u'When I install the product in the VM with OSType "([^"]*)" and hostname "([^"]*)"')
def when_i_install_the_product_in_the_vm_with_hostname_group1(step, ostype, hostname):

    body = dict_to_xml(simple_installation_body(product_name=world.product_name, product_version=world.product_version,
                                                ostype=ostype, hostname=hostname))

    world.request = api_utils.install_product(headers=world.headers, vdc_id=world.tenant_id, body=body)


@step(u'When I install the product with hostname "([^"]*)", ip "([^"]*)", fqn "([^"]*)" and OStype "([^"]*)"')
def install_product_with_all_data(step, hostname, ip, fqn, ostype):

    world.fqn = fqn
    body = dict_to_xml(simple_installation_body(product_name=world.product_name, product_version=world.product_version,
                                                ostype=ostype, hostname=hostname, ip=ip, fqn=fqn))

    world.request = api_utils.install_product(headers=world.headers, vdc_id=world.tenant_id, body=body)


@step(u'When I install the release "([^"]*)" in the VM with hostname "([^"]*)"')
def when_i_install_the_release_group1_in_the_vm_with_hostname_group2(step, product_version, hostname):

    body = dict_to_xml(simple_installation_body(product_name=world.product_name, product_version=product_version,
                                                hostname=hostname))

    world.request = api_utils.install_product(headers=world.headers, vdc_id=world.tenant_id, body=body)


@step(u'Then the product is installed')
def then_the_product_is_installed(step):

    assert_true(world.request.ok, world.request.content)
    obtained_href, obtained_status, obtained_vdc = get_installation_response(world.request)

    assert_equals(obtained_status, RUNNING)
    assert_equals(obtained_vdc, world.tenant_id)
    assert_true(wait_for_task_finished(headers=world.headers, url=obtained_href, status_to_be_finished=SUCCESS),
                'Task is not performed. Task is: {}'.format(obtained_href))
    assert_true(wait_for_software_installed(status_to_be_finished=True, file_name=world.file_name),
                "ERROR: SOFTWARE IS NOT INSTALLED")

    req = api_utils.uninstall_product(headers=world.headers, product_id=world.file_name, vdc_id=world.tenant_id,
                                      fqn=world.fqn)
    assert req.ok
    response = req.json()
    task_url = response[TASK_URL]
    print task_url
    assert_true(wait_for_task_finished(headers=world.headers, url=task_url, status_to_be_finished=SUCCESS),
                'Task is not performed. Task is: {}'.format(task_url))


@step(u'Then the product is not installed')
def then_the_product_is_installed(step):

    assert_true(world.request.ok, world.request.content)
    obtained_href, obtained_status, obtained_vdc = get_installation_response(world.request)

    assert_equals(obtained_status, RUNNING)
    assert_equals(obtained_vdc, world.tenant_id)
    assert_true(wait_for_task_finished(headers=world.headers, url=obtained_href, status_to_be_finished=ERROR),
                'Task is not performed. Task is: {}'.format(obtained_href))


@step(u'Then I obtain an "([^"]*)"')
def then_i_obtain_an_group1(step, error_code):

    assert_equals(str(world.request.status_code), error_code, world.request.content)
    world.headers = set_default_headers(world.token_id, world.tenant_id)


@step(u'Then I obtain the error "([^"]*)" in the task')
def then_i_obtain_the_error_group1_in_the_task(step, group1):

    assert_true(world.request.ok, world.request.content)
    obtained_href, obtained_status, obtained_vdc = get_installation_response(world.request)

    assert_equals(obtained_status, RUNNING)
    assert_equals(obtained_vdc, world.tenant_id)
    assert_true(wait_for_task_finished(headers=world.headers, url=obtained_href, status_to_be_finished=ERROR),
                'Task is not performed. Task is: {}'.format(obtained_href))
    print obtained_href


@step(u'And incorrect "([^"]*)" authentication')
def incorrect_token(step, new_token):
    world.headers[AUTH_TOKEN_HEADER] = new_token


@after.all
def tear_down(scenario):

    world.token_id, world.tenant_id = get_token()
    world.headers = set_default_headers(world.token_id, world.tenant_id)
    api_utils.delete_all_testing_products(world.headers)
