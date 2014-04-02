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
Created on 01/08/2013

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
import xml

domine = "130.206.80.119"
port = "8080"

keystone_ip = "130.206.80.63"
keystone_port = "35357"
vdc = '60b4125450fc4a109f50357894ba2e28'
user = 'henar'
password = 'vallelado'
project = 'henarproject'

print("Getting token from keystone")
token = utils.obtainToken(keystone_ip, keystone_port, user, password, project)
print("OK: " + token)

headers = {'Content-Type': 'application/xml', 'X-Auth-Token': token, 'Tenant-ID': vdc}

print('Get product list in the software catalogue...')
resource = "/sdc/rest/catalog/product"
data1 = utils.doRequestHttpOperation(domine, port, resource, 'GET', None, headers)

#Calculating number of products in SDC Catalogue
root = xml.etree.ElementTree.fromstring(data1)
number_product = 0
for product in root.findall('product'):
    number_product = number_product + 1

print("Number of products in catalog:")
print(number_product)
print("SUCCESS")
