__author__ = 'arobres'

import xmldict
import string
import random
import time
from commons.rest_utils import RestUtils
from constants import AUTH_TOKEN_HEADER, TENANT_ID_HEADER, CONTENT_TYPE, CONTENT_TYPE_XML, ACCEPT_HEADER, \
    CONTENT_TYPE_JSON, VDC, TASK_URL,STATUS_XML
from lxml import etree
from commons.fabric_utils import execute_file_exist


def dict_to_xml(dict_to_convert):

    return xmldict.dict_to_xml(dict_to_convert)


def xml_to_dict(xml_to_convert):

    return xmldict.xml_to_dict(xml_to_convert)


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


def wait_for_task_finished(seconds=30, url=None, status_to_be_finished=None, headers=None):

    headers[ACCEPT_HEADER] = CONTENT_TYPE_JSON
    for count in range(seconds):
        request = RestUtils.call_url_task(method='get', url=url, headers=headers)
        response = request.json()
        print 'TIME: {}\n STATUS: {}'.format(count, response['@status'])

        if response['@status'] == status_to_be_finished:
            return True
        time.sleep(1)

    return False


def wait_for_software_installed(seconds=90, status_to_be_finished=True, file_name=None):

    for count in range(seconds/3):

        response = execute_file_exist(test_file_name=file_name)
        print "FILE EXIST: {}".format(response)
        if status_to_be_finished:
            return True

        time.sleep(3)
    return False


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
