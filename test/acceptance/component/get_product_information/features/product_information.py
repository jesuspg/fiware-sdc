__author__ = 'arobres'

# -*- coding: utf-8 -*-
from lettuce import step, world, before, after
from commons.authentication import get_token
from commons.rest_utils import RestUtils
from commons.product_body import default_product, create_default_metadata_list, create_default_attribute_list
from commons.utils import dict_to_xml, set_default_headers, xml_to_dict, response_body_to_dict, \
    replace_none_value_metadata_to_empty_string
from commons.constants import CONTENT_TYPE, CONTENT_TYPE_JSON, PRODUCT_NAME, ACCEPT_HEADER, AUTH_TOKEN_HEADER, PRODUCT, \
    PRODUCT_DESCRIPTION, PRODUCT_ATTRIBUTES, PRODUCT_METADATAS, DEFAULT_METADATA, METADATA, ATTRIBUTE
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


@before.outline
def setup_outline(param1, param2, param3, param4):
    setup_scenario(None)


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


@step(u'When I retrieve the product "([^"]*)" with accept parameter "([^"]*)" response')
def when_i_retrieve_the_product_group1_with_accept_parameter_group2_response(step, product_id, accept_content):

    world.headers[ACCEPT_HEADER] = accept_content
    world.response = api_utils.retrieve_product(headers=world.headers, product_id=product_id)


@step(u'When I retrieve the product attributes "([^"]*)" with accept parameter "([^"]*)" response')
def retrieve_product_attributes(step, product_id, accept_content):

    world.headers[ACCEPT_HEADER] = accept_content
    world.response = api_utils.retrieve_product_attributes(headers=world.headers, product_id=product_id)


@step(u'When I retrieve the product metadatas "([^"]*)" with accept parameter "([^"]*)" response')
def retrieve_product_metadatas(step, product_id, accept_content):

    world.headers[ACCEPT_HEADER] = accept_content
    world.response = api_utils.retrieve_product_metadatas(headers=world.headers, product_id=product_id)


@step(u'Then the product is retrieved')
def then_the_product_is_retrieved(step):

    assert_true(world.response.ok)
    response_headers = world.response.headers

    if response_headers[CONTENT_TYPE] == CONTENT_TYPE_JSON:
        try:
            response_body = world.response.json()
        except Exception, e:
            print str(e)

    else:
        response_body = xml_to_dict(world.response.content)[PRODUCT]

    assert_equals(response_body[PRODUCT_NAME], world.created_product_body[PRODUCT][PRODUCT_NAME])
    assert_equals(response_body[PRODUCT_DESCRIPTION], world.created_product_body[PRODUCT][PRODUCT_DESCRIPTION])

    if world.attributes is not None:

        assert_equals(world.created_product_body[PRODUCT][PRODUCT_ATTRIBUTES], response_body[PRODUCT_ATTRIBUTES])
        world.attributes = None

    if world.metadatas is not None:
        for metadata in world.created_product_body[PRODUCT][PRODUCT_METADATAS]:
            assert_in(metadata, response_body[PRODUCT_METADATAS])
        world.metadatas = None


@step(u'Then I obtain an "([^"]*)"')
def then_i_obtain_an_group1(step, error_code):

    assert_equals(str(world.response.status_code), error_code)
    world.headers = set_default_headers(world.token_id, world.tenant_id)


@step(u'Then the attributes product are empty')
def then_the_attributes_product_are_empty(step):

    assert_true(world.response.ok)
    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response.content))

    response_body = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER],
                                          xml_root_element_name=PRODUCT_ATTRIBUTES, is_list=True)

    assert_true(response_body is None or len(response_body) == 0)


@step(u'Then the attributes product are retrieved')
def then_the_attributes_product_are_retrieved(step):

    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response.content))

    response_body = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER],
                                          xml_root_element_name=PRODUCT_ATTRIBUTES, is_list=True)

    assert_equals(world.created_product_body[PRODUCT][PRODUCT_ATTRIBUTES], response_body)


@step(u'Then the metadatas product contain default metadatas')
def then_the_metadatas_product_contain_default_metadatas(step):

    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response.content))

    response_body = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER],
                                          xml_root_element_name=PRODUCT_METADATAS, is_list=True)

    assert_equals(len(response_body), 6)

    # Add default metadata 'tenant_id'
    metadatas_with_tenant = list(DEFAULT_METADATA[METADATA])
    metadatas_with_tenant.append({"key": "tenant_id", "value": world.tenant_id})

    # Workaround: xmldict manage Empty values as None value
    replace_none_value_metadata_to_empty_string(response_body)

    assert_equals(response_body, metadatas_with_tenant)


@step(u'Then the metadatas product are retrieved')
def then_the_metadatas_product_are_retrieved(step):

    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response.content))

    response_body = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER],
                                          xml_root_element_name=PRODUCT_METADATAS, is_list=True)

    if world.metadatas is not None:
        for metadata in world.created_product_body[PRODUCT][PRODUCT_METADATAS]:
            assert_in(metadata, response_body)
        world.metadatas = None


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
