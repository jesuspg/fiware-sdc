# -*- coding: utf-8 -*-
# Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U
#
# This file is part of FI-WARE project.
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

__author__ = 'arobres, jfernandez'

import xmldict
import xmltodict
import string
import random
import time
from commons.rest_utils import RestUtils
from constants import *
from lxml import etree
from commons.fabric_utils import execute_file_exist
from configuration import WAIT_FOR_OPERATION, WAIT_FOR_INSTALLATION


def __set_none_as_empty_value__(python_dict_element):
    """ Replace all None values of a dict by an empty value ""
    :param python_dict_element: Dict to be analyzed. Will be replaced itself.
    :return: None
    """
    for element in python_dict_element:
        if python_dict_element[element] is None:
            python_dict_element.update({element: ''})
        elif isinstance(element, dict):
            __set_none_as_empty_value__(element)


def dict_to_xml(dict_to_convert):

    return xmldict.dict_to_xml(dict_to_convert)


def xml_to_dict(xml_to_convert):

    return xmldict.xml_to_dict(xml_to_convert)


def xml_to_dict_attr(xml_to_convert):

    return xmltodict.parse(xml_to_convert, attr_prefix='')


def response_body_to_dict(http_response, accept_content_type, with_attributes=False, xml_root_element_name=None,
                          is_list=False):
    """
    Method to convert a XML o JSON response in a Python dict
    :param http_response: 'Requests (lib)' response
    :param accept_content_type: Accept header value
    :param with_attributes: For XML requests. If True, XML attributes will be processed
    :param xml_root_element_name: For XML requests. XML root element in response.
    :param is_list: For XML requests. If response is a list, a True value will delete list node name
    :return: Python dict with response.
    """
    if ACCEPT_HEADER_JSON in accept_content_type:
        try:
            return http_response.json()
        except Exception, e:
            print str(e)
    else:
        if with_attributes is True:
            return xml_to_dict_attr(http_response.content)[xml_root_element_name]
        else:
            assert xml_root_element_name is not None,\
                "xml_root_element_name is a mandatory param when body is in XML and attributes are not considered"
            response_body = xml_to_dict(http_response.content)[xml_root_element_name]
            if response_body is not None:
                __set_none_as_empty_value__(response_body)

            if is_list and response_body is not None:
                response_body = response_body.popitem()[1]

            return response_body


def body_model_to_body_request(body_model, content_type, body_model_root_element=None):
    if CONTENT_TYPE_XML in content_type:
        return dict_to_xml(body_model)
    else:
        return body_model[body_model_root_element]


def set_default_headers(token_id, tenant_id):

    headers = dict()
    headers[AUTH_TOKEN_HEADER] = token_id
    headers[TENANT_ID_HEADER] = tenant_id
    headers[CONTENT_TYPE] = CONTENT_TYPE_XML
    headers[ACCEPT_HEADER] = CONTENT_TYPE_JSON

    return headers


def id_generator(size=10, chars=string.ascii_letters + string.digits):

    """Method to create random ids
    :param size: define the string size
    :param chars: the characters to be use to create the string
    return ''.join(random.choice(chars) for x in range(size))
    """

    return ''.join(random.choice(chars) for x in range(size))


def delete_keys_from_dict(dict_del, key):

    """
    Method to delete keys from python dict
    :param dict_del: Python dictionary with all keys
    :param key: key to be deleted in the Python dictionary
    :returns a new Python dictionary without the rules deleted
    """

    if key in dict_del.keys():

        del dict_del[key]
    for v in dict_del.values():
        if isinstance(v, dict):
            delete_keys_from_dict(v, key)

    return dict_del


def delete_keys_when_value_is_none(dict_del):

    default_dict = dict_del.copy()
    for v in default_dict.keys():
        if default_dict[v] is None:
            del dict_del[v]
    return dict_del


def replace_none_value_metadata_to_empty_string(list_of_metadatas):
    """
    In a metadata list, replace None value by empty string
    :param list_of_metadatas:
    :return:
    """
    for metadata in list_of_metadatas:
        if metadata['value'] is None:
            metadata['value'] = ''


