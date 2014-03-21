# -*- coding: utf-8 -*-
from lettuce import step, world
from time import sleep
from tdaf_lettuce_tools.dataset_utils.dataset_utils import DatasetUtils
from tools import http
#from tools.environment_request import EnvironmentRequest
from tools import catalogue_request

## errorTypes constants
NOT_ERROR = ""
NOT_FOUND = "Not Found"

AcceptXML = "xml"

@step(u'A new product "([^"]*)" with release "([^"]*)" with description "([^"]*)" in the catalog')
def a_new_product_release_with_description_in_the_catalog(step, product, version, description):
    world.product = product                                                                       # used in terrain.py for delete the product
    world.version = version                                                                       # used in terrain.py for delete the product
    world.env_requests.catalogue_addProductRelease (product, version, description, NOT_ERROR, AcceptXML)

@step(u'I delete a product "([^"]*)" with release "([^"]*)" in the catalog')
def i_delete_a_product_with_release_in_the_catalog(step, product, version):
    """
    I delete an existent product in the catalog

    :param step:
    :param product:
    """
    world.env_requests.catalogue_deleteProductRelease(product, version, NOT_ERROR)

@step(u'I request a wrong path when delete a product "([^"]*)" with release "([^"]*)" in the catalogue')
def i_request_a_wrong_path_when_delete_a_product_with_release_in_the_catalogue(step, product, version):
    world.env_requests.catalogue_deleteProductRelease(product, version, NOT_FOUND)

@step(u'I receive an? "([^"]*)" response with an? "([^"]*)" [?]*')
def i_receive_a_response_of_type(step, response_type, operation):
    status_code = http.status_codes[response_type]
    body_expected = world.env_requests.get_body_expected(response_type, operation)        # Read from body_message.py
    world.env_requests.check_response_status(world.response, status_code)
    world.env_requests.check_response_body(world.response, str(body_expected))
    pass


