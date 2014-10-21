from lettuce import step, world

from commons.product_steps import ProductSteps
from commons.rest_utils import RestUtils
from commons.constants import *
from commons.utils import response_body_to_dict, replace_none_value_metadata_to_empty_string

from nose.tools import assert_equals, assert_true, assert_false, assert_in

api_utils = RestUtils()
product_steps = ProductSteps()


def check_if_product_is_in_list(response, product_release):
    """
    Checks if product is in response list with his attribute and metadatas
        - Assertions:
            * Metadata (default metadatas)
            * Attributes (if exist)
            * Product existence in list
    :param response: Response from API - dic
    :param product_release: Product release version - str
    :return: None
    """

    found = False
    for product_and_release in response:
        if product_and_release[PRODUCT][PRODUCT_NAME] == world.product_name:
            if product_release is None or product_and_release[VERSION] == product_release:
                found = True
                for metadata in DEFAULT_METADATA[METADATA]:
                    # Workaround: xmldict manage Empty values as None value
                    replace_none_value_metadata_to_empty_string(product_and_release[PRODUCT][PRODUCT_METADATAS])
                    assert_in(metadata, product_and_release[PRODUCT][PRODUCT_METADATAS],
                              "Metadata are not the expected!")
                if world.attributes is not None:
                    assert_equals(product_and_release[PRODUCT][PRODUCT_ATTRIBUTES], world.attributes,
                                  "Attributes are not expected!")
                break
    assert_true(found, "Product and release not found in list!")


@step(u'a created product with this name "([^"]*)"')
def a_created_product_with_name(step, product_name):
    world.product_name = product_name
    product_steps.a_created_product_with_name(step, product_name)


@step(u'a created product with name "([^"]*)" and release "([^"]*)"')
def a_created_product_with_name_group1_and_release_group2(step, product_name, product_release):
    world.product_name = product_name
    world.product_release = product_release
    product_steps.a_created_product_with_name_and_release(step=step, product_name=product_name,
                                                          product_version=product_release)


@step(u'a created product with name "([^"]*)" and releases:')
def a_create_product_with_name_group1_and_releases(step, product_name):
    world.product_name = product_name
    world.product_release = []
    for row in step.hashes:
        world.product_release.append(row['release'])

    product_steps.a_created_product_with_name_and_release_list(step, product_name, world.product_release)


@step(u'accept header value "([^"]*)"')
def accept_header_value_group1(step, accept_header):
    world.headers[ACCEPT_HEADER] = accept_header


@step(u'default product attributes')
def default_product_attributes(step):
    world.attributes = DEFAULT_ATTRIBUTE[ATTRIBUTE]


@step(u'the authentication token "([^"]*)":')
def the_authentication_token_group1(step, token):
    world.headers[AUTH_TOKEN_HEADER] = token


@step(u'the authentication tenant-id "([^"]*)"')
def the_authentication_tenant_id_group1(step, tenant_id):
    world.headers[TENANT_ID_HEADER] = tenant_id


@step(u'I retrieve the product list with its releases')
def i_retrieve_the_product_list_with_its_releases(step):
    world.response = api_utils.retrieve_productandrelease_list(headers=world.headers)


@step(u'I use a invalid HTTP "([^"]*)" method')
def i_use_a_invalid_http_group1_method(step, http_method):
    world.response = api_utils.request_productandrelease(headers=world.headers, method=http_method)


@step(u'the list is returned')
def the_list_is_returned(step):
    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response.content))

    response_headers = world.response.headers
    assert_in(response_headers[CONTENT_TYPE], world.headers[ACCEPT_HEADER],
              'RESPONSE HEADERS: {}'.format(world.response.headers))


@step(u'the product with its release is in the list')
def the_product_with_its_release_is_in_the_list(step):
    response = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER],
                                     xml_root_element_name=PRODUCTANDRELEASE_LIST, is_list=True)

    assert_true(len(response) != 0)
    check_if_product_is_in_list(response, world.product_release)


@step(u'the product with all its releases is in the list')
def the_product_with_all_its_releases_is_in_the_list(step):
    response = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER],
                                     xml_root_element_name=PRODUCTANDRELEASE_LIST, is_list=True)

    for release in world.product_release:
        check_if_product_is_in_list(response, release)


@step(u'the product is not in the list')
def the_product_is_not_in_the_list(step):
    response = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER],
                                     xml_root_element_name=PRODUCTANDRELEASE_LIST, is_list=True)

    found = False
    if len(response) != 0:
        for product_and_release in response:
            if product_and_release[PRODUCT][PRODUCT_NAME] == world.product_name:
                found = True
                break
    assert_false(found, "Product is in the list and it shouldn't!")


@step(u'I obtain an http error code "([^"]*)"')
def i_obtain_an_http_error_code_group1(step, error_code):
    assert_equals(str(world.response.status_code), error_code)

