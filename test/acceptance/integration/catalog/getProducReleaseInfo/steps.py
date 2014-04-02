# -*- coding: utf-8 -*-
from lettuce import step, world
from time import sleep
from tools import http
from tools import catalogue_request

## errorTypes constants
NOT_ERROR = ""
NOT_FOUND = "Not Found"

AcceptXML = "xml"

@step(u'the sdc is up and properly configured')
def the_sdc_is_up_and_properly_configured(step):
    """

    :param step:
    """
    pass  # Nothing to do here, the set up should be done by external means

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

#------------------------------------- getProductList ----------------------------------------------
@step(u'I request releases list associated to a product "([^"]*)" in the catalog with "([^"]*)" content in the response')
def i_request_releases_list_associated_to_a_product_in_the_catalog(step, product, Accept):
    world.env_requests.catalogue_getProductReleaseInfo("getProductReleaseList", product, None, NOT_ERROR, Accept)
    pass

@step(u'I request a wrong path when list of existing products releases in the catalog')
def i_request_a_wrong_path_when_list_of_existing_products_in_the_catalog(step):
    world.env_requests.catalogue_getProductReleaseInfo("getProductReleaseList",world.product, None, NOT_FOUND, AcceptXML)

@step(u'I request unauthorized errors "([^"]*)" when "([^"]*)" in the catalog')
def i_request_unauthorized_errors_when_operation_in_the_catalog(step, errorToken, operation):
    world.env_requests.catalogue_getProductReleaseInfo(operation, world.product, None, errorToken, AcceptXML)

#------------------------------------- getDetails - not yet----------------------------------------------
@step(u'I get details of release "([^"]*)" associated to a product "([^"]*)" in the catalog with "([^"]*)" content in the response')
def i_get_details_of_a_product_in_the_catalog(step, version, product, Accept):
    """
    I get details of an existent product in the catalog

    :param step:
    :param product:
    """
    world.env_requests.catalogue_getProductReleaseInfo("getProductReleaseDetails", product, version,  NOT_ERROR, Accept)


@step(u'I request a wrong path when get details of a product "([^"]*)" in the catalogue')
def i_request_a_wrong_path_when_get_details_of_a_product_in_the_catalogue(step, product):
    world.env_requests.catalogue_getProductInfo("getDetails", product, NOT_FOUND)

@step(u'I try to get details of a product "([^"]*)" in the catalog with request method is "([^"]*)"')
def i_try_to_get_details_of_a_product_in_the_catalog_with_request_method(step, product, badMethod):
    world.env_requests.catalogue_getProductInfo("getDetails", product, badMethod)

#------------------------------------- check ----------------------------------------------
@step(u'I receive an? "([^"]*)" response with an? "([^"]*)" [?]*')
def i_receive_a_response_of_type(step, response_type, operation):
    status_code = http.status_codes[response_type]
    body_expected = world.env_requests.get_body_expected(response_type, operation)        # Read from body_message.py

    world.env_requests.check_response_status(world.response, status_code)
    world.env_requests.check_response_body(world.response, body_expected)
    world.env_requests.catalogue_deleteProductRelease(world.product, world.version, NOT_ERROR)
    world.env_requests.catalogue_deleteProduct(world.product, AcceptXML, NOT_ERROR)
    pass

@step(u'I receive an? "([^"]*)" response with a "([^"]*)" with "([^"]*)" with "([^"]*)" content')
def i_receive_a_response_of_type(step, response_type, operation, version, Accept):
    """
    Check Code status and body response against body_message.py
    :param step:
    :param response_type: code status
    :param operation: String to use in body_message.py
    :param version:
    :param Accept:
    """
    status_code = http.status_codes[response_type]
    body_expected = world.env_requests.get_body_expected(response_type, operation)        # Read from body_message.py
    body_expected = world.env_requests.change_version (body_expected, version, Accept)

    world.env_requests.check_response_status(world.response, status_code)
    world.env_requests.check_response_body(world.response, body_expected)

    world.env_requests.catalogue_deleteProductRelease(world.product, world.version, NOT_ERROR)  # delete each version
    world.env_requests.catalogue_deleteProduct(world.product, AcceptXML, NOT_ERROR)
    pass




