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


domine = "130.206.80.112"
port = "8080"

vm_fqn = 'testArtifactManagement3'
vm_ip = '130.206.80.114'
product_name = 'test'
product_version = '0.1'
#vdc = 'test3'
artifact_name = 'artifact'

keystone_ip = "130.206.80.63"
keystone_port = "35357"
user = 'henar'
password = 'vallelado'
project = 'henarproject'
vdc = '60b4125450fc4a109f50357894ba2e28'

token = utils.obtainToken(keystone_ip, keystone_port, user, password, project)
print(token)

headers = {'Content-Type': 'application/xml', 'X-Auth-Token': token, 'Tenant-ID': vdc}
print(headers)

#Deploy a product
resource_product_instance = "/sdc/rest/vdc/" + vdc + "/productInstance"

#productInstanceDto =  utils.createProductInstanceDto (vm_ip,vm_fqn, product_name, product_version)
#task = utils.doRequestHttpOperation(domine, port, resource_product_instance, 'POST',tostring(productInstanceDto), headers)
#status = utils.processTask (domine,task)
#print ("  " + status)

#if status != 'SUCCESS':
#  print("Error with the requirements to test")
#  sys.exit(1)

resource_install_artifact = "/sdc/rest/vdc/" + vdc + "/productInstance/" + vm_fqn + '_' + product_name + '_' + product_version + "/ac"
print('Deploy Ac in product ' + product_name )
artifactDto = utils.createArtifactDto(artifact_name, product_name, product_version)
print(artifactDto)
data = utils.doRequestHttpOperation(domine, port, resource_install_artifact, 'POST', artifactDto, headers)
status = utils.processTask(domine, data)
print ("  " + status)
#if  status != 'INSTALLED':
# print("Status not correct" + status)
resource_delete_artifact = "/sdc/rest/vdc/" + vdc + "/productInstance/" + vm_fqn + '_' + product_name + '_' + product_version + "/ac/" + artifact_name
print('Get Info Ac in product ' + product_name )
data = utils.doRequestHttpOperation(domine, port, resource_delete_artifact, 'GET', None, headers)
print ("  OK" )

print('Get Delete Product Instance ' + product_name )
task = utils.doRequestHttpOperation(domine, port, resource_delete_artifact, 'DELETE', None, headers)
status = utils.processTask(domine, task)
print ("  " + status)
#data = utils.doRequestHttpOperation(domine, port, resource_delete_artifact, 'GET',None, headers)
#statusProduct = utils.processProductInstanceStatus(data)
#if  status != 'UNINSTALLED':
# print("Status not correct" + statusProduct)






    

    
    

    

    
    
 


