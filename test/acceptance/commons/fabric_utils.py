from fabric.api import env, get
from fabric.tasks import execute as fabric_execute
from fabric.contrib import files
from configuration import CONFIG_VM_IP, CONFIG_VM_PASSWORD, CONFIG_VM_USERNAME, PROVISION_ROOT_PATH
from constants import FABRIC_RESULT_EXECUTE
from StringIO import StringIO

env.user = CONFIG_VM_USERNAME
env.password = CONFIG_VM_PASSWORD
env.host_string = CONFIG_VM_IP


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

    success = fabric_execute(assert_file_exist, test_file_name=test_file_name)
    return success[FABRIC_RESULT_EXECUTE]


def execute_content_in_file(file_name, expected_content):

    print "Checkin if file contains:", expected_content

    success = fabric_execute(assert_content_in_file, file_name=file_name, expected_content=expected_content)
    return success[FABRIC_RESULT_EXECUTE]
