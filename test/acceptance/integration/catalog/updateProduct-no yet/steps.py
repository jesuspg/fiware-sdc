# -*- coding: utf-8 -*-
from lettuce import step, world
from time import sleep
import random
from tdaf_lettuce_tools.dataset_utils.dataset_utils import DatasetUtils
from tools import http
#from tools.environment_request import EnvironmentRequest
from tools import catalogue_request

## errorTypes constants
NOT_ERROR = ""
NOT_FOUND = "Not Found"

#-----------------------------------------------------------------------------------
@step(u'the sdc is up and properly configured')
def the_sdc_is_up_and_properly_configured(step):
    """

    :param step:
    """

    pass  # Nothing to do here, the set up should be done by external means
#---------------------------------- Add -------------------------------------------------
@step(u'I add a new product "([^"]*)" in the catalog')
def i_add_a_new_product_in_the_catalog(step, product):
    world.product = product                                         # used in terrain.py for to delete the product
    world.env_requests.catalogue_addProduct(product, NOT_ERROR)

@step(u'I receive an? "([^"]*)" response with an? "([^"]*)" [?]*')
def i_receive_a_response_of_type(step, response_type, operation):
    status_code = http.status_codes[response_type]
    body_expected = world.env_requests.get_body_expected(response_type, operation)        # Read from body_message.py

    world.env_requests.check_response_status(world.response, status_code)
    world.env_requests.check_response_body(world.response, body_expected)
    pass


