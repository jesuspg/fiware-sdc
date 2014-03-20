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
    world.product = product
    world.env_requests.catalogue_addProduct(product, "attributes_and_all_metadatas", None, NOT_ERROR, AcceptXML)
    pass

#---------------------------------- Add product release -------------------------------------------------
@step(u'I add a new release "([^"]*)" with description "([^"]*)" associated to product "([^"]*)" in the catalog with "([^"]*)" content in the response')
def i_add_a_new_release_with_description_associated_to_product_in_the_catalog(step, version, description, product, Accept):
    world.version = version                                                                       # used in terrain.py for delete the product
    world.env_requests.catalogue_addProductRelease (product, version, description, NOT_ERROR, Accept)

#--------------------------------------------------------------------------------------------------------

@step(u'I receive an? "([^"]*)" response with an? "([^"]*)" [?]*')
def i_receive_a_response_of_type(step, response_type, operation):
    status_code = http.status_codes[response_type]
    body_expected = world.env_requests.get_body_expected(response_type, operation)        # Read from body_message.py
    world.env_requests.check_response_status(world.response, status_code)
    world.env_requests.check_response_body(world.response, body_expected)
    pass


