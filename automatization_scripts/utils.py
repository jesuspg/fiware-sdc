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


import httplib
from xml.dom.minidom import parse, parseString
from xml.dom.minidom import getDOMImplementation
from xml.etree.ElementTree import Element, SubElement, tostring
import sys
import hashlib, base64


def doRequestHttpOperation(domine, port, resource, operation, data, headers):
    try:
        conn = httplib.HTTPConnection(domine, port)
        if operation == 'GET' or operation == 'DELETE':
            conn.request(operation, resource, None, headers)
        else:
            conn.request(operation, resource, data, headers)
        r1 = conn.getresponse()

        if r1.status != 200:
            print ('   ERROR in the Operation' )
            print(r1.status, r1.reason)
            exit(1)
            #else:
            # print ('   Operation sucessfull')

        data1 = r1.read()
        return data1
    except httplib.HTTPException, e:
        print("An error has ocurred when connecting")
        print (e)
        sys.exit(1)


def processTask(domine, port, taskdom):
    try:
        dom = parseString(taskdom)
        task = (dom.getElementsByTagName('task'))[0]
        href = task.attributes["href"].value
        #print(href)
        status = task.attributes["status"].value

        while status == 'RUNNING':
            data1 = doRequestHttpOperation(domine, port, href, 'GET', None)
            dom = parseString(data1)
            task = (dom.getElementsByTagName('task'))[0]
            status = task.attributes["status"].value
        if status == 'ERROR':
            error = (dom.getElementsByTagName('error'))[0]
            message = error.attributes["message"].value
            majorErrorCode = error.attributes["majorErrorCode"].value
            print "ERROR : " + message + " " + majorErrorCode
        return status
    except:
        print ("Error in parsing the taskId")
        sys.exit(1)


def processProductInstanceStatus(productInstance):
    try:
        dom = parseString(productInstance)
        status = (dom.getElementsByTagName('status'))[0]
        return status.firstChild.nodeValue
    except:
        print ("Error in parsing the taskId")
        sys.exit(1)


def createProductInstanceDto(ip_string, fqn_string, product_name, product_version):
    impl = getDOMImplementation()
    productInstanceDto = Element('productInstanceDto')
    vm = SubElement(productInstanceDto, "vm")
    ip = SubElement(vm, "ip")
    ip.text = ip_string
    fqn = SubElement(vm, "fqn")
    fqn.text = fqn_string
    hostname = SubElement(vm, "hostname")
    hostname.text = "testchef2"
    domain = SubElement(vm, "domain")
    domain.text = ".test"

    product = SubElement(productInstanceDto, "product")
    name = SubElement(product, "name")
    name.text = product_name

    version = SubElement(product, "version")
    version.text = product_version

    return productInstanceDto


def createArtifactDto( artifact_name, product_name, product_version):
    impl = getDOMImplementation()
    artifact = Element('artifactDto')
    name = SubElement(artifact, "name")
    name.text = artifact_name
    productRelease = SubElement(artifact, "productRelease")
    product = SubElement(productRelease, "product")
    name = SubElement(product, "name")
    name.text = product_name
    version = SubElement(product, "version")
    version.text = product_version


def createAttributesDto(value_name, key_name):
    impl = getDOMImplementation()
    attributes = Element('attributes')
    attribute = SubElement(attributes, "attribute")
    value = SubElement(attribute, "value")
    value.text = value_name
    key = SubElement(attribute, "key")
    key.text = key_name

    return attributes


def obtainToken(keystone_ip, keystone_port, user, password, project):
    payload = '{"auth":{"tenantName":"' + project + '","passwordCredentials":{"username":"' + user + '","password":"' + password + '"}}}'
    headers = {'Content-Type': 'application/json', 'Accept': 'application/xml'}

    data1 = doRequestHttpOperation(keystone_ip, keystone_port, "/v2.0/tokens", 'POST', payload, headers)
    #print(data1)
    dom = parseString(data1)
    try:
        result = (dom.getElementsByTagName('token'))[0]
        token = result.attributes["id"].value

        #print(token)
        return token

    except:
        print ("Error in the processing enviroment")
        sys.exit(1)

#Hashing using SHA1 and encoded in Base64
def getHash(message):
    try:
        #hash_object = hashlib.sha1(body.encode('ascii')
        #hex_dig = hash_object.hexdigest()
        #bodyHashed = base64.b64encode(hex_dig)
        #print(bodyHashed)

        bodyHashed = base64.b64encode(hashlib.sha1(message).digest())
        return bodyHashed
    except:
        print("Error hashing sha1 and encoding b64")
        sys.exit(1)
  

