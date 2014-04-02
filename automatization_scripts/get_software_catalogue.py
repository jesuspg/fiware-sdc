#!/usr/bin/env python
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
#        http://www.apache.org/licenses/LICENSE-2.0
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

'''
Created on 16/04/2013

@author: henar
'''
import httplib
import sys
import os
from xml.dom.minidom import parse, parseString
from xml.dom.minidom import getDOMImplementation
from xml.etree.ElementTree import Element, SubElement, tostring
import md5
import httplib, urllib
import utils


domine = "130.206.80.119"
port = "8080"
resource = "/sdc/rest/catalog/product"
vm_fqn = 'fqn6'
vm_ip = '130.206.80.114'
product_name = 'test'
product_version = '0.1'
#vdc = 'test3'

keystone_ip = "130.206.80.63"
keystone_port = "35357"
vdc = '60b4125450fc4a109f50357894ba2e28'
user = 'henar'
password = 'vallelado'
project = 'henarproject'

token = utils.obtainToken(keystone_ip, keystone_port, user, password, project)
print(token)

headers = {'Content-Type': 'application/xml', 'X-Auth-Token': token, 'Tenant-ID': vdc}
print(headers)

print('Get products in the software catalogue: ')
resource = "/sdc/rest/catalog/product"
data1 = utils.doRequestHttpOperation(domine, port, resource, 'GET', None, headers)

dom = parseString(data1)
try:
    product = (dom.getElementsByTagName('product'))[0]
    productname = product.firstChild.firstChild.nodeValue
    print('First product in the software catalogue: ' + productname)

except:
    print ("Error in the request to get products")
    sys.exit(1)

print('Get Product Details ' + product_name )
data1 = utils.doRequestHttpOperation(domine, port, "/sdc/rest/catalog/product/" + product_name, 'GET', None, headers)
print("  OK")

print('Get Product Releases ' + product_name )
data1 = utils.doRequestHttpOperation(domine, port, "/sdc/rest/catalog/product/" + product_name + "/release", 'GET',
    None, headers)
print("  OK")

print('Get Product Release Info ' + product_name + " " + product_version )
data1 = utils.doRequestHttpOperation(domine, port,
    "/sdc/rest/catalog/product/" + product_name + "/release/" + product_version, 'GET', None, headers)
print("  OK")

print('Get Product Attributes ' + product_name )
data1 = utils.doRequestHttpOperation(domine, port, "/sdc/rest/catalog/product/" + product_name + '/attributes', 'GET',
    None, headers)
print("  OK")

resource_product_instance = "/sdc/rest/vdc/" + vdc + "/productInstance"
print('Install a product in VM. Product ' + product_name )

productInstanceDto = utils.createProductInstanceDto(vm_ip, vm_fqn, product_name, product_version)
print (tostring(productInstanceDto))
task = utils.doRequestHttpOperation(domine, port, resource_product_instance, 'POST', tostring(productInstanceDto),
    headers)
print (task)
status = utils.processTask(domine, port, task)
print ("  " + status)

resource_get_info_product_instance = "/sdc/rest/vdc/" + vdc + "/productInstance/" + vm_fqn + '_' + product_name + '_' + product_version
print('Get Product Instance Info. Product ' + product_name )
data = utils.doRequestHttpOperation(domine, port, resource_get_info_product_instance, 'GET', None)
print(data)
status = utils.processProductInstanceStatus(data)
#if  status != 'INSTALLED':
# print("Status not correct" + status)

resource_delete_product_instance = "/sdc/rest/vdc/" + vdc + "/productInstance/" + vm_fqn + '_' + product_name + '_' + product_version
print('Get Delete Product Instance ' + product_name )
task = utils.doRequestHttpOperation(domine, port, resource_delete_product_instance, 'DELETE', None)
status = utils.processTask(domine, port, task)
print("  OK")
data = utils.doRequestHttpOperation(domine, port, resource_delete_product_instance, 'GET', None)
statusProduct = utils.processProductInstanceStatus(data)
#if  status != 'UNINSTALLED':
# print("Status not correct" + statusProduct)






    

    
    

    

    
    
 


