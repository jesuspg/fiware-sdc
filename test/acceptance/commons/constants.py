__author__ = 'arobres'

#AUTHENTICATION CONSTANTS

AUTH = u'auth'
TENANT_NAME = u'tenantName'
USERNAME = u'username'
PASSWORD = u'password'
ACCESS = u'access'
TOKEN = u'token'
TENANT = u'tenant'
ID = u'id'

#PRODUCT_PROPERTIES
PRODUCT_NAME = u'name'
PRODUCT_DESCRIPTION = u'description'
PRODUCT = u'product'
PRODUCT_ATTRIBUTES = u'attributes'
PRODUCT_METADATAS = u'metadatas'
KEY = u'key'
VALUE = u'Value'
DESCRIPTION = u'description'


#HEADERS
CONTENT_TYPE = u'content-type'
CONTENT_TYPE_JSON = u'application/json'
CONTENT_TYPE_XML = u'application/xml'
AUTH_TOKEN_HEADER = u'X-Auth-Token'
TENANT_ID_HEADER = u'Tenant-Id'
ACCEPT_HEADER = u'Accept'

#PRODUCT RELEASE
PRODUCT_RELEASE = u'productRelease'
VERSION = u'version'

#INCORRECT PARAMETERS
LONG_ID = 'long' * 64 + 'a' #STRING WITH 257 characters

#DEFAULT_METADATA
DEFAULT_METADATA = {"metadata": [{"key": "image", "value": "df44f62d-9d66-4dc5-b084-2d6c7bc4cfe4"},
                                 {"key": "cookbook_url", "value": ""}, {"key": "cloud", "value": "yes"},
                                 {"key": "installator", "value": "chef"}, {"key": "open_ports", "value": "80 22"}]}

PRODUCT_RELEASE_WITHOUT_RELEASES_RESPONSE = u'<?xml version="1.0" encoding="UTF-8" standalone="yes"?>' \
                                            u'<productReleases></productReleases>'
