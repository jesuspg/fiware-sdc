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
PRODUCTS = u'products'
PRODUCT_ATTRIBUTES = u'attributes'
PRODUCT_METADATAS = u'metadatas'

METADATA = u'metadata'
ATTRIBUTE = u'attribute'
KEY = u'key'
VALUE = u'value'
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

FABRIC_RESULT_EXECUTE = u'<local-only>'

#PRODUCT_INSTALLATION_PARAMETERS

PRODUCT_INSTANCE = u'productInstanceDto'
VM = u'vm'
HOSTNAME = u'hostname'
IP = u'ip'
FQN = u'fqn'
OSTYPE = u'osType'


# METADATAS VALUES

INSTALLATOR = u'instalator'
INSTALLATOR_VALUE = (u'puppet', u'chef')


#TASKS
TASK_URL = u'@href'
RUNNING = u'RUNNING'
SUCCESS = u'SUCCESS'
ERROR = u'ERROR'
INSTALLED = u'INSTALLED'
UNINSTALLED = u'UNINSTALLED'
STATUS = u'status'
STATUS_XML = u'@status'
VDC = u'vdc'
