__author__ = 'arobres'

# -*- coding: utf-8 -*-
from lettuce import step, world, before, after
from commons.authentication import get_token
from commons.rest_utils import RestUtils
from commons.product_body import default_product, create_default_metadata_list, create_default_attribute_list
from commons.utils import dict_to_xml, response_body_to_dict, set_default_headers
from commons.constants import CONTENT_TYPE, PRODUCTS, PRODUCT_NAME, PRODUCT, CONTENT_TYPE_JSON, \
    ACCEPT_HEADER, AUTH_TOKEN_HEADER, PRODUCT_DESCRIPTION, PRODUCT_ATTRIBUTES, PRODUCT_METADATAS
from nose.tools import assert_equals, assert_true, assert_in

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
    world.exist = False


@step(u'Given a created product with name "([^"]*)"')
def given_a_created_product_with_name_group1(step, product_id):

    world.created_product_body = default_product(name=product_id)
    body = dict_to_xml(world.created_product_body)
    response = api_utils.add_new_product(headers=world.headers, body=body)
    assert_true(response.ok, response.content)
    world.product_id = response.json()[PRODUCT_NAME]


@step(u'Given a created product with attributes and name "([^"]*)"')
def given_a_created_product_with_attributes_and_name_group1(step, product_id):

    world.attributes = create_default_attribute_list(2)
    world.created_product_body = default_product(name=product_id, attributes=world.attributes)
    body = dict_to_xml(world.created_product_body)
    response = api_utils.add_new_product(headers=world.headers, body=body)
    assert_true(response.ok, response.content)
    world.product_id = response.json()[PRODUCT_NAME]


@step(u'Given a created product with metadatas and name "([^"]*)"')
def given_a_created_product_with_attributes_and_name_group1(step, product_id):

    world.metadatas = create_default_metadata_list(2)
    world.created_product_body = default_product(name=product_id, metadata=world.metadatas)
    body = dict_to_xml(world.created_product_body)
    response = api_utils.add_new_product(headers=world.headers, body=body)
    assert_true(response.ok, response.content)
    world.product_id = response.json()[PRODUCT_NAME]


@step(u'Given a created product with all data and name "([^"]*)"')
def given_a_created_product_with_all_data_and_name_group1(step, product_id):

    world.metadatas = create_default_metadata_list(5)
    world.attributes = create_default_attribute_list(5)
    world.created_product_body = default_product(name=product_id, metadata=world.metadatas, attributes=world.attributes)
    body = dict_to_xml(world.created_product_body)
    response = api_utils.add_new_product(headers=world.headers, body=body)
    assert_true(response.ok, response.content)
    world.product_id = response.json()[PRODUCT_NAME]


@step(u'When I retrieve the list product with accept parameter "([^"]*)" response')
def when_i_retrieve_the_list_product_with_accept_parameter_group1_response(step, accept_content):
    world.headers[ACCEPT_HEADER] = accept_content
    world.response = api_utils.retrieve_product_list(headers=world.headers)


@step(u'Then the product is returned in the list')
def then_the_product_is_returned_in_the_list(step):

    assert_true(world.response.ok, world.response.content)
    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response.content))

    response_body = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER],
                                          xml_root_element_name=PRODUCTS, is_list=True)

    for product in response_body:

        if product[PRODUCT_NAME] == world.created_product_body[PRODUCT][PRODUCT_NAME]:
            world.exist = True
            assert_equals(product[PRODUCT_DESCRIPTION], world.created_product_body[PRODUCT][PRODUCT_DESCRIPTION])

            if world.attributes is not None:
                assert_equals(product[PRODUCT_ATTRIBUTES], world.created_product_body[PRODUCT][PRODUCT_ATTRIBUTES])

            if world.metadatas is not None:
                for metadata in world.created_product_body[PRODUCT][PRODUCT_METADATAS]:
                    assert_in(metadata, product[PRODUCT_METADATAS])

    assert_true(world.exist)
    world.exist = False


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

    world.token_id, world.tenant_id = get_token()
    world.headers = set_default_headers(world.token_id, world.tenant_id)
    api_utils.delete_all_testing_products(world.headers)
