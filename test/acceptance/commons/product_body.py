__author__ = 'arobres'

from constants import PRODUCT, PRODUCT_DESCRIPTION, PRODUCT_NAME, PRODUCT_ATTRIBUTES, PRODUCT_METADATAS, KEY, \
    DESCRIPTION, VALUE, VERSION, ATTRIBUTE_TYPE, ATTRIBUTE_TYPE_PLAIN
from utils import id_generator, delete_keys_from_dict


def simple_product_body(description=None, name=None):

    return {PRODUCT: {PRODUCT_DESCRIPTION: description, PRODUCT_NAME: name}}


def product_with_attributes(description=None, name=None, attributes=None):

    if len(attributes) == 1:
        attributes = attributes[0]

    return {PRODUCT: {PRODUCT_DESCRIPTION: description, PRODUCT_NAME: name, PRODUCT_ATTRIBUTES: attributes}}


def product_with_metadata(description=None, name=None, metadata=None):

    if len(metadata) == 1:
        metadata = metadata[0]

    return {PRODUCT: {PRODUCT_DESCRIPTION: description, PRODUCT_NAME: name, PRODUCT_METADATAS: metadata}}


def product_with_all_parameters(description=None, name=None, metadata=None, attributes=None):

    if len(attributes) == 1:
        attributes = attributes[0]

    if len(metadata) == 1:
        metadata = metadata[0]

    return {PRODUCT: {PRODUCT_DESCRIPTION: description, PRODUCT_NAME: name, PRODUCT_METADATAS: metadata,
                      PRODUCT_ATTRIBUTES: attributes}}


def default_product(name=None, metadata=None, attributes=None):

    body_dict = {PRODUCT: {PRODUCT_DESCRIPTION: id_generator(20), PRODUCT_NAME: name, PRODUCT_METADATAS: metadata,
                 PRODUCT_ATTRIBUTES: attributes}}

    if attributes is None:
        body_dict = delete_keys_from_dict(body_dict, PRODUCT_ATTRIBUTES)
    if metadata is None:
        body_dict = delete_keys_from_dict(body_dict, PRODUCT_METADATAS)

    return body_dict


def create_default_metadata_list(num_metadatas=2):
    """
    Creates a list with random metadata values
    :param num_metadatas: Number of metadatas to be generated
    :return: A list of random metadatas
    """
    parameters = []

    for i in range(num_metadatas):
        parameter = {}
        parameter[KEY] = id_generator(10)
        parameter[DESCRIPTION] = id_generator(10)
        parameter[VALUE] = id_generator(10)
        parameters.append(parameter)

    return parameters


def create_default_attribute_list(num_attributes=2):
    """
    Creates a list with random attribute values and default type
    :param num_attributes: Number of attributes to be generated
    :return: A list of random attributes
    """
    parameters = []

    for i in range(num_attributes):
        parameter = {}
        parameter[KEY] = id_generator(10)
        parameter[DESCRIPTION] = id_generator(10)
        parameter[VALUE] = id_generator(10)
        parameter[ATTRIBUTE_TYPE] = ATTRIBUTE_TYPE_PLAIN
        parameters.append(parameter)

    return parameters


def create_product_release(version=None):

    return {"productReleaseDto": {VERSION: version}}
