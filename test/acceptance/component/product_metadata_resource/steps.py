# -*- coding: utf-8 -*-
# Copyright 2015 Telefonica Investigación y Desarrollo, S.A.U
#
# This file is part of FIWARE project.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
#
# You may obtain a copy of the License at:
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#
# See the License for the specific language governing permissions and
# limitations under the License.
#
# For those usages not covered by the Apache version 2.0 License please
# contact with opensource@tid.es

__author__ = 'jfernandez'


from lettuce import step, world
from commons.rest_utils import RestUtils
from commons.utils import response_body_to_dict, body_model_to_body_request
from commons.constants import ACCEPT_HEADER, AUTH_TOKEN_HEADER, DEFAULT_METADATA, METADATA, KEY, VALUE, DESCRIPTION, \
    METADATA_TENANT_ID, TENANT_ID_HEADER, CONTENT_TYPE
from nose.tools import assert_equals, assert_true
from commons.product_steps import ProductSteps
from lettuce_tools.dataset_utils.dataset_utils import DatasetUtils

api_utils = RestUtils()
product_steps = ProductSteps()
dataset_utils = DatasetUtils()


def __check_metadata_response__(metadata_to_check):
    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response.content))
    response_model = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER], xml_root_element_name=METADATA)

    assert_equals(metadata_to_check, response_model)


@step(u'a created product with name "([^"]*)"$')
@step(u'a created product with name "([^"]*)" and those metadatas$')
def given_a_created_product_with_name_group1(step, product_name):
    world.product_name = product_name
    product_steps.a_created_product_with_name(step, product_name, metadatas=world.metadatas)


@step(u'a created product with name "([^"]*)", release "([^"]*)" and those metadatas$')
def a_created_product_with_name_group1_and_release_group2(step, product_name, product_release):
    world.product_name = product_name
    world.product_release = product_release
    product_steps.a_created_product_with_name_and_release_with_metadatas(step=step, product_name=product_name,
                                                                         product_version=product_release,
                                                                         metadatas=world.metadatas)


@step(u'the following metadatas')
def and_the_following_metadatas(step):
    world.metadatas = list()
    for row in step.hashes:
        metadata = dict()
        metadata[KEY] = row[KEY]
        metadata[VALUE] = row[VALUE]
        if row[DESCRIPTION] != 'None':
            metadata[DESCRIPTION] = row[DESCRIPTION]

        if METADATA_TENANT_ID == metadata[KEY]:
            metadata[VALUE] = metadata[VALUE].replace('CURRENT', world.headers[TENANT_ID_HEADER])

        world.metadatas.append(metadata)


@step(u'the following attributes')
def and_the_following_attributes(step):
    world.attributes = list()
    for row in step.hashes:
        data = dict(dataset_utils.prepare_data(row))
        world.attributes.append(data)

    if len(step.hashes) == 1:
        world.attributes = world.attributes[0]


@step(u'accept header value "([^"]*)"')
def accept_header_value_group1(step, accept_header):
    world.headers[ACCEPT_HEADER] = accept_header


@step(u'the authentication token "([^"]*)":')
def the_authentication_token_group1(step, token):
    world.headers[AUTH_TOKEN_HEADER] = token


@step(u'the authentication tenant-id "([^"]*)"')
def the_authentication_tenant_id_group1(step, tenant_id):
    world.headers[TENANT_ID_HEADER] = tenant_id


@step(u'I request the metadata "([^"]*)" of the product "([^"]*)"')
def i_request_the_metadata_of_the_product(step, metadata_key, product_name):
    world.metadata_key_request = metadata_key
    world.response = api_utils.retrieve_product_metadata(headers=world.headers, product_id=product_name,
                                                         metadata_key=metadata_key)


@step(u'I request the metadata "([^"]*)" of the product "([^"]*)" using a invalid HTTP "([^"]*)" method')
def i_use_a_invalid_http_group1_method(step, metadata_key, product_name, http_method):
    world.response = api_utils.retrieve_product_metadata(headers=world.headers, product_id=product_name,
                                                         metadata_key=metadata_key, method=http_method)


@step(u'I delete the metadata "([^"]*)" of the product "([^"]*)"')
def i_delete_the_metadata_of_the_product(step, metadata_key, product_name):
    world.metadata_key_request = metadata_key
    world.response = api_utils.delete_product_metadata(headers=world.headers, product_id=product_name,
                                                       metadata_key=metadata_key)


@step(u'I update the metadata "([^"]*)" of the product "([^"]*)"')
def i_update_the_metadata_of_the_product(step, metadata_key, product_name):
    world.metadata_key_request = metadata_key
    world.metadata_to_be_updated = dict()
    assert_true(len(step.hashes) == 1, "Only one metadata is accepted in the dataset for this test case")
    dataset = dataset_utils.prepare_data(step.hashes[0])

    world.metadata_to_be_updated = {METADATA: dict(dataset)}
    body_request = body_model_to_body_request(body_model=world.metadata_to_be_updated,
                                              content_type=world.headers[CONTENT_TYPE],
                                              body_model_root_element=METADATA)

    world.response = api_utils.update_product_metadata(body=body_request, headers=world.headers,
                                                       product_id=product_name,
                                                       metadata_key=metadata_key)


@step(u'the metadata is retrieved')
def the_metadata_is_retrieved(step):
    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response.content))
    expected_metadata_list = DEFAULT_METADATA[METADATA] if world.metadatas is None else world.metadatas
    for metadata in expected_metadata_list:
        if metadata[KEY] == world.metadata_key_request:
            __check_metadata_response__(metadata)
            break


@step(u'the metadata is deleted')
def the_metadata_is_deleted(step):
    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response.content))
    i_request_the_metadata_of_the_product(step, world.metadata_key_request, world.product_name)
    assert_equals(str(world.response.status_code), "404")


@step(u'the metadata is updated')
def the_metadata_is_updated(step):
    assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response.content))
    i_request_the_metadata_of_the_product(step, world.metadata_key_request, world.product_name)
    expected_metadata_list = DEFAULT_METADATA[METADATA] if world.metadatas is None else world.metadatas
    for metadata in expected_metadata_list:
        if metadata[KEY] == world.metadata_key_request:
            metadata_to_check = dict(metadata)
            metadata_to_check.update(world.metadata_to_be_updated[METADATA])
            __check_metadata_response__(metadata_to_check)
            break


@step(u'the other metadatas are still present in the product')
def the_other_metadatas_are_still_present_in_the_product(step):
    deleted_metadata_key = world.metadata_key_request
    all_metadatas = list()
    if world.metadatas is not None:
        all_metadatas += world.metadatas
    all_metadatas += DEFAULT_METADATA[METADATA]

    for metadata in all_metadatas:
        if metadata[KEY] != deleted_metadata_key:
            i_request_the_metadata_of_the_product(step, metadata[KEY], world.product_name)
            __check_metadata_response__(metadata)


@step(u'I obtain an http error code "([^"]*)"')
def i_obtain_an_http_error_code_group1(step, error_code):
    assert_equals(str(world.response.status_code), error_code)
