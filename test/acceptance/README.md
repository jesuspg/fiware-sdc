# SDC API Acceptance Tests

Folder for acceptance tests of SDC API.

## How to Run the Acceptance Tests

### Prerequisites:

- Python 2.6 or newer

- pip installed (http://docs.python-guide.org/en/latest/starting/install/linux/)

- virtualenv installed (pip install virtalenv)

- To run the Product Provisioning tests it's required a instance in OpenStack and registered in the Puppet Wrapper. This instance should has installed the puppet client in order to install products. This instance should have the name "testing"


### Configuration file

- The Product Provisioning files requires the configuration of the instance to install products. This configuration is set in the commons/configuration.py file. The parameters to be set should be the following:

	- PROVISION_HOST_IP = Instance IP
	- PROVISION_HOST_USERNAME = username to connect via SSH to the host
	- PROVISION_HOST_PWD = Passowrd to connect via SSH to the host
	- PROVISION_ROOT_PATH = u'/tmp/{}'

### Environment preparation:

- Create a virtual environment somewhere, e.g. in ~/venv (virtualenv ~/venv)

- Activate the virtual environment (source ~/venv/bin/activate)

- Make sure pdihub.hi.inet domain is reachable from your system (some of the requirements come from it) and instruct Git not to validate SSL connections to it (export GIT\_SSL\_NO\_VERIFY=true).

- Change to the test/acceptance folder of the project

- Install the requirements for the acceptance tests in the virtual environment (pip install -r requirements.txt --allow-all-external).

### Tests execution:

- Change to the test/acceptance folder of the project if not already on it

- Run lettucetdaf (see available params with the -h option)
