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
port="8080"

vm_fqn = 'fqn6'
vm_ip = '130.206.80.112'
product_name = 'tomcat'
product_version = '6'
vdc = '60b4125450fc4a109f50357894ba2e28'

keystone_ip = "130.206.80.63"
keystone_port = "35357"
#vdc = 'ebe6d9ec7b024361b7a3882c65a57dda'
user = 'henar'
password='vallelado'
project ='henarproject'

token = utils.obtainToken (keystone_ip, keystone_port, user, password, project)
print(token)

headers = {'Content-Type': 'application/xml', 'X-Auth-Token':   token  ,  'Tenant-ID': vdc}
print(headers)
    
print('Get products in the software catalogue: ')
resource = "/sdc/rest/catalog/product"
data1 = utils.doRequestHttpOperation(domine, port,resource, 'GET',None, headers)
print("  OK")
dom = parseString(data1)
try:
	product = (dom.getElementsByTagName('product'))[0]
	productname = product.firstChild.firstChild.nodeValue
	print('First product in the software catalogue')
	print("  OK " + productname)

except:
	print ("Error in the request to get products")
	sys.exit(1)

print('Get Product Details ' + product_name )
data1 = utils.doRequestHttpOperation(domine,port,"/sdc/rest/catalog/product/"+product_name, 'GET',None, headers)
print("  OK")

print('Get Product Releases ' + product_name )
data1 = utils.doRequestHttpOperation(domine,port,"/sdc/rest/catalog/product/"+product_name+"/release", 'GET',None, headers)
print("  OK")

print('Get Product Release Info ' + product_name + " " + product_version )
data1 = utils.doRequestHttpOperation(domine,port,"/sdc/rest/catalog/product/"+product_name+"/release/"+ product_version , 'GET',None,headers)
print("  OK")

print('Get Product Attributes ' + product_name )   
data1 = utils.doRequestHttpOperation(domine,port,"/sdc/rest/catalog/product/"+product_name+'/attributes', 'GET',None, headers)
print("  OK")

