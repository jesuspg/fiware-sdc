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
ATTRIBUTE_TYPE = u'type'
ATTRIBUTE_TYPE_PLAIN = u'Plain'
ATTRIBUTE_TYPE_IPALL = u'IPALL'


#HEADERS
CONTENT_TYPE = u'content-type'
CONTENT_TYPE_JSON = u'application/json'
CONTENT_TYPE_XML = u'application/xml'
AUTH_TOKEN_HEADER = u'X-Auth-Token'
TENANT_ID_HEADER = u'Tenant-Id'
ACCEPT_HEADER = u'Accept'
ACCEPT_HEADER_XML = u'application/xml'
ACCEPT_HEADER_JSON = u'application/json'

#PRODUCT RELEASE
PRODUCT_RELEASE = u'productRelease'
PRODUCT_RELEASE_LIST = u'productReleases'
VERSION = u'version'

#INCORRECT PARAMETERS
LONG_ID = 'long' * 64 + 'a' #STRING WITH 257 characters

#DEFAULT_METADATA
NUMBER_OF_DEFAULT_SDC_METADATA = 6
DEFAULT_METADATA = {"metadata": [{"key": "image", "value": ""},
                                 {"key": "cookbook_url", "value": ''}, {"key": "cloud", "value": "yes"},
                                 {"key": "installator", "value": "chef"}, {"key": "open_ports", "value": "80 22"}]}

DEFAULT_ATTRIBUTE = {"attribute": [{"key": "custom_att_01", "value": "att_01_default", "type": "Plain"},
                                   {"key": "custom_att_02", "value": "att_02_default", "type": "Plain"}]}

PRODUCT_RELEASE_WITHOUT_RELEASES_RESPONSE = u'<?xml version="1.0" encoding="UTF-8" standalone="yes"?>' \
                                            u'<productReleases></productReleases>'

# FABRIC AND PRODUCT INSTALLATION
FABRIC_RESULT_EXECUTE = u'<local-only>'

PRODUCT_FILE_NAME_FORMAT = u'{product_name}_{product_version}_{installator}'
PRODUCT_INSTALLATION_FILE_CONTENT = u'Operation: install; Product: {product_name}; Version: {product_version}; Att01: {att_01}; Att02: {att_02}'
PRODUCT_INSTALLATION_ATT1_DEFAULT = u'att_01_default'
PRODUCT_INSTALLATION_ATT2_DEFAULT = u'att_02_default'

#PRODUCT_INSTALLATION_PARAMETERS

PRODUCT_INSTANCE_LIST = u'productInstances'
PRODUCT_INSTANCE = u'productInstanceDto'
PRODUCT_INSTANCE_RES = u'productInstance'
PRODUCT_INSTANCE_NAME = u'name'
PRODUCT_INSTANCE_STATUS = u'status'
PRODUCT_INSTANCE_VM = u'vm'
PRODUCT_INSTANCE_VM_IP = u'ip'
PRODUCT_INSTANCE_VM_FQN = u'fqn'
PRODUCT_INSTANCE_VM_OSTYPE = u'osType'
PRODUCT_INSTANCE_VM_HOSTNAME =  u'hostname'
PRODUCT_INSTANCE_ATTRIBUTES = u'attributes'


# METADATAS VALUES

INSTALLATOR = u'installator'
INSTALLATOR_VALUE = (u'puppet', u'chef')
METADATA_TENANT_ID = u'tenant_id'

#TASKS
TASK = u'task'
TASK_HREF = u'href'
TASK_STARTTIME = u'startTime'
TASK_STATUS = u'status'
TASK_DESCRIPTION = u'description'
TASK_VDC = u'vdc'
TASK_ERROR = u'error'
TASK_ERROR_MINOR_CODE = u'minorErrorCode'
TASK_URL = u'@href'
STATUS = u'status'
STATUS_XML = u'@status'
VDC = u'vdc'
TASK_STATUS_VALUE_RUNNING = u'RUNNING'
TASK_STATUS_VALUE_SUCCESS = u'SUCCESS'
TASK_STATUS_VALUE_ERROR = u'ERROR'
TASK_STATUS_VALUE_INSTALLED = u'INSTALLED'
TASK_STATUS_VALUE_UNINSTALLED = u'UNINSTALLED'

#PRODUCTANDRELEASE VALUES
PRODUCTANDRELEASE_LIST = u'productAndReleaseDtoes'
PRODUCTANDRELEASE = u'productAndReleaseDto'

#ATTRIBUTE FROM CONFIG FILE (for loading values from config_file)
CONFIG_FILE = u'${CONFIG_FILE}'

