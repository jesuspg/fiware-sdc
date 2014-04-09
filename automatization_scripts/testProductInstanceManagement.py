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
import httplib, urllib
import utils


domine = "130.206.80.112"
port = "8080"

vm_fqn = 'fqn44'
vm_ip = '130.206.80.114'
product_name = 'test'
product_version = '0.1'
#vdc = 'test3'

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

resource_product_instance = "/sdc/rest/vdc/" + vdc + "/productInstance"
print('Install a product in VM. Product ' + product_name )

productInstanceDto = utils.createProductInstanceDto(vm_ip, vm_fqn, product_name, product_version)
task = utils.doRequestHttpOperation(domine, port, resource_product_instance, 'POST', tostring(productInstanceDto),
    headers)
status = utils.processTask(domine, port, task)
print ("  " + status)
if status == "ERROR":
    sys.exit(1)

resource_get_info_product_instance = "/sdc/rest/vdc/" + vdc + "/productInstance/" + vm_fqn + '_' + product_name + '_' + product_version
print('Get Product Instance Info. Product ' + product_name )
data = utils.doRequestHttpOperation(domine, port, resource_get_info_product_instance, 'GET', None, headers)
print("  OK")
status = utils.processProductInstanceStatus(data)
if status != 'INSTALLED':
    print("Status not correct" + status)

resource_delete_product_instance = "/sdc/rest/vdc/" + vdc + "/productInstance/" + vm_fqn + '_' + product_name + '_' + product_version
print('Delete Product Instance ' + product_name )
task = utils.doRequestHttpOperation(domine, port, resource_delete_product_instance, 'DELETE', None, headers)
status = utils.processTask(domine, port, task)
print ("  " + status)
data = utils.doRequestHttpOperation(domine, port, resource_delete_product_instance, 'GET', None, headers)
statusProduct = utils.processProductInstanceStatus(data)
if statusProduct != 'UNINSTALLED':
    print("Status not correct" + statusProduct)

print ('Install again')

productInstanceDto = utils.createProductInstanceDto(vm_ip, vm_fqn, product_name, product_version)
task = utils.doRequestHttpOperation(domine, port, resource_product_instance, 'POST', tostring(productInstanceDto),
    headers)
status = utils.processTask(domine, port, task)
print ("  " + status)
if status == "ERROR":
    sys.exit(1)

print ('Install product already installed')

productInstanceDto = utils.createProductInstanceDto(vm_ip, vm_fqn, product_name, product_version)
task = utils.doRequestHttpOperation(domine, port, resource_product_instance, 'POST', tostring(productInstanceDto),
    headers)
status = utils.processTask(domine, port, task)
print ("  " + status)
if status != "ERROR":
    sys.exit(1)

resource_get_info_product_instance = "/sdc/rest/vdc/" + vdc + "/productInstance/" + vm_fqn + '_' + product_name + '_' + product_version
print('Get Product Instance Info. Product ' + product_name )
data = utils.doRequestHttpOperation(domine, port, resource_get_info_product_instance, 'GET', None, headers)
print("  OK")
status = utils.processProductInstanceStatus(data)
if status != 'INSTALLED':
    print("Status not correct" + status)


#resource_configure_product_instance = "/sdc2/rest/vdc/"+vdc+"/productInstance/" + vm_fqn+'_'+product_name+'_'+product_version
#print('Configuring ... Product Instance Info. Product ' + product_name )  
#attributesDto= utils.createAttributesDto ('value','id_web_server')
#data = utils.doRequestHttpOperation(domine,resource_configure_product_instance, 'PUT',tostring(attributesDto), headers)

#status = utils.processProductInstanceStatus(data)
#if  status != 'INSTALLED':
#  print("Status not correct" + status)
#print("  OK")

resource_delete_product_instance = "/sdc/rest/vdc/" + vdc + "/productInstance/" + vm_fqn + '_' + product_name + '_' + product_version
print('Delete Product Instance ' + product_name )
task = utils.doRequestHttpOperation(domine, port, resource_delete_product_instance, 'DELETE', None, headers)
status = utils.processTask(domine, port, task)
print ("  " + status)
data = utils.doRequestHttpOperation(domine, port, resource_delete_product_instance, 'GET', None, headers)
statusProduct = utils.processProductInstanceStatus(data)
if statusProduct != 'UNINSTALLED':
    print("Status not correct" + statusProduct)


  

  








    

    
    

    

    
    
 


