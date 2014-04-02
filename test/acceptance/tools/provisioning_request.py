# -*- coding: utf-8 -*-
__author__ = 'ivanl@tid.es'

import http
from lettuce import world
from tools import body_message
import utils


class ProvisioningRequest:
    """
    Manage provisioning of product in some VM.
    """
    vdc = "testQA"
    provisioningURL = "vdc/"+vdc
    INSTALL_BODY = ""

    def __init__(self, keystone_url, paas_manager_url, tenant, user, password, vdc, sdc_url, VM_IP, VM_hostname):
        """
        constructor
        :type sdc_url: object
        :type paas_manager_url:
        :param keystone_url:
        :param paas_manager_url:
        :param tenant:
        :param user:
        :param password:
        :param vdc:
        :param sdc_url:
        """
        self.paasmanager_url = paas_manager_url
        self.sdc_url = sdc_url
        self.vdc = vdc
        self.keystone_url = keystone_url
        self.user = user
        self.password = password
        self.tenant = tenant
        world.VM_IP = VM_IP
        world.VM_hostname = VM_hostname
        self.token=http.get_token(self.keystone_url + '/tokens', self.tenant, self.user, self.password)

    def __init_body (self, content):
        """
        initializes install body
        """
        if content == 'xml':
            self.INSTALL_BODY = '<?xml version="1.0" encoding="UTF-8"?><productInstanceDto></productInstanceDto>'
        elif content == 'json':
            self.INSTALL_BODY = '{}'

    def __create__url (self, operation, productName=""): # ex: http://130.206.80.119:8082/sdc/rest/vdc/testQA/productInstance
        """
        create the url for different requests
        :param task:
        :param productInstance:
        :param operation: Identifier to URL
        :param release:
        :return: request url
        """
        if operation == "installProduct":
            return "%s/%s/%s" % (self.sdc_url, self.provisioningURL, 'productInstance')
        if operation == "uninstallProduct":
            return "%s/%s/%s/%s" % (self.sdc_url, self.provisioningURL, 'productInstance', productName)

    def __create__headers(self, Accept="xml"):
        """
        create the header for different requests
        :param operation:
        :param Accept:
        :return:
        """
        return {'X-Auth-Token': self.token, 'Tenant-Id': self.vdc, 'Accept': "application/"+Accept, "Content-Type":"application/"+Accept}


    def provisioning_installProduct (self, ip, hostname, osType, product, release, errorType, content):
        """
         install a product in a VM
        :param product:
        :param release:
        :param ip:
        :param errorType:
        :param content:
        """
        vmElement = [{'label': 'ip',       'value': ip},
                     {'label': 'hostname', 'value': hostname},
                     {'label': 'domain',   'value': None},
                     {'label': 'fqn',      'value': 'fqn'},
                     {'label': 'osType',   'value': osType}]

        productElement = [{'label': 'name',          'value': product},
                          {'label': 'version',       'value': release},
                          {'label': 'type',          'value': None}]

        self.__init_body(content)
        self.INSTALL_BODY = utils.body_elements(self.INSTALL_BODY, vmElement,      "vm",      "installProduct", content)
        self.INSTALL_BODY = utils.body_elements(self.INSTALL_BODY, productElement, "product", "installProduct", content)

        world.response = utils.request("POST", self.__create__url("installProduct"), self.__create__headers(content), self.INSTALL_BODY, errorType)

    def provisioning_uninstallProduct(self, product, release, fqn, errorType, content):
        """
        uninstall a product-release in a VM
        :param release:
        :param fqn:
        :param NOT_ERROR:
        :param content:
        """
        name = fqn+"_"+product+"_"+release
        world.response = utils.request("DELETE", self.__create__url("uninstallProduct", name), self.__create__headers(content), None, errorType)
        pass
