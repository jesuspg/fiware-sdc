# -*- coding: utf-8 -*-
from lettuce import step, world
from tools import http


## Constants
NOT_ERROR = ""
NOT_FOUND = "Not Found"
AcceptXML = "xml"


@step(u'The "([^"]*)" has been created and the sdc is up and properly configured')
def the_Product_test_0001_has_been_created_and_the_sdc_is_up_and_properly_configured(step, product):
    """
    Add a new product into the catalog
    :param step:
    :param product:
    """
    world.product = product                                                                # used in terrain.py for delete the product
    world.env_requests.catalogue_addProduct(product, "attributes_and_all_metadatas", None, NOT_ERROR, AcceptXML)
    pass

@step(u'the sdc is up and properly configured')
def the_sdc_is_up_and_properly_configured(step):
    """
    Nothing to do here, the set up should be done by external means
    :param step:
    """
    pass  #

@step(u'I delete a product "([^"]*)" in the catalog with "([^"]*)" content in the response')
def i_delete_a_product_in_the_catalog(step, product, content):
    """
    I delete an existent product in the catalog

    :param step:
    :param product:
    """
    world.product = product
    world.env_requests.catalogue_deleteProduct(product, content, NOT_ERROR)

@step(u'I request a wrong path when delete a product "([^"]*)" in the catalogue')
def i_request_a_wrong_path_when_get_details_of_a_product_in_the_catalogue(step, product):
    world.product = product
    world.env_requests.catalogue_deleteProduct(product, AcceptXML, NOT_FOUND)


@step(u'I request unauthorized errors "([^"]*)" when delete a product Product_test_0001')
def i_request_unauthorized_error(step, error):
    """
    Add a new product
    :param step:
    :param product: It is the product name that it will be created
    :param label: configuration of values into the new product, ex:
                   "only name",  "attributes", "attributes_and_all_metadatas", "metadata_installator", etc.
    :param Accept: specify media types which are acceptable for the response, ex:
                     "xml", "json"
    """
    world.product = "Product_test_0001"                                       # used in terrain.py for delete the product
    world.env_requests.catalogue_deleteProduct(world.product, AcceptXML, error)


@step(u'I receive an? "([^"]*)" response with an? "([^"]*)" [?]*')
def i_receive_a_response_of_type(step, response_type, operation):
    status_code = http.status_codes[response_type]
    body_expected = world.env_requests.get_body_expected(response_type, operation)        # Read from body_message.py

    world.env_requests.check_response_status(world.response, status_code)
    world.env_requests.check_response_body(world.response, str(body_expected))

    world.env_requests.catalogue_deleteProduct(world.product, AcceptXML, NOT_ERROR)
    pass


