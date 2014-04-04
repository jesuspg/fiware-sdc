# -*- coding: utf-8 -*-
from lettuce import step, world
from time import sleep

from tdaf_lettuce_tools.dataset_utils.dataset_utils import DatasetUtils
from tools import http

from tools import catalogue_request

## errorTypes constants
NOT_ERROR = ""
NOT_FOUND = "Not Found"

AcceptXML = "xml"

#--------------------------------------------------------------------------------------------------------
@step(u'The "([^"]*)" has been created and the sdc is up and properly configured')
def the_Product_test_0001_has_been_created_and_the_sdc_is_up_and_properly_configured(step, product):
    """
    Add a new product into the catalogue
    :param step:
    :param product: product name
    """
    world.product = product                                                                    # used in terrain.py for delete the product
    world.version = " "                                                                        # used in terrain.py for delete the product
    world.env_requests.catalogue_addProduct(product, "attributes_and_all_metadatas", None, NOT_ERROR, AcceptXML)
    pass

@step(u'the sdc is up and properly configured')
def the_sdc_is_up_and_properly_configured(step):
    """
    Nothing to do here, the set up should be done by external means
    :param step:
    """
    pass


#---------------------------------- Add product release -------------------------------------------------
@step(u'I add a new release "([^"]*)" with description "([^"]*)" associated to product "([^"]*)" in the catalog with "([^"]*)" content in the response')
def i_add_a_new_release_with_description_associated_to_product_in_the_catalog(step, version, description, product, Accept):
    """
    Add a new release associated to a Product into the catalogue
    :param step:
    :param version: new release
    :param description: release Notes
    :param product:  product name
    :param Accept: specify media types which are acceptable for the response, ex:
                     "xml", "json"
    """
    world.product = product                                                                        # used in terrain.py for delete the product
    world.version = version                                                                        # used in terrain.py for delete the product
    world.env_requests.catalogue_addProductRelease (product, version, description, NOT_ERROR, Accept)

@step(u'I request a wrong path when I add a new product release in the catalog')
def i_request_a_wrong_path_when_i_add_a_new_product_release_in_the_catalog(step):
    world.env_requests.catalogue_addProductRelease (world.product, "version_error", "description_error", NOT_FOUND, AcceptXML)

@step(u'I request unauthorized errors "([^"]*)" when add a new product "([^"]*)" with release "([^"]*)" in the catalog with xml content in the response')
def  i_request_unauthorized_errors_when_add_a_new_product_with_release_in_the_catalog_with_xml_content_in_the_response(step, error, product, version):
    """
    Add a new release associated to a Product into the catalogue
    :param step:
    :param version: new release
    :param description: release Notes
    :param product:  product name
    :param Accept: specify media types which are acceptable for the response, ex:
                     "xml", "json"
    """
    world.product = product                                                                        # used in terrain.py for delete the product
    world.version = version                                                                        # used in terrain.py for delete the product
    world.env_requests.catalogue_addProductRelease (product, version, "description for token error", error, AcceptXML)



#--------------------------------------------------------------------------------------------------------

@step(u'I receive an? "([^"]*)" response with an? "([^"]*)" [?]*')
def i_receive_a_response_of_type(step, response_type, operation):
    status_code = http.status_codes[response_type]
    body_expected = world.env_requests.get_body_expected(response_type, operation)        # Read from body_message.py
    world.env_requests.check_response_status(world.response, status_code)
    world.env_requests.check_response_body(world.response, body_expected)
    pass


