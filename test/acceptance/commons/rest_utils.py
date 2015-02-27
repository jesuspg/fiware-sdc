# -*- coding: utf-8 -*-
# Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U
#
# This file is part of FI-WARE project.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
#
# You may obtain a copy of the License at:
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#
# See the License for the specific language governing permissions and
# limitations under the License.
#
# For those usages not covered by the Apache version 2.0 License please
# contact with opensource@tid.es

__author__ = 'arobres, jfernandez'

from json import JSONEncoder
from configuration import SDC_IP, SDC_PORT, SDC_PROTOCOL, CONFIG_PUPPETDB_PROTOCOL, CONFIG_PUPPETDB_IP,\
    CONFIG_PUPPETDB_PORT
from constants import *

import requests

SDC_SERVER = '{}://{}:{}'.format(SDC_PROTOCOL, SDC_IP, SDC_PORT)
PRODUCT_PATTERN_ROOT = '{url_root}/sdc/rest/catalog/product/'
PRODUCT_PATTERN = '{url_root}/sdc/rest/catalog/product/{product_id}'
PRODUCT_RELEASE_PATTERN = '{url_root}/sdc/rest/catalog/product/{product_id}/release'
VERSION_RELEASE_PATTERN = '{url_root}/sdc/rest/catalog/product/{product_id}/release/{version}'
PRODUCT_ATTRIBUTES_PATTERN = '{url_root}/sdc/rest/catalog/product/{product_id}/attributes'
PRODUCT_METADATA_LIST_PATTERN = '{url_root}/sdc/rest/catalog/product/{product_id}/metadatas'
PRODUCT_METADATA_PATTERN = '{url_root}/sdc/rest/catalog/product/{product_id}/metadatas/{metadata_key}'
PRODUCTANDRELEASE_PATTERN_ROOT = '{url_root}/sdc/rest/catalog/productandrelease/'
INSTALL_PATTERN = '{url_root}/sdc/rest/vdc/{vdc_id}/productInstance'
PRODUCT_INSTALLED_PATTERN = '{url_root}/sdc/rest/vdc/{vdc_id}/productInstance/{product_id}'
TASK_PATTERN_ROOT = '{url_root}/sdc/rest/vdc/{vdc_id}/task'
TASK_PATTERN = "{url_root}/sdc/rest/vdc/{vdc_id}/task/{task_id}"
NODE_PATTERN_ROOT = "{url_root}/sdc/rest/vdc/{vdc_id}/chefClient"
NODE_PATTERN = "{url_root}/sdc/rest/vdc/{vdc_id}/chefClient/{node_name}"
METADATA_PATTERN = '{url_root}/sdc/rest/catalog/product/{product_id}'

#PuppetDB
PUPPETDB_ROOT_PATTERN = '{}://{}:{}'.format(CONFIG_PUPPETDB_PROTOCOL, CONFIG_PUPPETDB_IP, CONFIG_PUPPETDB_PORT)
PUPPETDB_NODE_PATTERN_ROOT = '{url_root}/v3/nodes'

requests.packages.urllib3.disable_warnings()


