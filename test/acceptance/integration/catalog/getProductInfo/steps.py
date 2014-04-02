# -*- coding: utf-8 -*-
from lettuce import step, world
from tools import http

## Constants
NOT_ERROR = ""
NOT_FOUND = "Not Found"
AcceptXML = "xml"

@step(u'the sdc is up and properly configured')
def the_sdc_is_up_and_properly_configured(step):
    """
    Nothing to do here, the set up should be done by external means
    :param step:
    """
    pass

@step(u'The "([^"]*)" has been created and the sdc is up and properly configured')
def the_Product_test_0001_has_been_created_and_the_sdc_is_up_and_properly_configured(step, product):
    """
    Add a new product into the catalog
    :param step:
    :param: product: new product
    """
    world.product = product                                                              # used in terrain.py for delete the product
    world.env_requests.catalogue_addProduct(product, "attributes_and_all_metadatas", None, NOT_ERROR, AcceptXML)

    pass

#------------------------------------- getProductList ----------------------------------------------
@step(u'I request the list of existing products in the catalog with "([^"]*)" content in the response')
def i_request_the_list_of_existing_products_in_the_catalog(step, Accept):
    """

    :param step:
    :param Accept:
    """
    world.env_requests.catalogue_getProductInfo("getProductList", None, NOT_ERROR, Accept)

@step(u'I request a wrong path when list of existing products in the catalog')
def i_request_a_wrong_path_when_list_of_existing_products_in_the_catalog(step):
    world.env_requests.catalogue_getProductInfo("getProductList", None, NOT_FOUND, AcceptXML)

@step(u'I request unauthorized errors "([^"]*)" when "([^"]*)" in the catalog')
def i_request_unauthorized_errors_when_operation_in_the_catalog(step, errorToken, operation):
    world.env_requests.catalogue_getProductInfo(operation, None, errorToken, AcceptXML)

@step(u'I list existing products in the catalog with request method is "([^"]*)"')
def i_list_existing_products_in_the_catalog_with_request_method_is_wrong (step, badMethod):
    world.env_requests.catalogue_getProductInfo("getProductList", None, badMethod, AcceptXML)

#------------------------------------- getDetails ----------------------------------------------
@step(u'I get details of a product "([^"]*)" in the catalog with "([^"]*)" content in the response')
def i_get_details_of_a_product_in_the_catalog(step, product, Accept):
    """
    I get details of an existent product in the catalog

    :param step:
    :param product:
    """
    world.product=product
    world.env_requests.catalogue_getProductInfo("getDetails", product, NOT_ERROR, Accept)

@step(u'I request a wrong path when get details of a product "([^"]*)" in the catalogue')
def i_request_a_wrong_path_when_get_details_of_a_product_in_the_catalogue(step, product):
    world.env_requests.catalogue_getProductInfo("getDetails", product, NOT_FOUND, AcceptXML)

@step(u'I try to get details of a product "([^"]*)" in the catalog with request method is "([^"]*)"')
def i_try_to_get_details_of_a_product_in_the_catalog_with_request_method(step, product, badMethod):
    """
    launch a request but with wrong method
    :param step:
    :param product:
    :param badMethod:
    """
    world.env_requests.catalogue_getProductInfo("getDetails", product, badMethod, AcceptXML)

#------------------------------------- getAttributes ----------------------------------------------
@step(u'I get attributes of a product "([^"]*)" in the catalog with "([^"]*)" content in the response')
def i_get_attributes_of_a_product_in_the_catalog(step, product, Accept):
    """
    I get attributes of an existent product in the catalog

    :param step:
    :param product:
    """
    world.product = product
    world.env_requests.catalogue_getProductInfo("getAttributes", product, NOT_ERROR, Accept)

@step(u'I request a wrong path when get attributes of a product "([^"]*)" in the catalogue')
def i_request_a_wrong_path_when_get_attributes_of_a_product_in_the_catalogue(step, product):
    world.env_requests.catalogue_getProductInfo("getAttributes", product, NOT_FOUND, AcceptXML)

@step(u'I try to get attributes of a product "([^"]*)" in the catalog with request method is "([^"]*)"')
def i_try_to_get_attributes_of_a_product_in_the_catalog_with_request_method(step, product, badMethod):
    """
    launch a request but with wrong method
    :param step:
    :param product:
    :param badMethod:
    """
    world.env_requests.catalogue_getProductInfo("getAttributes", product, badMethod, AcceptXML)



#------------------------------------- getMetadatas ----------------------------------------------
@step(u'I get metadatas of a product "([^"]*)" in the catalog with "([^"]*)" content in the response')
def i_get_metadatas_of_a_product_in_the_catalog(step, product, Accept):
    """
    I get metadatas of an existent product in the catalog
    :param step:
    :param product:
    """
    world.product = product
    world.env_requests.catalogue_getProductInfo("getMetadatas", product, NOT_ERROR, Accept)

@step(u'I request a wrong path when get metadatas of a product "([^"]*)" in the catalogue')
def i_request_a_wrong_path_when_get_metadatas_of_a_product_in_the_catalogue(step, product):
    """
    launch a request with error in path
    :param step:
    :param product:
    """
    world.env_requests.catalogue_getProductInfo("getMetadatas", product, NOT_FOUND, AcceptXML)

@step(u'I try to get metadatas of a product "([^"]*)" in the catalog with request method is "([^"]*)"')
def i_try_to_get_metadatas_of_a_product_in_the_catalog_with_request_method(step, product, badMethod):
    """
    launch a request but with wrong method
    :param step:
    :param product:
    :param badMethod:
    """
    world.env_requests.catalogue_getProductInfo("getMetadatas", product, badMethod, AcceptXML)

#------------------------------------- check ----------------------------------------------

@step(u'I receive an? "([^"]*)" response with an? "([^"]*)" [?]*')
def i_receive_a_response_of_type(step, response_type, operation):
    """
    Check Code status and body response against body_message.py
    :param step:
    :param response_type:
    :param operation:
    """
    status_code = http.status_codes[response_type]
    body_expected = world.env_requests.get_body_expected(response_type, operation)        # Read from body_message.py

    world.env_requests.check_response_status(world.response, status_code)
    world.env_requests.check_response_body(world.response, body_expected)
    world.env_requests.catalogue_deleteProduct(world.product, AcceptXML, NOT_ERROR)
    pass


@step(u'I check a.? "([^"]*)" response with a.? "([^"]*)" with "([^"]*)" with "([^"]*)" content')
def i_receive_a_response_of_type(step, response_type, operation, product, Accept):
    """
    Check Code status and body response against body_message.py
    :param step:
    :param response_type: code status
    :param operation: String to use in body_message.py
    :param Accept:
    """
    status_code = http.status_codes[response_type]
    body_expected = world.env_requests.get_body_expected(response_type, operation)        # Read from body_message.py
    body_expected = world.env_requests.change_product (body_expected, product, Accept)

    world.env_requests.check_response_status(world.response, status_code)
    world.env_requests.check_response_body(world.response, body_expected)
    world.env_requests.catalogue_deleteProduct(world.product, AcceptXML, NOT_ERROR)

    pass

