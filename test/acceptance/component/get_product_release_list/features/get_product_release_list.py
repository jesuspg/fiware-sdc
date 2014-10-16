__author__ = 'arobres'

# -*- coding: utf-8 -*-
from lettuce import step, world, before, after
from commons.authentication import get_token
from commons.rest_utils import RestUtils
from commons.product_body import default_product, create_product_release
from commons.utils import dict_to_xml, set_default_headers, response_body_to_dict
from commons.constants import CONTENT_TYPE, PRODUCT_NAME, ACCEPT_HEADER, AUTH_TOKEN_HEADER, CONTENT_TYPE_JSON, PRODUCT
from commons.constants import PRODUCT_RELEASE_WITHOUT_RELEASES_RESPONSE, VERSION, PRODUCT_RELEASE_LIST
from nose.tools import assert_equals, assert_true

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


@step(u'Given a created product with name "([^"]*)" and release "([^"]*)"')
def given_a_created_product_with_name_group1(step, product_id, product_release):

    body = dict_to_xml(default_product(name=product_id))
    response = api_utils.add_new_product(headers=world.headers, body=body)
    assert_true(response.ok, response.content)
    world.product_id = response.json()[PRODUCT_NAME]
    body = dict_to_xml(create_product_release(version=product_release))
    response = api_utils.add_product_release(headers=world.headers, body=body, product_id=product_id)
    assert_true(response.ok, response.content)


@step(u'a created non released product with name "([^"]*)"')
def given_a_created_product_with_name_group1(step, product_id):

    world.created_product_body = default_product(name=product_id)
    body = dict_to_xml(world.created_product_body)
    response = api_utils.add_new_product(headers=world.headers, body=body)
    assert_true(response.ok, response.content)
    world.product_id = response.json()[PRODUCT_NAME]


@step(u'When I retrieve the product release "([^"]*)" list assigned to the "([^"]*)" '
      u'with accept parameter "([^"]*)" response')
def get_product_release(step, product_release, product_name, accept_content):

    world.headers[ACCEPT_HEADER] = accept_content
    world.product_name = product_name
    world.product_release = product_release
    world.response = api_utils.retrieve_product_release_list(headers=world.headers, product_id=world.product_name)


@step(u'Then the product release list is received')
def then_the_product_release_list_is_received(step):

    assert_true(world.response.ok)
    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response.content))

    response_body = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER],
                                          xml_root_element_name=PRODUCT_RELEASE_LIST, is_list=True)

    assert_equals(response_body[VERSION], world.product_release)
    assert_equals(response_body[PRODUCT][PRODUCT_NAME], world.product_name)


@step(u'Then no product releases are received')
def then_no_product_releases_are_received(step):

    assert_true(world.response.ok)
    response_headers = world.response.headers
    if response_headers[CONTENT_TYPE] == CONTENT_TYPE_JSON:
        assert_equals(len(world.response.json()), 0, 'RESPONSE: {}'.format(world.response.content))
    else:
        assert_equals(PRODUCT_RELEASE_WITHOUT_RELEASES_RESPONSE, world.response.content)


@step(u'Then I obtain an "([^"]*)"')
def then_i_obtain_an_group1(step, error_code):

    assert_equals(str(world.response.status_code), error_code)
    world.headers = set_default_headers(world.token_id, world.tenant_id)


@step(u'And incorrect "([^"]*)" header')
def and_incorrect_content_type_header(step, content_type):
    world.headers[CONTENT_TYPE] = content_type


@step(u'And incorrect "([^"]*)" authentication')
def incorrect_token(step, new_token):
    world.headers[AUTH_TOKEN_HEADER] = new_token


@after.all
def tear_down(scenario):

    world.headers = set_default_headers(world.token_id, world.tenant_id)
    api_utils.delete_all_testing_products(world.headers)
