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

AcceptXML= "xml"

@step(u'The "([^"]*)" has been created and the sdc is up and properly configured')
def the_Product_test_0001_has_been_created_and_the_sdc_is_up_and_properly_configured(step, product):
    world.product = product                                                                # used in terrain.py for delete the product
    world.env_requests.catalogue_addProduct(product, "attributes_and_all_metadatas", None, NOT_ERROR, AcceptXML)
    pass

@step(u'the sdc is up and properly configured')
def the_sdc_is_up_and_properly_configured(step):
     pass  # Nothing to do here, the set up should be done by external means

@step(u'I delete a product "([^"]*)" in the catalog')
def i_delete_a_product_in_the_catalog(step, product):
    """
    I delete an existent product in the catalog

    :param step:
    :param product:
    """
    world.env_requests.catalogue_deleteProduct(product, NOT_ERROR)

@step(u'I request a wrong path when delete a product "([^"]*)" in the catalogue')
def i_request_a_wrong_path_when_get_details_of_a_product_in_the_catalogue(step, product):
    world.env_requests.catalogue_deleteProduct(product, NOT_FOUND)

@step(u'I receive an? "([^"]*)" response with an? "([^"]*)" [?]*')
def i_receive_a_response_of_type(step, response_type, operation):
    status_code = http.status_codes[response_type]
    body_expected = world.env_requests.get_body_expected(response_type, operation)        # Read from body_message.py

    world.env_requests.check_response_status(world.response, status_code)
    world.env_requests.check_response_body(world.response, str(body_expected))
    print "-----------------------------------------------------------------------------------------------------------------------------------"+ world.product
    world.env_requests.catalogue_deleteProduct(world.product, NOT_ERROR)
    pass