def wait_for_task_finished(vdc_id, task_id, seconds=WAIT_FOR_OPERATION, status_to_be_finished=None, headers=None):

    rest_utils = RestUtils()

    correct_status = False
    for count in range(seconds):
        response = rest_utils.retrieve_task(headers=headers, vdc_id=vdc_id, task_id=task_id)
        response_body = response_body_to_dict(response, headers[ACCEPT_HEADER], with_attributes=True,
                                              xml_root_element_name=TASK)
        status = response_body[TASK_STATUS]
        print '[TASK] Waiting for task status. CHECK: {} - STATUS: {}'.format(count, status)

        if status == status_to_be_finished:
            correct_status = True
            break
        elif status != TASK_STATUS_VALUE_RUNNING:
            break
        time.sleep(5)

    if correct_status is False:
        print "[TASK] Response body:", response_body

    return correct_status


def wait_for_software_installed(seconds=WAIT_FOR_INSTALLATION, status_to_be_finished=True, file_name=None):

    for count in range(seconds/3):

        response = execute_file_exist(test_file_name=file_name)
        print "Waiting for file status. FILE EXISTS: {}".format(response)
        if status_to_be_finished == response:
            return True

        time.sleep(3)
    return False


#@deprecated [utils.response_body_to_dict]
def convert_response_to_json(response):

    response_headers = response.headers
    if response_headers[CONTENT_TYPE] == CONTENT_TYPE_JSON:
        try:
            response_body = response.json()
        except Exception, e:
            print str(e)

    else:
        response_body = xml_to_dict(response.content)

    return response_body


def get_installation_response(response):

    response_headers = response.headers
    if response_headers[CONTENT_TYPE] == CONTENT_TYPE_JSON:
        try:
            response_body = response.json()
            status = response_body[STATUS_XML]
            href = response_body[TASK_URL]
            vdc = response_body[VDC]
        except Exception, e:
            print str(e)

    else:
        try:
            response_xml = etree.XML(response.content)
            href = response_xml.xpath("//task/@href")[0]
            status = response_xml.xpath("//task/@status")[0]
            vdc = response_xml.xpath("//task/vdc")[0].text

        except Exception, e:
            print str(e)

    return href, status, vdc


def generate_product_instance_id(vm_fqn, product_name, product_version):
    """
    Method to generate instance id for installed products (product instances)
    :param vm_fqn: FQN (where product has been installed)
    :param product_name: Product name (instance)
    :param product_version: Product release (instance)
    :return: Product instance ID
    """
    return "{}_{}_{}".format(vm_fqn, product_name, product_version)


def _replace_ipall_att_value(attribute, installator):
    """
    If installator is Puppet, list of values (IPALL type) is installed by our testing manifest without ",".
    This method return the list without that character to validate a successful installation
    :param attribute: Instance attribute to be processed
    :return: If installator is Puppet, list of values when attribute type is IPALL will be processed
    to delete the "," character
    """
    if installator == 'puppet' and ATTRIBUTE_TYPE in attribute and attribute[ATTRIBUTE_TYPE] == ATTRIBUTE_TYPE_IPALL:
        return attribute[VALUE].replace(",", "")
    else:
        return attribute[VALUE]


def generate_content_installed_by_product(product_name, product_version, instance_attributes, installator='chef'):

    att_01 = PRODUCT_INSTALLATION_ATT1_DEFAULT
    att_02 = PRODUCT_INSTALLATION_ATT2_DEFAULT

    if instance_attributes is not None or len(instance_attributes) != 0:
        if len(instance_attributes) >= 1:
            att_01 = _replace_ipall_att_value(instance_attributes[0], installator)

        if len(instance_attributes) >= 2:
            att_02 = _replace_ipall_att_value(instance_attributes[1], installator)
    return PRODUCT_INSTALLATION_FILE_CONTENT.format(product_name=product_name, product_version=product_version,
                                                    att_01=att_01, att_02=att_02)
