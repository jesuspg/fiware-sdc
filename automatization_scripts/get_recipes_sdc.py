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
password='vallelado'
project ='henarproject'

token = utils.obtainToken (keystone_ip, keystone_port, user, password, project)

headers = {'Content-Type': 'application/xml', 'X-Auth-Token':   token  ,  'Tenant-ID': vdc}

print('Get Cookbook list...')
resource = "/sdc/rest/catalog/product"
data1 = utils.doRequestHttpOperation(domine, port, resource, 'GET',None, headers)
#print(data1)

#Calculating number of products in SDC Catalogue
root = xml.etree.ElementTree.fromstring(data1)
number_product = 0
for name in root.iter('name'):
  number_product = number_product +1
  print("Cookbok  " + str(number_product) + " in SDC Catalogue is " + name.text)

print("Number of Cookbooks in SDC catalog: " + str(number_product)) 
print("SUCCESS")