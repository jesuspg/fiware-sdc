# -*- coding: utf-8 -*-
from lettuce import step, world
from tools import http
from tools import utils

from tools.catalogue_request import CatalogueRequest


## errorTypes constants
NOT_ERROR = ""
NOT_FOUND = "Not Found"

AcceptXML = "xml"

#-----------------------------------------------------------------------------------
@step(u'I add a new product "([^"]*)" and release "([^"]*)" with "([^"]*)" and "([^"]*)" in the catalog with "([^"]*)" content in the response')
def the_product_and_your_release_are_created(step, product, release, metadataLabel, metadataValue, content):
    """
    Create a new product and a new release
    :param step:
    :param product: product name
    :param version: version name
    """
    world.product = product                                                                        # used in terrain.py for delete the product
    world.version = release                                                                        # used in terrain.py for delete the release
    world.catalogue.catalogue_addProduct(product, metadataLabel, metadataValue, NOT_ERROR, content)
    world.catalogue.catalogue_addProductRelease (product, release, "only for test", NOT_ERROR, content)
    pass
#---------------------------------- uninstall  -------------------------------------------------
@step(u'Install a product "([^"]*)" with release "([^"]*)" in a VM "([^"]*)" with hostname "([^"]*)" and OS "([^"]*)" with "([^"]*)" content')
def install_a_product_with_release_in_a_VM_with_content(step, product, release, IP, hostname, osType, content):
    """
    Install a product in a VM
    :param step:
    :param product: It is the product name that it will be installed
    :param release: it is the release associate to product
    :param IP: VM IP
    :param Accept: specify media types which are acceptable for the response, ex:
                     "xml", "json"
    """
    world.env_requests.provisioning_installProduct(IP, hostname, osType, product, release, NOT_ERROR, content)
    pass


@step(u'Uninstall a product "([^"]*)" with release "([^"]*)" and fqn "([^"]*)" with "([^"]*)" content')
def uninstall_a_product_with_release_and_fqn_with_content(step, product, release, fqn, content):
    """
    Uninstall a product in a VM
    :param step:
    :param product: It is the product name that it will be installed
    :param release: it is the release associate to product
    :param fqn:
    :param Accept: specify media types which are acceptable for the response, ex:
                     "xml", "json"
    """
    world.env_requests.provisioning_uninstallProduct(product, release, fqn, NOT_ERROR, content)
    pass


#---------------------------------------------------------------------------------------------------
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
    utils.check_response_status(world.response, status_code)
    utils.check_response_body(world.response, body_expected)
    pass



