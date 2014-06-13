__author__ = 'arobres'

# -*- coding: utf-8 -*-
from lettuce import step, world, before, after
from commons.authentication import get_token
from commons.rest_utils import RestUtils
from commons.product_body import default_product, create_product_release
from commons.utils import dict_to_xml, set_default_headers, xml_to_dict
from commons.constants import CONTENT_TYPE, PRODUCT_NAME, ACCEPT_HEADER, AUTH_TOKEN_HEADER, CONTENT_TYPE_JSON, PRODUCT, VERSION, PRODUCT_RELEASE
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


@step(u'When I retrieve the product release "([^"]*)" information assigned to the "([^"]*)" with accept parameter "([^"]*)" response')
def get_product_release(step, product_release, product_name, accept_content):

    world.headers[ACCEPT_HEADER] = accept_content
    world.product_name = product_name
    world.product_release = product_release
    world.response = api_utils.retrieve_product_release_information(headers=world.headers, product_id=world.product_name,
                                                                    version=world.product_release)


@step(u'Then the product release information is received')
def then_the_product_release_is_deleted(step):

    assert_true(world.response.ok)
    response_headers = world.response.headers

    if response_headers[CONTENT_TYPE] == CONTENT_TYPE_JSON:
        try:
            response_body = world.response.json()
        except Exception, e:
            print str(e)

    else:
        response_body = xml_to_dict(world.response.content)[PRODUCT_RELEASE]

    assert_equals(response_body[VERSION], world.product_release)
    assert_equals(response_body[PRODUCT][PRODUCT_NAME], world.product_name)


@step(u'Then I obtain an "([^"]*)"')
def then_i_obtain_an_group1(step, error_code):

    assert_equals(str(world.response.status_code), error_code, world.response.status_code)
    world.headers = set_default_headers(world.token_id, world.tenant_id)


@step(u'And incorrect "([^"]*)" header')
def and_incorrect_content_type_header(step, content_type):
    world.headers[CONTENT_TYPE] = content_type


@step(u'And incorrect "([^"]*)" authentication')
def incorrect_token(step, new_token):
    world.headers[AUTH_TOKEN_HEADER] = new_token


@after.all
def setup_scenario(scenario):

    world.headers = set_default_headers(world.token_id, world.tenant_id)
    api_utils.delete_all_testing_products(world.headers)
