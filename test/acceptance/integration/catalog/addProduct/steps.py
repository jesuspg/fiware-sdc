# -*- coding: utf-8 -*-
from lettuce import step, world
from tools import http
from tools import utils


# constants
NOT_ERROR = ""
NOT_FOUND = "Not Found"

AcceptXML = "xml"

#-----------------------------------------------------------------------------------
@step(u'the sdc is up and properly configured')
def the_sdc_is_up_and_properly_configured(step):
    """
    Nothing to do here, the set up should be done by external means
    :param step:
    """
    pass
#---------------------------------- Add -------------------------------------------------
@step(u'I add a new product "([^"]*)" with "([^"]*)" in the catalog with "([^"]*)" content in the response')
def i_add_a_new_product_in_the_catalog(step, product, label, Accept):
    """
    Add a new product
    :param step:
    :param product: It is the product name that it will be created
    :param label: configuration of values into the new product, ex:
                   "only name",  "attributes", "attributes_and_all_metadatas", "metadata_installator", etc.
    :param Accept: specify media types which are acceptable for the response, ex:
                     "xml", "json"
    """
    world.product = product                                        # used in terrain.py for delete the product
    world.env_requests.catalogue_addProduct(product, label, None, NOT_ERROR, Accept)

@step(u'With metadatas I add a new product "([^"]*)" with "([^"]*)" and "([^"]*)" in the catalog with "([^"]*)" content in the response')
def with_metadatas_i_add_a_new_product_with_label_and_value_in_the_catalog(step, product, label, value, Accept):
    """
    Add a new product metadata alone
    :param step:
    :param product: It is the product name that it will be created
    :param label: configuration of values into the new product, ex:
                   "metadata_installator", "metadata_cloud", etc.
    :param value: It is the value for metadata key
    :param Accept: specify media types which are acceptable for the response, ex:
                     "xml", "json"
    """
    world.product = product                                        # used in terrain.py for delete the product
    world.env_requests.catalogue_addProduct(product, label, value, NOT_ERROR, Accept)

@step(u'I request a wrong path when add a new product "([^"]*)" with "([^"]*)" in the catalog')
def i_request_a_wrong_path_when_add_a_new_product_in_the_catalog(step, product, label):
    """
    try to Add a new product, but I cause an url not found
    :param step:
    :param product: It is the product name that it will be created
    :param label: configuration of values into the new product, ex:
                   "only name", etc.

    """
    world.product = product                                        # used in terrain.py for delete the product
    world.env_requests.catalogue_addProduct(product, label, None, NOT_FOUND, AcceptXML)


@step(u'I request unauthorized errors "([^"]*)" when add a new product Product_test_0001 Without Name Label in the catalog with xml content in the response')
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
    world.env_requests.catalogue_addProduct("Product_test_0001", "Without Name Label", None, error, AcceptXML)

@step(u'I receive an? "([^"]*)" response with an? "([^"]*)" [?]*')
def i_receive_a_response_of_type(step, response_type, operation):
    """
    Verify code and body response and compare against body_message.py
    :param step:
    :param response_type: type of response expected
    :param operation: determine type of body message, ex:
                        "add Product only name XML", "add Product only name JSON", etc
    """
    status_code = http.status_codes[response_type]
    body_expected = utils.get_body_expected(response_type, operation)        # Read from body_message.py
    world.env_requests.check_response_status(world.response, status_code)
    world.env_requests.check_response_body(world.response, body_expected)
    world.env_requests.catalogue_deleteProduct(world.product, AcceptXML, NOT_ERROR)
    pass


