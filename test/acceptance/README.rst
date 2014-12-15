==============================
Sagitta API - Acceptance Tests
==============================
This project contains the Sagitta (Software Deployment & Configuration) acceptance tests
(component, integration and E2E testing).
All test cases have been defined using Gherkin that it is a Business Readable, Domain Specific Language that lets you
describe software’s behaviour without detailing how that behaviour is implemented.
Gherkin has the purpose of serving documentation of test cases.


Test case implementation has been performed using `Python <http://www.python.org/>`_ and
`Lettuce <http://lettuce.it/>`_.


Test environment
----------------

**Prerequisites**

- Python 2.7 or newer (2.x) (https://www.python.org/downloads/)
- pip (https://pypi.python.org/pypi/pip)
- virtualenv (https://pypi.python.org/pypi/virtualenv)
- Sagitta, Chef and Puppet (`Download SDC (Software Deployment & Configuration)<http://catalogue.fi-ware.org/enablers/software-deployment-configuration-sagitta/downloads>`_)

**Test case execution using virtualenv**

1. Create a virtual environment somewhere *(virtualenv ~/venv)*
#. Activate the virtual environment *(source ~/venv/bin/activate)*
#. Go to *test/acceptance* folder in the project
#. Install the requirements for the acceptance tests in the virtual environment *(pip install -r requirements.txt --allow-all-external)*

**Test case execution using Vagrant (optional)**

Instead of using virtualenv, you can use the provided Vagrantfile to deploy a local VM using `Vagrant <https://www.vagrantup.com/>`_, that will provide all environment configurations for launching test cases.

1. Download and install Vagrant (https://www.vagrantup.com/downloads.html)
#. Go to *test/acceptance* folder in the project
#. Execute *vagrant up* to launch a VM based on Vagrantfile provided.
#. After Vagrant provision, your VM is properly configured to launch acceptance tests. You have to access to the VM using *vagrant ssh* and change to */vagrant* directory that will have mounted your workspace *(test/acceptance)*.

If you need more information about how to use Vagrant, you can see
`Vagrant Getting Started <https://docs.vagrantup.com/v2/getting-started/index.html>`_


Test structure and prerequisites
---------------------------------

Acceptance tests have two type of test cases:

- **Component test cases**: They try to test Sagitta catalog and they do not need additional prerequisites.
- **E2E test cases**: Those test cases try to test Sagitta provisioning functionality (Product installation and uninstallation) and they need running in the platform an installed and configured: Chef-Server, Puppet-Master and a *target VM* to install/uninstall products.

**Target VM for E2E testing purpose**

You should deploy a VM and assign a public IP in order to access to it with, this one must have **chef-client** and **puppet agent**
installed. When VM will be deployed, you should not launch chef-client and puppet agent daemons. Acceptance tests will
take care of it.


Configuration file
------------------
Some configuration is needed before test case execution. This configuration is set in the *commons/configuration.py*
file.

All configuration values will be 'strings'.

**Environment configuration**

Sagitta properties ::

    SDC_PROTOCOL, SDC_IP, SDC_PORT

CHEF SERVER properties ::

    CONFIG_CHEF_SERVER_IP, CONFIG_CHEF_SERVER_USERNAME, CONFIG_CHEF_SERVER_PASSWORD

PUPPET-MASTER properties ::

    CONFIG_PUPPET_MASTER_IP, CONFIG_PUPPET_MASTER_USERNAME, CONFIG_PUPPET_MASTER_PASSWORD, 
    CONFIG_PUPPETDB_PROTOCOL, CONFIG_PUPPETDB_IP, CONFIG_PUPPETDB_PORT

KEYSTONE properties ::

    TENANT_NAME_VALUE, USERNAME_VALUE, PWD_VALUE, KEYSTONE_URL

You will need setup a valid configuration properties for the environment (*KEYSTONE_URL*) you are going to use.

- You need a valid FIWARE credentials for the configured *KEYSTONE_URL*: User, Password and TenantId.
- You can get the public endpoints configuration (*_URL*, *_PROTOCOL*, *_IP*, *_PORT*) requesting for that
to the Service Catalog (Keystone).
- You need configure your Chef-Server and PuppetMaster endpoints or use the public ones in FIWARE arch.
- For E2E testing you will need setup User and Password for that servers.

**Recipe/Manifest configuration and target VM for E2E testing**

VM properties ::

    CONFIG_VM_HOSTNAME, CONFIG_VM_IP, CONFIG_VM_FQN, CONFIG_VM_USERNAME, CONFIG_VM_PASSWORD

Before launching E2E test cases, you must be sure that testing recipes/manifests have been added to Chef/Puppet.
These can be found in the folder *cookbooks_testing*.

Configuration properties for that are ::

    CONFIG_PRODUCT_NAME_CHEF, CONFIG_PRODUCT_NAME_CHEF_2, CONFIG_PRODUCT_VERSION_CHEF
    CONFIG_PRODUCT_NAME_PUPPET, CONFIG_PRODUCT_NAME_PUPPET_2, CONFIG_PRODUCT_VERSION_PUPPET


Tests execution
---------------
- Go to the *test/acceptance* folder of the project if not already on it or, if you are using Vagrant, change to */vagrant*
- Run *lettuce_tools --tags=-skip*. This command will execute all acceptance tests (see available params with the -h option)
