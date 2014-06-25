__author__ = 'arobres'

import requests
import ujson
from configuration import TENANT_NAME_VALUE, USERNAME_VALUE, PWD_VALUE, AUTHENTICATION_HEADERS, KEYSTONE_URL
from constants import TENANT_NAME, PASSWORD, USERNAME, AUTH, ACCESS, TENANT, TOKEN, ID


def get_token():
    body = {AUTH: {TENANT_NAME: TENANT_NAME_VALUE, "passwordCredentials": {USERNAME: USERNAME_VALUE,
                                                                           PASSWORD: PWD_VALUE}}}
    body = ujson.dumps(body)
    r = requests.request(method='post', url=KEYSTONE_URL, data=body, headers=AUTHENTICATION_HEADERS)
    response = r.json()
    token_id = response[ACCESS][TOKEN][ID]
    tenant_id = response[ACCESS][TOKEN][TENANT][ID]
    return token_id, tenant_id
