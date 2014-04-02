# -*- coding: utf-8 -*-
__author__ = 'ivanl@tid.es'

from lettuce import world
from tools import body_message
import utils
import http


class CatalogueRequest:
    """
    Manage products and releases in a catalogue.
    """
    catalogURL = "catalog/product"

    #Bodies
    ADD_PRODUCT_BODY         = ""
    ADD_PRODUCT_RELEASE_BODY = ""

    def __init__(self, keystone_url, paas_manager_url, tenant, user, password, vdc, sdc_url):
        """
        constructor
        """
        self.paasmanager_url = paas_manager_url
        self.sdc_url = sdc_url
        self.vdc = vdc
        self.keystone_url = keystone_url

        self.user = user
        self.password = password
        self.tenant = tenant

        self.token = self.__get__token()

    def __init_ADD_PRODUCT_Body(self, content):
        """
        initializes add product body
        :param content: "xml" or "json"
        """
        if content == 'xml':
            self.ADD_PRODUCT_BODY = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>  <product>  </product>'
        elif content == 'json':
            self.ADD_PRODUCT_BODY = '{}'

    def __init_ADD_PRODUCT_RELEASE_Body(self, content):
        """
        initializes add product release body
        :param content: "xml" or "json"
        """
        if content == 'xml':
            self.ADD_PRODUCT_RELEASE_BODY = '<productReleaseDto></productReleaseDto>'
        elif content == 'json':
            self.ADD_PRODUCT_RELEASE_BODY = '{}'

    def __get__token(self):
        """
        get token
        :return: token
        """
        return http.get_token(self.keystone_url + '/tokens', self.tenant, self.user, self.password)

    def __get__url (self, operation, product, version=None):
        """
        return URL for each operation
        :param operation:
        :param product:
        :param version:
        :return:
        """
        if operation == "getProductList" or operation == "addProduct":
            return "%s/%s" % (self.sdc_url, self.catalogURL)
        elif operation == "getDetails" or operation == "deleteProduct":
            return "%s/%s/%s" % (self.sdc_url, self.catalogURL, product)
        elif operation == "getAttributes":
            return "%s/%s/%s/%s" % (self.sdc_url, self.catalogURL, product, "attributes")
        elif operation == "getMetadatas":
            return "%s/%s/%s/%s" % (self.sdc_url, self.catalogURL, product, "metadatas")
        elif operation == "addProductRelease" or operation == "getProductReleaseList":
            return "%s/%s/%s/%s" % (self.sdc_url, self.catalogURL, product, "release")
        elif operation == "deleteProductRelease" or operation == "getProductReleaseDetails":
            return "%s/%s/%s/%s/%s" % (self.sdc_url, self.catalogURL, product, "release", version)

    def __get__headers(self,  content="xml"):
        """
         return header
        :param Accept: :param content: "xml" or "json"
        :return:
        """
        contentAccept = content
        contentType = content
        if content == "error in Accept":
            contentAccept = "sdfdfsdf"
            contentType = "xml"
        if content == "error in Content-Type":
            contentType = "sdfdfsdf"
            contentAccept = "xml"
        return {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc, 'Accept': "application/"+contentAccept, "Content-Type":"application/"+contentType}

    def __request(self, method, url,  headers, body,  error):
        """
        Launch a request and returns its response
        :param method: method used ex: POST, GET, DELETE, etc
        :param url: <IP>:<port>/<path>
        :param headers: headers used
        :param body: body in case of POST method
        :param error: error types
        :return: response
        """
        headers['X-Auth-Token'] = utils.errorLabel (headers['X-Auth-Token'], error)
        url = utils.errorUrl(url, error)

        if error == "GET" or error == "PUT" or error == "POST" or error == "DELETE":
            method = error

        if method == "GET":
            response = http.get(url, headers)
        elif method == "POST":
            response = http.post(url, headers, body)
        elif method == "PUT":
            response =  http.put(url, headers, body)
        elif method == "DELETE":
            response = http.delete(url, headers)

        #utils.printRequest(method,url,headers,body)                 # show request
        #utils.printResponse(response)                               # show response
        return response

    def __set_body_name (self, product, content):
        """
         add name and description before the end marker of request
        :param product: product name
        :param content:
        """
        element_1 = {'label': 'name',        'value': product}
        element_2 = {'label': 'description', 'value': "Product only for test"}
        self.ADD_PRODUCT_BODY = utils.body_oneElement (self.ADD_PRODUCT_BODY, element_1, 'addProduct', content)
        self.ADD_PRODUCT_BODY = utils.body_oneElement (self.ADD_PRODUCT_BODY, element_2, 'addProduct', content)

    def __set_body_attributes (self, content):
        """
        add attribute before the end marker of request
        :param content: "xml" or "json"
        """
        for id in range(0, len(body_message.ATTRIBUTES)):
            self.ADD_PRODUCT_BODY = utils.body_elements(self.ADD_PRODUCT_BODY, body_message.ATTRIBUTES[id], "attributes", "addProduct", content)

    def __set_body_metadata (self, meta, value, content):
        """
        add metadata before the end marker of request
        :param meta: label for metadata (key)
        :param value: value for metadata
        :param content: "xml" or "json"
        """
        if meta == "all": metadata = body_message.ALL_METADATAS
        else:
            key = meta[len("metadata_"):]
            metadata = [[{'label': 'key', 'value': key}, {'label': 'value', 'value': value}]]
        for id in range(0, len(metadata)):
            self.ADD_PRODUCT_BODY = utils.body_elements(self.ADD_PRODUCT_BODY, metadata[id], "metadatas", "addProduct", content)

    def __create_body_add (self, product, label, metadataValue, content):
        """
        Create body to Add request
        :param product:
        :param label:
        :param metadataValue:
        :param content:
        """
        self.__set_body_name(product, content)
        if label == "attributes" or label == "attributes_and_all_metadatas":
            self.__set_body_attributes(content)
            if label == "attributes_and_all_metadatas":
                self.__set_body_metadata("all", None, content)
        elif label.find("metadata_") == 0:
            self.__set_body_metadata(label, metadataValue, content)

    def __create_body_release (self, version, description, content):
        """
        Create body to release request
        :param version:
        :param description:
        :param content:
        """
        element_1 = {'label': 'version',     'value': version}
        element_2 = {'label': 'releaseNotes', 'value': "version only for test"}

        self.ADD_PRODUCT_RELEASE_BODY = utils.body_oneElement (self.ADD_PRODUCT_RELEASE_BODY, element_1, 'addProductRelease', content)
        self.ADD_PRODUCT_RELEASE_BODY = utils.body_oneElement (self.ADD_PRODUCT_RELEASE_BODY, element_2, 'addProductRelease', content)

    def catalogue_getProductInfo(self, searchType, product, errorType, content):
        """
        List all products in catalogue
        Returns all details of a Product
        Returns all attributes of a Product
        Delete an existent product

        :param method: define which protocol method are using
        :param product: define the product used
        :param errorType: definition of several error caused. Ex: Not Found, bad Method, unauthorized, etc.
        """
        world.response = self.__request("GET", self.__get__url(searchType,product),self.__get__headers(content), None, errorType)

    def catalogue_addProduct(self, product, label, metadataValue,  errorType, content):
        """
        Add a new product in catalogue
        :param method: define which protocol method are using
        :param product: product name that it will created
        :param metadataValue: In case that you are adding metadatas, it is for the value metadata
        :param errorType: definition of several error caused. Ex: Not Found, bad Method, unauthorized, etc.
        """
        self.__init_ADD_PRODUCT_Body(content)
        if label != "Without Name Label":
            self. __create_body_add(product, label,  metadataValue, content)

        world.response = self.__request("POST", self.__get__url("addProduct", None),self.__get__headers(content), self.ADD_PRODUCT_BODY, errorType)

    def catalogue_deleteProduct(self, product, content, errorType):
        """
        Delete a product in catalogue

        :param method: define which protocol method are using
        :param errorType: definition of several error caused. Ex: Not Found, bad Method, unauthorized, etc.
        """
        world.response = self.__request("DELETE", self.__get__url("deleteProduct", product, None),self.__get__headers(content), None, errorType)

    def catalogue_addProductRelease (self, product,  version, description, errorType, content):
        """
        add a new release to product
        :param product:
        :param version:
        :param description:
        :param errorType:
        :param content:
        """
        self.__init_ADD_PRODUCT_RELEASE_Body(content)
        if version is not None:
            self.__create_body_release(version, description, content)

        world.response = self.__request("POST", self.__get__url("addProductRelease", product),self.__get__headers(content), self.ADD_PRODUCT_RELEASE_BODY, errorType)

    def catalogue_deleteProductRelease (self, product, version, errorType):
        """
        launch a request to delete a product releases
        :param product:
        :param version:
        :param errorType:
        """
        world.response = self.__request("DELETE", self.__get__url("deleteProductRelease", product, version),self.__get__headers(), None, errorType)

    def catalogue_getProductReleaseInfo(self, searchType, product, version, errorType, Accept):
        """
        launch a request to list all product releases or only one product release
        :param searchType:
        :param product:
        :param version:
        :param errorType:
        :param Accept:
        """
        world.response = self.__request("GET", self.__get__url(searchType,product, version),self.__get__headers(searchType, Accept), None, errorType)

    def get_body_expected(self, response_type, operation):
        for ID in body_message.Catalog_body:
            if ID["operation"] == operation and ID["code"] == response_type:
                return ID["body"]

    def change_version (self, body_expected, version, Accept):
        """
        Add version name in response for verify in body
        :param body_expected:
        :param version:
        :param Accept:
        :return:
        """
        if Accept == "xml":
            label = '</version>'
        elif Accept == "json":
            label = '","product"'
        else:
            return body_expected
        return utils.insert_label (body_expected, label, version)

    def change_product (self, body_expected, product, Accept):
        """
        Add product name in response for verify in body
        :param body_expected:
        :param product:
        :param Accept:
        :return:
        """
        if Accept == "xml":
            label = '</name>'
        elif Accept == "json":
            label = '","description"'
        else:
            return body_expected
        return utils.insert_label (body_expected, label, product)

    def check_response_status(self, response, expected_status_code):
        """
        Checks that the response status is the expected one.
        :param response: Response to be checked.
        :param expected_status_code: Expected status code of the response.
        """
        assert response.status == expected_status_code, \
        "Wrong status code received: %d. Expected: %d. \n\nBody content: %s" \
        % (response.status, expected_status_code, response.read())

    def check_response_body(self, response, expected_body):
        """
        Checks that the response body is the expected one.
        :param response: Response to be checked.
        :param expected_body: Expected body of the response.
        """

        resp = str(response.read())
        #print "\n\n\n respuesta: "+ resp+ "\n\n\n"
        #print "\n  esperado: "+ expected_body + "\n\n\n"
        #print "\n\n------------------------------------------------------------------------------------------------------------------------------------------------- "+str(resp.find(expected_body))+"\n\n"

        assert resp.find(expected_body) >= 0,  \
            "Wrong body received: %s \n\n Expected: %s" \
            % (resp, expected_body)



    ## -------------------


