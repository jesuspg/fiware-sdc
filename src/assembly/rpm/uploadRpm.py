__author__ = 'jmmovilla'

import requests
import json
#import ujson

#AUTHENTICATION CONSTANTS
AUTH = u'auth'
TENANT_NAME = u'tenantName'
USERNAME = u'username'
PASSWORD = u'password'
ACCESS = u'access'
TOKEN = u'token'
TENANT = u'tenant'
ID = u'id'

#AUTHENTICATION
AUTHENTICATION_HEADERS = {'content-type': 'application/json', 'Accept': 'application/json'}
TENANT_NAME_VALUE = '00000000000000000000000000000192'
USERNAME_VALUE = 'arobres@tid.es'
PWD_VALUE = 'testing'
KEYSTONE_URL = 'http://130.206.80.57:4731/v2.0/tokens'

#HEADERS
CONTENT_TYPE = u'content-type'
CONTENT_TYPE_JSON = u'application/json'
CONTENT_TYPE_XML = u'application/xml'
AUTH_TOKEN_HEADER = u'X-Auth-Token'
TENANT_ID_HEADER = u'Tenant-Id'
ACCEPT_HEADER = u'Accept'

#REPOSITORY LOCATION
REPOSITORY_URL = 'http://repositories.testbed.fi-ware.eu:8889/upload'
#RPM_LOCATION = './target/rpm/fiware-sdc/RPMS/noarch/fiware-sdc-2.2.0-SNAPSHOT20140923114214.noarch.rpm'
RPM_LOCATION = 'prueba.txt'

def get_token():
    body = {AUTH: {TENANT_NAME: TENANT_NAME_VALUE, "passwordCredentials": {USERNAME: USERNAME_VALUE,
                                                                           PASSWORD: PWD_VALUE}}}
    #body = ujson.dumps(body)
    body = json.dumps(body)
    print 'Body to request token: ' + body
    r = requests.request(method='post', url=KEYSTONE_URL, data=body, headers=AUTHENTICATION_HEADERS)
    response = r.json()
    token_id = response[ACCESS][TOKEN][ID]
    print 'Tokenid: ' + token_id
    return token_id
	

def upload_rpm():
    #url = REPOSITORY_URL
    print 'Uploading rpm to ' + REPOSITORY_URL
    files = {'file': (RPM_LOCATION, open(RPM_LOCATION, 'rb'), {AUTH_TOKEN_HEADER: get_token()})}
    #files = {'file': ('report.xls', open('report.xls', 'rb'), 'application/vnd.ms-excel', {'Expires': '0'})}
    r = requests.post(REPOSITORY_URL, files=files)
    print 'Status Code of uploading rpm ' + r.status_code

upload_rpm()