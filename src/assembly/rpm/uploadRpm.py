__author__ = 'jmmovilla'

import requests
import json
import os

#AUTHENTICATION CONSTANTS
AUTH = u'auth'
TENANT_NAME = u'tenantName'
USERNAME = u'username'
PASSWORD = u'password'
PASSWORD_CREDENTIALS = u'passwordCredentials'
ACCESS = u'access'
TOKEN = u'token'
TENANT = u'tenant'
ID = u'id'

#AUTHENTICATION
AUTHENTICATION_HEADERS = {'content-type': 'application/json', 'Accept': 'application/json'}
TENANT_NAME_VALUE = os.environ['OS_TENANT_NAME_VALUE']
USERNAME_VALUE = os.environ['OS_USERNAME_VALUE']
PASSWORD_VALUE = os.environ['OS_PASSWORD_VALUE']

KEYSTONE_URL = 'http://130.206.80.61:35357/v2.0/tokens'

#HEADERS
AUTH_TOKEN_HEADER = u'x-Auth-Token'

#REPOSITORY LOCATION
REPOSITORY_URL = 'http://repositories.testbed.fi-ware.eu:8889/upload'
RPM_LOCATION = './target/rpm/fiware-sdc/RPMS/noarch/fiware-sdc-2.2.0-SNAPSHOT*.noarch.rpm'

def get_token():
    print TENANT_NAME_VALUE
    print USERNAME_VALUE
    print PASSWORD_VALUE
	
    body = {AUTH: {TENANT_NAME: TENANT_NAME_VALUE, PASSWORD_CREDENTIALS: {USERNAME: USERNAME_VALUE,
                                                                           PASSWORD: PASSWORD_VALUE}}}
    body = json.dumps(body)
    print 'Body to request token: ' + body
    r = requests.request(method='post', url=KEYSTONE_URL, data=body, headers=AUTHENTICATION_HEADERS)
    response = r.json()
    token_id = response[ACCESS][TOKEN][ID]
    print 'Tokenid: ' + token_id
    return token_id
	
token_id = get_token()

def upload_rpm():
    #url = REPOSITORY_URL
    print 'Uploading rpm to ' + REPOSITORY_URL
    files = {'file': (RPM_LOCATION, open(RPM_LOCATION, 'rb'), {'Expires': '0'})}
    AUTH_TOKEN_HEADERS = {AUTH_TOKEN_HEADER : token_id}
    print AUTH_TOKEN_HEADERS
    r = requests.request(method='post', url=REPOSITORY_URL, files=files, headers=AUTH_TOKEN_HEADERS)
    print r.status_code

upload_rpm()