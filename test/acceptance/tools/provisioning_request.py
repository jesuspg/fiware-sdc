__author__ = 'henar'

import http
import json
from xml.etree.ElementTree import tostring
from lettuce import world
from tools import body_message


class CatalogueRequest:
    """
    Manage products in a catalogue.
    """
    provisioningURL = ""

    #Body types
    ADD_PRODUCT_BODY =  '<?xml version="1.0" encoding="UTF-8"?>' \
                    '<product>  </product>'

    ALL_METADATAS = ' <metadatas>' \
                    '   <key>image</key>' \
                    '   <value>e6c5b19e-f655-4da8-86ea-6dd05be673ef</value>' \
                    ' </metadatas>' \
                    '  <metadatas>' \
                    '   <key>cookbook_url</key>' \
                    '   <value>https://forge.fi-ware.eu/scmrepos/svn/testbed/trunk/cookbooks/GESoftware/beatest</value>' \
                    ' </metadatas>' \
                    ' <metadatas>' \
                    '   <key>cloud</key>' \
                    '   <value>yes</value>' \
                    ' </metadatas>' \
                    ' <metadatas>' \
                    '   <key>installator</key>' \
                    '   <value>chef</value>' \
                    ' </metadatas>' \
                    ' <metadatas>' \
                    '   <key>open_ports</key>' \
                    '   <value>22</value>' \
                    ' </metadatas>' \
                    ' <metadatas>' \
                    '   <key>repository</key>' \
                    '   <value>svn</value>' \
                    ' </metadatas>' \
                    ' <metadatas>' \
                    '   <key>public</key>' \
                    '   <value>no</value>' \
                    ' </metadatas>' \
                    ' <metadatas>' \
                    '   <key>dependencies</key>' \
                    '   <value>tomcat nodejs mysql</value>' \
                    ' </metadatas>'

    #ADD_PRODUCT = "<?xml version='1.0' encoding='UTF-8'?> <product></product>"

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

    def __init_ADD_PRODUCT_Body(self):
        self.ADD_PRODUCT_BODY =  '<?xml version="1.0" encoding="UTF-8"?>' \
                    '<product>  </product>'

    def __get__token(self):
        return http.get_token(self.keystone_url + '/tokens', self.tenant, self.user, self.password)

    def __get__url (self, operation, product):
        if operation == "getProductList" or operation == "addProduct":
            return "%s/%s" % (self.sdc_url, self.catalogURL)
        elif operation == "getDetails" or operation == "deleteProduct":
            return "%s/%s/%s" % (self.sdc_url, self.catalogURL, product)
        elif operation == "getAttributes":
            return "%s/%s/%s/%s" % (self.sdc_url, self.catalogURL, product, "attributes")
        elif operation == "getMetadatas":
            return "%s/%s/%s/%s" % (self.sdc_url, self.catalogURL, product, "metadatas")

    def __get__headers(self, operation):
        if operation == "getProductList":
            return {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc, 'Accept': "application/json"}
        elif operation == "addProduct":
            return {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc, 'Accept': "application/xml", "Content-Type":"application/xml"}
        elif operation == "getDetails" or operation == "getAttributes" or operation == "getMetadatas":
            return {'X-Auth-Token': self.token, 'Accept': "application/xml"}
        elif operation == "deleteProduct":
            return {'X-Auth-Token': self.token, 'Accept': "application/xml", "Content-Type":"application/json"}

    def __errorLabel (self, value, error):
        if error == "wrong":
            return '1234567890'
        elif error == "empty":
            return ''
        else:
            return value

    def __errorUrl (self, value, error):
        if error == "Not Found":
           pos = value.find("catalog")
           value = value[:pos] + "error_" + value[pos:]    # ex: http://130.206.80.119:8082/sdc/rest/error_catalog/product/Product_test_0001
        return value

    def __request(self, method, url,  headers, body,  error):
        headers['X-Auth-Token'] = self.__errorLabel (headers['X-Auth-Token'], error)
        url = self.__errorUrl(url, error)

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

        #self.printRequest(method,url,headers,body)
        return response

    def __insert_label (self, string, stringBeforeToInsert, newSubString):
        pos = string.find(stringBeforeToInsert)
        return string[:pos] + newSubString + string[pos:]

    def __set_body_name (self, product):
        label = "</product>"
        product = "<name>"+str(product)+"</name> " \
                  " <description>Product only for test</description>"
        self.ADD_PRODUCT_BODY = self.__insert_label(self.ADD_PRODUCT_BODY, label, product)

    def __set_body_attributes (self):
        label = "</product>"
        attributes = "<attributes>" \
                     "     <key>username</key>" \
                     "     <value>postgres</value>" \
                     "     <description>The administrator usename</description>" \
                     "</attributes>" \
                     "<attributes>" \
                     "     <key>password</key>" \
                     "     <value>postgres</value>" \
                     "     <description>The administrator password</description>" \
                     "</attributes>"

        self.ADD_PRODUCT_BODY = self.__insert_label(self.ADD_PRODUCT_BODY, label, attributes)

    def __set_body_metadata (self, meta, value):
        label = "</product>"
        metadata = " <metadatas> " \
                     "    <key>"+meta+"</key> " \
                     "    <value>"+str(value)+"</value> " \
                     " </metadatas>"
        if meta == "all": metadata = self.ALL_METADATAS
        self.ADD_PRODUCT_BODY = self.__insert_label(self.ADD_PRODUCT_BODY, label, metadata)

    def __create_body (self, label,  product, metadataValue):
        self.__init_ADD_PRODUCT_Body()
        self.__set_body_name(product)
        if label == "attributes" or label == "attributes_and_all_metadatas":
            self.__set_body_attributes()
            if label == "attributes_and_all_metadatas":
                self.__set_body_metadata("all", None)
        elif label.find("metadata_") == 0:
            key = label [len ("metadata_"):]
            self.__set_body_metadata(key, metadataValue)

    def catalogue_getProductInfo(self, searchType, product, errorType):
        """
        List all products in catalogue
        Returns all details of a Product
        Returns all attributes of a Product
        Delete an existent product

        :param method: define which protocol method are using
        :param product: define the product used
        :param errorType: definition of several error caused. Ex: Not Found, bad Method, unauthorized, etc.
        """
        world.response = self.__request("GET", self.__get__url(searchType,product),self.__get__headers(searchType), None, errorType)
        #self.printResponse()

    def catalogue_addProduct(self, product, label, metadataValue,  errorType):
        """
        Add a new product in catalogue

        :param method: define which protocol method are using
        :param product: product name that it will created
        :param metadataValue: In case that you are adding metadatas, it is for the value metadata
        :param errorType: definition of several error caused. Ex: Not Found, bad Method, unauthorized, etc.
        """

        if label != "Without Name Label":
            self. __create_body(label, product, metadataValue)

        world.response = self.__request("POST", self.__get__url("addProduct", None),self.__get__headers("addProduct"), self.ADD_PRODUCT_BODY, errorType)
        #self.printResponse()

    def catalogue_deleteProduct(self, product, errorType):
        """
        Delete a product in catalogue

        :param method: define which protocol method are using
        :param errorType: definition of several error caused. Ex: Not Found, bad Method, unauthorized, etc.
        """
        world.response = self.__request("DELETE", self.__get__url("deleteProduct", product),self.__get__headers("deleteProduct"), None, errorType)
        #self.printResponse()

    def printRequest(self, method, url, headers, body):
        print "----------------------------------------------------------------------------------------"
        print "url: "+ str(method) + "  "+str(url)
        print "\nHeader: "+ str (headers)+"\n"
        if body is not None:
            print "\nBody: init("+str (body)+")end\n\n"
        print "----------------------------------------------------------------------------------------\n\n\n\n"

    def printResponse(self):
        print "----------------------------------------------------------------------------------------"
        print "status code: "+str(world.response.status)
        print "\nHeader: "+ str(world.response.msg)
        print "\nBody: init("+str(world.response.read())+")end\n\n\n"
        print "----------------------------------------------------------------------------------------"

    def get_body_expected(self, response_type, operation):
        for ID in body_message.Catalog_body:
            if ID["operation"] == operation and ID["code"] == response_type:
                return ID["body"]

    def check_response_status(self, response, expected_status_code):
        """
        Checks that the response status is the expected one.
        :param response: Response to be checked.
        :param expected_status_code: Expected status code of the response.
        """

        assert response.status == expected_status_code, \
        "Wrong status code received: %d. Expected: %d. Body content: %s" \
        % (response.status, expected_status_code, response.read())

    def check_response_body(self, response, expected_body):
        """
        Checks that the response body is the expected one.
        :param response: Response to be checked.
        :param expected_body: Expected body of the response.
        """
        #print "\n\n\n respuesta: "+ response.read()+ "\n\n\n"
        #print "\n  esperado: "+ expected_body + "\n\n\n"
        #print "\n\n------------------------------------------------------------------------------------------------------------------------------------------------- "+str(response.read().find(expected_body))+"\n\n"

        assert str(response.read().find(expected_body)) >= 0,  \
            "Wrong body received: %s \n\n Expected: %s" \
            % (response.read(), expected_body)



    ## -------------------


