__author__ = 'jfernandez'

from lettuce import world, step
from commons.rest_utils import RestUtils
from commons.constants import *
from commons.utils import dict_to_xml, response_body_to_dict, body_model_to_body_request
from commons.product_body import default_product, create_product_release, product_with_all_parameters
from nose.tools import assert_equals, assert_true, assert_false, assert_in

class ProductSteps ():

    api_utils = RestUtils()

    def __init__(self):
        None

    def a_created_product_with_name(self, step, product_name, metadatas=None):
        """
        Creates new product in SDC if not exists, using default values.
            - Product parameters:
                * Metadata: Default values if metadatas is None
                * Attributes: if exist in world.attributes - list
            - Assertions:
                * Product response is OK
        :param step: Lettuce step
        :param product_name: The name of the product
        :param metadatas: Metadata list to be used in the new product
        :return: None
        """

        response = self.api_utils.retrieve_product(headers=world.headers, product_id=product_name)
        if response.ok:
            print "WARNING: Product %s already exists. It will not be created." % product_name
        else:
            metadata_list = DEFAULT_METADATA[METADATA] if metadatas is None else metadatas
            if world.attributes is None:
                body_model = default_product(name=product_name, metadata=metadata_list)
            else:
                body_model = product_with_all_parameters(name=product_name, description='QA Test',
                                                         metadata=metadata_list, attributes=world.attributes)

            body = body_model_to_body_request(body_model, world.headers[CONTENT_TYPE],
                                              body_model_root_element=PRODUCT)

            response = self.api_utils.add_new_product(headers=world.headers, body=body)
            assert_true(response.ok, response.content)

    def a_created_release(self, step, product_name, product_version):
        """
        Creates new release for the product if not exists, using default values.
            - Assertions:
                * Release response is OK
        :param step: Lettuce step
        :param product_name: The name of the product
        :param product_version: The version for the product release
        :return: None
        """

        response = self.api_utils.retrieve_product_release_information(headers=world.headers, product_id=product_name,
                                                                       version=product_version)
        if response.ok:
            print "WARNING: Version %s for the product %s already exists. It will not be created." % \
                  (product_version, product_name)
        else:
            body_model = create_product_release(version=product_version)
            body = body_model_to_body_request(body_model, world.headers[CONTENT_TYPE],
                                              body_model_root_element=PRODUCT_RELEASE)
            response = self.api_utils.add_product_release(headers=world.headers, body=body, product_id=product_name)
            assert_true(response.ok, response.content)

    def a_created_product_with_name_and_release(self, step, product_name, product_version):
        """
        Creates new product in SDC with a release if not exists, using default values.
            - Product parameters:
                * Metadata: Default values
                * Attributes: if exists in world.attributes - list
            - Assertions:
                * Product response is OK
                * Release response is OK
        :param step: Lettuce step
        :param product_name: The name of the product
        :param product_version: The version for the product release
        :return: None
        """

        # Create product
        self.a_created_product_with_name(step, product_name)

        # Create release for that product
        self.a_created_release(step, product_name, product_version)

    def a_created_product_with_name_and_release_with_metadatas(self, step, product_name, product_version,
                                                               metadatas):
        """
        Creates new product with metadatas in SDC with a release if not exists, using default values.
            - Product parameters:
                * Attributes: if exists in world.attributes - list
            - Assertions:
                * Product response is OK
                * Release response is OK
        :param step: Lettuce step
        :param product_name: The name of the product
        :param product_version: The version for the product release
        :param metadatas: The metadatas to be used for the new product
        :return: None
        """

        # Create product
        self.a_created_product_with_name(step, product_name, metadatas=metadatas)

        # Create release for that product
        self.a_created_release(step, product_name, product_version)

    def a_created_product_with_name_and_release_list(self, step, product_name, product_version_list):
        """
        Creates new product in SDC with a release if not exists, using default values.
            - Product parameters:
                * Metadata: Default values
                * Attributes: if exists in world.attributes - list
            - Assertions:
                * Product response is OK
                * Release response is OK
        :param step: Lettuce step
        :param product_name: The name of the product
        :param product_version_list: The list of versions for the product to be created - list
        :return: None
        """

        # Create product
        self.a_created_product_with_name(step, product_name)

        for product_version in product_version_list:
            # Create release for that product
            self.a_created_release(step, product_name, product_version)