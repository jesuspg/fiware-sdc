# -*- coding: utf-8 -*-
# Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U
#
# This file is part of FI-WARE project.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
#
# You may obtain a copy of the License at:
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#
# See the License for the specific language governing permissions and
# limitations under the License.
#
# For those usages not covered by the Apache version 2.0 License please
# contact with opensource@tid.es

__author__ = 'arobres, jfernandez'

from fabric.api import env, get, run
from fabric.tasks import execute as fabric_execute
from fabric.contrib import files
from configuration import CONFIG_VM_IP, CONFIG_VM_PASSWORD, CONFIG_VM_USERNAME, PROVISION_ROOT_PATH, \
    CONFIG_CHEF_SERVER_USERNAME, CONFIG_CHEF_SERVER_PASSWORD, CONFIG_CHEF_SERVER_IP, CONFIG_PUPPET_MASTER_IP,\
    CONFIG_PUPPET_MASTER_PASSWORD, CONFIG_PUPPET_MASTER_USERNAME
from constants import FABRIC_RESULT_EXECUTE
from StringIO import StringIO

# Command templates to be used by this helper
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
COMMAND_RM_ALL_TESTFILES = "rm -rf /tmp/qa-test-product-*"
COMMAND_RM_PUPPET_CLIENT_CATALOG = "rm -f /var/lib/puppet/client_data/catalog/*"


def _init_vm_connection():
    """
    Init Fabric environment with VM credentials
    :return: None
    """
    env.user = CONFIG_VM_USERNAME
    env.password = CONFIG_VM_PASSWORD
    env.host_string = CONFIG_VM_IP


def _init_chef_server_connection():
    """
    Init Fabric environment with Chef-Server credentials
    :return: None
    """
    env.user = CONFIG_CHEF_SERVER_USERNAME
    env.password = CONFIG_CHEF_SERVER_PASSWORD
    env.host_string = CONFIG_CHEF_SERVER_IP


def _init_puppet_master_connection():
    """
    Init Fabric environment with Puppet Master credentials
    :return:
    """
    env.user = CONFIG_PUPPET_MASTER_USERNAME
    env.password = CONFIG_PUPPET_MASTER_PASSWORD
    env.host_string = CONFIG_PUPPET_MASTER_IP


def assert_file_exist(test_file_name):
    """
    Fabric assertion: Check if file (result of installing a test product) exists on the current remote hosts.
    :param test_file_name: File name
    :return: True if given file exists on the current remote host (dir: PROVISION_ROOT_PATH).
    """

    file_path = PROVISION_ROOT_PATH.format(test_file_name)
    return files.exists(file_path)


def assert_content_in_file(file_name, expected_content):
    """
    Fabric assertion: Check if some text is in the specified file (result of installing a test product)
    Provision dir: PROVISION_ROOT_PATH
    :param file_name: File name
    :param expected_content: String to be found in file
    :return: True if given content is in file (dir: PROVISION_ROOT_PATH).
    """

    file_path = PROVISION_ROOT_PATH.format(file_name)

    fd = StringIO()
    get(file_path, fd)
    file_content = fd.getvalue()

    return expected_content in file_content


def execute_file_exist(test_file_name):
    """
    Fabric executor: Run method with assertion 'assert_file_exist' on the remote host
    :param test_file_name: Target file name
    :return: True if file contains that content (dir: PROVISION_ROOT_PATH)
    """
    print "FABRIC: Checking if file exists"
    _init_vm_connection()

    success = fabric_execute(assert_file_exist, test_file_name=test_file_name)
    return success[FABRIC_RESULT_EXECUTE]


def execute_content_in_file(file_name, expected_content):
    """
    Fabric executor: Run method with assertion 'assert_content_in_file' on the remote host
    :param file_name: Target file name
    :param expected_content: String to be found in file
    :return: True if file contains that content (dir: PROVISION_ROOT_PATH)
    """
    print "FABRIC: Checking if file contains:", expected_content
    _init_vm_connection()

    success = fabric_execute(assert_content_in_file, file_name=file_name, expected_content=expected_content)
    return success[FABRIC_RESULT_EXECUTE]


def _execute_command(command):
    """
    Execute a shell command on the current remote host
    :param command: Command to be execute
    :return: Result of the remote execution or None if some problem happens
    """
    print "FABRIC: Executing remote command:", command
    try:
        result = run(command)
        return result
    except:
        print "  WARNING: Any problem executing command"
        return None


def execute_chef_client():
    """
    Init environment to connect to VM and execute 'chef-client' (runs as a daemon)
    :return: Result of the remote execution
    """
    _init_vm_connection()
    return _execute_command(COMMAND_CHEF_CLIENT)


def execute_chef_client_once():
    """
    Init environment to connect to VM and execute 'chef-client' runs ones)
    :return: Result of the remote execution
    """
    _init_vm_connection()
    return _execute_command(COMMAND_CHEF_CLIENT_ONCE)


def execute_chef_client_stop():
    """
    Init environment to connect to VM and kill all 'chef-client' process
    :return: Result of the remote execution
    """
    _init_vm_connection()
    return _execute_command(COMMAND_CHEF_CLIENT_STOP)


def remove_chef_client_cert_file():
    """
    Init environment to connect to VM and remove Chef client certificates
    :return: Result of the remote execution
    """
    _init_vm_connection()
    return _execute_command(COMMAND_RM_CHEF_CERTS)


def execute_puppet_agent():
    """
    Init environment to connect to VM and execute 'puppet agent' (runs as a daemon)
    :return: Result of the remote execution
    """
    _init_vm_connection()
    return _execute_command(COMMAND_PUPPET_AGENT)


def execute_puppet_agent_once():
    """
    Init environment to connect to VM and execute 'puppet agent' (ones)
    :return: Result of the remote execution
    """
    _init_vm_connection()
    return _execute_command(COMMAND_PUPPET_AGENT_ONCE)


def execute_puppet_agent_stop():
    """
    Init environment to connect to VM and kill all 'puppet' process
    :return: Result of the remote execution
    """
    _init_vm_connection()
    return _execute_command(COMMAND_PUPPET_AGENT_STOP)


def get_chef_node_info_from_server(node_name):
    """
    Init environment to connect to Chef-Server and retrieve the information for that node in the server (if exists)
    :param node_name: Node name
    :return: Information about the node that is managed by Chef-Server or None if this node has not been found
    """
    _init_chef_server_connection()
    return _execute_command(COMMAND_KNIFE_NODE_SHOW.format(node_name))


def remove_puppet_agent_cert_file():
    """
    Init environment to connect to VM and remove Puppet client certificates
    :return: Result of the remote execution
    """
    _init_vm_connection()
    return _execute_command(COMMAND_RM_PUPPET_CERTS)


def get_puppet_node_cert_from_server(node_name):
    """
    Init environment to connect to Puppet Master and retrieve the certificate for that node in the server (if exists)
    :param node_name: Name of target node
    :return: Certificate for that node in Puppet Master or None if this information has not been found
    """
    _init_puppet_master_connection()
    return _execute_command(COMMAND_PUPPET_GET_CERT.format(node_name))


def remove_all_generated_test_files():
    """
    Init environment to connect to VM and remove all files generated by recipe/manifest execution
    :return: Result of the remote execution
    """
    _init_vm_connection()
    return _execute_command(COMMAND_RM_ALL_TESTFILES)


def remove_puppet_agent_catalog():
    """
    Init environment to connect to VM and remove the client catalog (agent) after manifest execution
    :return: Result of the remote execution
    """
    _init_vm_connection()
    return _execute_command(COMMAND_RM_PUPPET_CLIENT_CATALOG)
