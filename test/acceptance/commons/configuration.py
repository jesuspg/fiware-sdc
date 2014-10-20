__author__ = 'arobres, jfernandez'

# SDC CONFIGURATION
SDC_PROTOCOL = 'https'
SDC_IP = '130.206.81.126'
SDC_PORT = '8443'
HEADERS = {'Accept': '', 'Tenant-Id': '', 'X-Auth-Token': ''}

# CHEF-SERVER CONFIGURATION
CONFIG_CHEF_SERVER_IP = 'chef-server.dev-havana.fi-ware.org'
CONFIG_CHEF_SERVER_USERNAME = '******** SET ********'
CONFIG_CHEF_SERVER_PASSWORD = '******** SET ********'

# PUPPET-MASTER CONFIGURATION
CONFIG_PUPPET_MASTER_IP = 'puppet-master.dev-havana.fi-ware.org'
CONFIG_PUPPET_MASTER_USERNAME = '******** SET ********'
CONFIG_PUPPET_MASTER_PASSWORD = '******** SET ********'
CONFIG_PUPPETDB_PROTOCOL = 'http'
CONFIG_PUPPETDB_IP = 'puppet-master.dev-havana.fi-ware.org'
CONFIG_PUPPETDB_PORT = '8080'


#AUTHENTICATION
AUTHENTICATION_HEADERS = {'content-type': 'application/json', 'Accept': 'application/json'}
TENANT_NAME_VALUE = '******** SET ********'
USERNAME_VALUE = '******** SET ********'
PWD_VALUE = '******** SET ********'
KEYSTONE_URL = 'http://130.206.80.57:4731/v2.0/tokens'

#E2E TEST: DEFAULT CONFIGURATION
CONFIG_PRODUCT_NAME_CHEF = 'qa-test-product-chef-01'
CONFIG_PRODUCT_NAME_CHEF_2 = 'qa-test-product-chef-02'
CONFIG_PRODUCT_VERSION_CHEF = '1.2.3'

CONFIG_PRODUCT_NAME_PUPPET = 'qa-test-product-puppet-01'
CONFIG_PRODUCT_NAME_PUPPET_2 = 'qa-test-product-puppet-02'
CONFIG_PRODUCT_VERSION_PUPPET = '1.2.3'

CONFIG_VM_HOSTNAME = 'qa-test-sp1'
CONFIG_VM_IP = '130.206.81.148'
CONFIG_VM_FQN = 'qa-test-sp.openstacklocal'
CONFIG_VM_USERNAME = '******** SET ********'
CONFIG_VM_PASSWORD = '******** SET ********'

#PROVISION HOST
PROVISION_ROOT_PATH = u'/tmp/{}'

# Defines the wait time for operation (install / uninstall)
WAIT_FOR_OPERATION = 150
WAIT_FOR_INSTALLATION = 30