class RestUtils(object):

    def __init__(self):
        """Initialization method
        """

        self.api_url = SDC_SERVER
        self.encoder = JSONEncoder()

    def _call_api(self, pattern, method, body=None, headers=None, payload=None, **kwargs):

        """Launch HTTP request to Policy Manager API with given arguments
        :param pattern: string pattern of API url with keyword arguments (format string syntax)
        :param method: HTTP method to execute (string)
        :param body: JSON body content (dict)
        :param headers: HTTP header request (dict)
        :param payload: Query parameters for the URL
        :param **kwargs: URL parameters (without url_root) to fill the patters
        :returns: REST API response
        """

        kwargs['url_root'] = self.api_url

        url = pattern.format(**kwargs)

        #print "==============="
        #print "### REQUEST ###"
        #print 'METHOD: {}\nURL: {} \nHEADERS: {} \nBODY: {}'.format(method, url, headers, self.encoder.encode(body))

        try:
            if headers[CONTENT_TYPE] == CONTENT_TYPE_JSON:
                r = requests.request(method=method, url=url, data=self.encoder.encode(body), headers=headers,
                                     params=payload, verify=False)
            else:
                r = requests.request(method=method, url=url, data=body, headers=headers, params=payload, verify=False)

        except Exception, e:
            print "Request {} to {} crashed: {}".format(method, url, str(e))
            return None

        #print "### RESPONSE ###"
        #print "HTTP RESPONSE CODE:", r.status_code
        #print 'HEADERS: {} \nBODY: {}'.format(r.headers, r.content)

        return r

    def add_new_product(self, headers=None, body=None):

        return self._call_api(pattern=PRODUCT_PATTERN_ROOT, method='post', headers=headers, body=body)

    def retrieve_product(self, headers=None, product_id=None):

        return self._call_api(pattern=PRODUCT_PATTERN, method='get', headers=headers, product_id=product_id)

    def retrieve_product_attributes(self, headers=None, product_id=None):

        return self._call_api(pattern=PRODUCT_ATTRIBUTES_PATTERN, method='get', headers=headers, product_id=product_id)

    def retrieve_product_metadatas(self, headers=None, product_id=None):

        return self._call_api(pattern=PRODUCT_METADATA_LIST_PATTERN, method='get', headers=headers, product_id=product_id)

    def retrieve_product_metadata(self, product_id, metadata_key, headers=None, method='get'):

        return self._call_api(pattern=PRODUCT_METADATA_PATTERN, method=method, headers=headers, product_id=product_id,
                              metadata_key=metadata_key)

    def delete_product_metadata(self, product_id, metadata_key, headers=None):

        return self._call_api(pattern=PRODUCT_METADATA_PATTERN, method='delete', headers=headers, product_id=product_id,
                              metadata_key=metadata_key)

    def update_product_metadata(self, body, product_id, metadata_key, headers=None):

        return self._call_api(pattern=PRODUCT_METADATA_PATTERN, method='put', headers=headers, product_id=product_id,
                              metadata_key=metadata_key, body=body)

    def delete_product(self, headers=None, product_id=None):

        return self._call_api(pattern=PRODUCT_PATTERN, method='delete', headers=headers, product_id=product_id)

    def retrieve_product_list(self, headers=None):

        return self._call_api(pattern=PRODUCT_PATTERN_ROOT, method='get', headers=headers)

    def add_product_release(self, headers=None, body=None, product_id=None):

        return self._call_api(pattern=PRODUCT_RELEASE_PATTERN, method='post', headers=headers, product_id=product_id,
                              body=body)

    def delete_product_release(self, headers=None, product_id=None, version=None):

        return self._call_api(pattern=VERSION_RELEASE_PATTERN, method='delete', headers=headers, product_id=product_id,
                              version=version)

    def retrieve_product_release_information(self, headers=None, product_id=None, version=None):

        return self._call_api(pattern=VERSION_RELEASE_PATTERN, method='get', headers=headers, product_id=product_id,
                              version=version)

    def retrieve_product_release_list(self, headers=None, product_id=None):

        return self._call_api(pattern=PRODUCT_RELEASE_PATTERN, method='get', headers=headers, product_id=product_id)

    def install_product(self, headers=None, vdc_id=None, body=None):

        return self._call_api(pattern=INSTALL_PATTERN, method='post', headers=headers, vdc_id=vdc_id, body=body)

    #TODO: @deprecated
    def uninstall_product(self, headers=None, product_id=None, vdc_id=None, fqn=''):

        return self._call_api(pattern=PRODUCT_INSTALLED_PATTERN, method='delete', headers=headers, vdc_id=vdc_id,
                              product_id="{}_{}".format(fqn, product_id))

    #TODO: @deprecated
    def retrieve_list_products_installed(self, headers=None, vdc_id=None,):

        return self._call_api(pattern=INSTALL_PATTERN, method='get', headers=headers, vdc_id=vdc_id)

    #TODO: @deprecated
    def retrieve_product_installed_information(self, headers=None, product_id=None, vdc_id=None, fqn=''):

        return self._call_api(pattern=PRODUCT_INSTALLED_PATTERN, method='get', headers=headers, vdc_id=vdc_id,
                              product_id="{}_{}".format(fqn, product_id))

    #TODO: @deprecated. Should be renamed when uninstall_product is deleted
    def uninstall_product_by_product_instance_id(self, headers=None, vdc_id=None, product_instance_id=None):

        return self._call_api(pattern=PRODUCT_INSTALLED_PATTERN, method='delete', headers=headers, vdc_id=vdc_id,
                              product_id=product_instance_id)

    def retrieve_task(self, headers=None, vdc_id=None, task_id=None):

        return self._call_api(pattern=TASK_PATTERN, method='get', headers=headers, vdc_id=vdc_id, task_id=task_id)

    def retrieve_product_instance_list(self, headers=None, vdc_id=None):

        return self._call_api(pattern=INSTALL_PATTERN, method='get', headers=headers, vdc_id=vdc_id)

    def retrieve_product_instance(self, headers=None, vdc_id=None, product_instance_id=None):

        return self._call_api(pattern=PRODUCT_INSTALLED_PATTERN, method='get', headers=headers, vdc_id=vdc_id,
                              product_id=product_instance_id)

    def retrieve_productandrelease_list(self, headers=None):
        return self._call_api(pattern=PRODUCTANDRELEASE_PATTERN_ROOT, method='get', headers=headers)

    def request_productandrelease(self, headers=None, method=None):
        return self._call_api(pattern=PRODUCTANDRELEASE_PATTERN_ROOT, method=method, headers=headers)

    def retrieve_node_list(self, headers, vdc_id):
        return self._call_api(pattern=NODE_PATTERN_ROOT, method='get', headers=headers, vdc_id=vdc_id)

    def delete_node(self, headers, vdc_id, node_name):
        return self._call_api(pattern=NODE_PATTERN, method='delete', headers=headers, vdc_id=vdc_id,
                              node_name=node_name)

    def retrieve_puppetdb_node_list(self):
        """
        This method gets the list of registered nodes from PuppetDB
        :return: REST API response (Requests lib)
        """
        url = PUPPETDB_NODE_PATTERN_ROOT.format(url_root=PUPPETDB_ROOT_PATTERN)
        return requests.request(method='get', url=url, verify=False)

    @staticmethod
    def call_url_task(method=None, headers=None, url=None):

        try:
            r = requests.request(method=method, url=url, headers=headers)

        except Exception, e:
            print "Request {} to {} crashed: {}".format(method, url, str(e))
            return None

        return r

    def _uninstall_product_if_installed(self, product, headers):
        if product[STATUS] == TASK_STATUS_VALUE_INSTALLED:
            response = self.uninstall_product_by_product_instance_id(headers=headers,
                                                                     vdc_id=headers[TENANT_ID_HEADER],
                                                                     product_instance_id=product[PRODUCT_NAME])
            assert response.ok

    def uninstall_all_products(self, headers=None):
        response = self.retrieve_product_instance_list(headers, headers[TENANT_ID_HEADER])
        products_installed_body = response.json()

        if not isinstance(products_installed_body, list):
            self._uninstall_product_if_installed(products_installed_body, headers)
        else:
            for product in products_installed_body:
                self._uninstall_product_if_installed(product, headers)

    def delete_all_testing_products(self, headers=None):

        response = self.retrieve_product_list(headers=headers)
        assert response.ok
        try:
            product_list = response.json()
        except:
            assert response.content == 'null'
            return

        if not isinstance(product_list, list):
            if ('testing' in product_list[PRODUCT_NAME] or 'qa-test' in product_list[PRODUCT_NAME]) \
                    and 'testing_prov_' not in product_list[PRODUCT_NAME]:
                delete_response = self.delete_product(headers=headers, product_id=product_list[PRODUCT_NAME])

                if not delete_response.ok:
                    release_list = self.retrieve_product_release_list(headers=headers,
                                                                      product_id=product_list[PRODUCT_NAME])
                    release_list = release_list.json()
                    print "RELEASE LIST: {}".format(release_list)
                    delete_release = self.delete_product_release(headers=headers, product_id=product_list[PRODUCT_NAME],
                                                                 version=release_list[VERSION])
                    #assert delete_release.ok
                    delete_response = self.delete_product(headers=headers, product_id=product_list[PRODUCT_NAME])
                    #assert delete_response.ok

        else:
            for product in product_list:
                if ('testing' in product[PRODUCT_NAME] or 'qa-test' in product[PRODUCT_NAME]) \
                        and 'testing_prov_' not in product[PRODUCT_NAME]:

                    delete_response = self.delete_product(headers=headers, product_id=product[PRODUCT_NAME])

                    if not delete_response.ok:

                        release_list = self.retrieve_product_release_list(headers=headers,
                                                                          product_id=product[PRODUCT_NAME])
                        release_list = release_list.json()

                        if not isinstance(release_list, list):

                            delete_release = self.delete_product_release(headers=headers,
                                                                         product_id=product[PRODUCT_NAME],
                                                                         version=release_list[VERSION])
                            #assert delete_release.ok, delete_release.content
                            delete_response = self.delete_product(headers=headers, product_id=product[PRODUCT_NAME])
                            #assert delete_response.ok

                        else:

                            for release in release_list:

                                delete_release = self.delete_product_release(headers=headers,
                                                                             product_id=product[PRODUCT_NAME],
                                                                             version=release[VERSION])
                                #assert delete_release.ok, delete_release.content
                                delete_response = self.delete_product(headers=headers, product_id=product[PRODUCT_NAME])
                                #assert delete_response.ok
