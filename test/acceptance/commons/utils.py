__author__ = 'arobres'

import xmldict
import string
import random
from constants import AUTH_TOKEN_HEADER, TENANT_ID_HEADER, CONTENT_TYPE, CONTENT_TYPE_XML, ACCEPT_HEADER, \
    CONTENT_TYPE_JSON

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
