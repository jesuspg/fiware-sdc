__author__ = 'arobres'

# -*- coding: utf-8 -*-
from lettuce import step, world, before, after
from commons.authentication import get_token
from commons.rest_utils import RestUtils
from commons.product_body import default_product, create_product_release
from commons.utils import dict_to_xml, set_default_headers, wait_for_task_finished, wait_for_software_installed, \
    get_installation_response
from commons.constants import PRODUCT_NAME, ACCEPT_HEADER, AUTH_TOKEN_HEADER, KEY, VALUE, INSTALLATOR, \
    INSTALLATOR_VALUE, RUNNING, SUCCESS, ERROR, TASK_URL, INSTALLED, UNINSTALLED
from nose.tools import assert_equals, assert_true
from commons.installation_body import simple_installation_body


api_utils = RestUtils()

@before.each_feature
def setup_feature(feature):

    world.token_id, world.tenant_id = get_token()


@before.each_scenario
def setup_scenario(scenario):

    world.headers = set_default_headers(world.token_id, world.tenant_id)
    api_utils.uninstall_all_products(world.headers)
    api_utils.delete_all_testing_products(world.headers)
    world.attributes = None
    world.metadatas = None
    world.fqn = ''


@step(u'a installed product with name "([^"]*)" and release "([^"]*)" in the hostname "([^"]*)"')
def installed_product(step, product_name, product_version, hostname):

    world.product_name = product_name
    world.product_version = product_version
    world.hostname = hostname
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
    world.status = INSTALLED
    world.file_name = '{}_{}'.format(product_name, product_version)


@step(u'Given a uninstalled product with name "([^"]*)" and release "([^"]*)" in the hostname "([^"]*)"')
def uninstalled_product(step, product_name, product_version, hostname):

    installed_product(step, product_name, product_version, hostname)
    world.product_id = '{}_{}'.format(product_name, product_version)

    response = api_utils.uninstall_product(headers=world.headers, product_id=world.product_id, vdc_id=world.tenant_id)
    assert_true(response.ok, response.content)
    world.status = UNINSTALLED


@step(u'And the accept header "([^"]*)"')
def and_the_accept_header_group1(step, accept_content):

    world.headers[ACCEPT_HEADER] = accept_content


@step(u'When I uninstall the installed product "([^"]*)" and release "([^"]*)"')
def when_i_uninstall_the_installed_product_group1_and_release_group2(step, product_name, product_release):
    world.product_name = product_name
    world.product_release = product_release
    world.product_id = '{}{}_{}'.format(world.fqn, product_name, product_release)
    world.request = api_utils.uninstall_product(headers=world.headers, product_id=world.product_id, vdc_id=world.tenant_id)



@step(u'Then the product "([^"]*)" and release "([^"]*)" is uninstalled')
def then_the_product_group1_and_release_group2_is_uninstalled(step, product_name, product_release):

    assert_true(world.request.ok, world.request.content)
    obtained_href, obtained_status, obtained_vdc = get_installation_response(world.request)

    assert_equals(obtained_status, RUNNING)
    assert_equals(obtained_vdc, world.tenant_id)

    assert_true(wait_for_task_finished(headers=world.headers, url=obtained_href, status_to_be_finished=SUCCESS),
                'Task is not performed. Task is: {}'.format(obtained_href))

    assert_true(wait_for_software_installed(status_to_be_finished=False, file_name=world.file_name),
                "ERROR: SOFTWARE IS NOT UNINSTALLED")


@step(u'Then the product "([^"]*)" and release "([^"]*)" remains installed')
def then_the_product_group1_and_release_group2_remains_installed(step, product_name, product_version):

    new_file_name = '{}_{}'.format(product_name, product_version)
    assert_true(wait_for_software_installed(status_to_be_finished=False, file_name=new_file_name),
                "ERROR: SOFTWARE IS NOT INSTALLED")


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


@step(u'Then I receive an error in the result')
def then_i_receive_an_error_in_the_result(step):

    assert_true(world.request.ok, world.request.content)
    obtained_href, obtained_status, obtained_vdc = get_installation_response(world.request)

    assert_equals(obtained_status, ERROR)

@step(u'And incorrect "([^"]*)" authentication')
def incorrect_token(step, new_token):
    world.headers[AUTH_TOKEN_HEADER] = new_token


@after.all
def tear_down(scenario):

    world.token_id, world.tenant_id = get_token()
    world.headers = set_default_headers(world.token_id, world.tenant_id)
    api_utils.uninstall_all_products(world.headers)
    api_utils.delete_all_testing_products(world.headers)
