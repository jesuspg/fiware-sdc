from fabric.api import env, get, run
from fabric.tasks import execute as fabric_execute
from fabric.contrib import files
from configuration import CONFIG_VM_IP, CONFIG_VM_PASSWORD, CONFIG_VM_USERNAME, PROVISION_ROOT_PATH, \
    CONFIG_CHEF_SERVER_USERNAME, CONFIG_CHEF_SERVER_PASSWORD, CONFIG_CHEF_SERVER_IP, CONFIG_PUPPET_MASTER_IP,\
    CONFIG_PUPPET_MASTER_PASSWORD, CONFIG_PUPPET_MASTER_USERNAME
from constants import FABRIC_RESULT_EXECUTE
from StringIO import StringIO

COMMAND_CHEF_CLIENT = "chef-client -i 10 -d"
COMMAND_CHEF_CLIENT_ONCE = "chef-client"
COMMAND_CHEF_CLIENT_STOP = "killall chef-client"
COMMAND_RM_CHEF_CERTS = "rm /etc/chef/client.pem"
COMMAND_PUPPET_AGENT = "puppet agent --daemonize"
COMMAND_PUPPET_AGENT_ONCE = "puppet agent --test"
COMMAND_PUPPET_AGENT_STOP = "killall puppet"
COMMAND_KNIFE_NODE_SHOW = "knife node show {}"
COMMAND_PUPPET_GET_CERT = "puppet cert list --all | grep {}"
COMMAND_RM_PUPPET_CERTS = "rm -r -f /var/lib/puppet/ssl/*"


def _init_vm_connection():
    env.user = CONFIG_VM_USERNAME
    env.password = CONFIG_VM_PASSWORD
    env.host_string = CONFIG_VM_IP


def _init_chef_server_connection():
    env.user = CONFIG_CHEF_SERVER_USERNAME
    env.password = CONFIG_CHEF_SERVER_PASSWORD
    env.host_string = CONFIG_CHEF_SERVER_IP


def _init_puppet_master_connection():
    env.user = CONFIG_PUPPET_MASTER_USERNAME
    env.password = CONFIG_PUPPET_MASTER_PASSWORD
    env.host_string = CONFIG_PUPPET_MASTER_IP


def assert_file_exist(test_file_name):

    file_path = PROVISION_ROOT_PATH.format(test_file_name)
    return files.exists(file_path)


def assert_content_in_file(file_name, expected_content):

    file_path = PROVISION_ROOT_PATH.format(file_name)

    fd = StringIO()
    get(file_path, fd)
    file_content = fd.getvalue()

    return expected_content in file_content


def execute_file_exist(test_file_name):
    print "FABRIC: Checking if file exists"
    _init_vm_connection()

    success = fabric_execute(assert_file_exist, test_file_name=test_file_name)
    return success[FABRIC_RESULT_EXECUTE]


def execute_content_in_file(file_name, expected_content):
    print "FABRIC: Checking if file contains:", expected_content
    _init_vm_connection()

    success = fabric_execute(assert_content_in_file, file_name=file_name, expected_content=expected_content)
    return success[FABRIC_RESULT_EXECUTE]


def _execute_command(command):
    print "FABRIC: Executing remote command:", command
    try:
        result = run(command)
        return result
    except:
        print "  WARNING: Any problem executing command"
        return None


def execute_chef_client():
    _init_vm_connection()
    return _execute_command(COMMAND_CHEF_CLIENT)


def execute_chef_client_once():
    _init_vm_connection()
    return _execute_command(COMMAND_CHEF_CLIENT_ONCE)


def execute_chef_client_stop():
    _init_vm_connection()
    return _execute_command(COMMAND_CHEF_CLIENT_STOP)


def remove_chef_client_cert_file():
    _init_vm_connection()
    return _execute_command(COMMAND_RM_CHEF_CERTS)


def execute_puppet_agent():
    _init_vm_connection()
    return _execute_command(COMMAND_PUPPET_AGENT)


def execute_puppet_agent_once():
    _init_vm_connection()
    return _execute_command(COMMAND_PUPPET_AGENT_ONCE)


def execute_puppet_agent_stop():
    _init_vm_connection()
    return _execute_command(COMMAND_PUPPET_AGENT_STOP)


def get_chef_node_info_from_server(node_name):
    _init_chef_server_connection()
    return _execute_command(COMMAND_KNIFE_NODE_SHOW.format(node_name))


def remove_puppet_agent_cert_file():
    _init_vm_connection()
    return _execute_command(COMMAND_RM_PUPPET_CERTS)


def get_puppet_node_cert_from_server(node_name):
    _init_puppet_master_connection()
    return _execute_command(COMMAND_PUPPET_GET_CERT.format(node_name))
