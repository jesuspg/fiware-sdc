from fabric.api import run, env, settings, task
from fabric.tasks import execute as fabric_execute
from fabric.contrib import files
from configuration import PROVISION_HOST_IP, PROVISION_HOST_PWD, PROVISION_HOST_USERNAME, PROVISION_ROOT_PATH
from constants import FABRIC_RESULT_EXECUTE

env.user = PROVISION_HOST_USERNAME
env.password = PROVISION_HOST_PWD
env.host_string = PROVISION_HOST_IP


def assert_file_exist(test_file_name):

    file_path = PROVISION_ROOT_PATH.format(test_file_name)
    return files.exists(file_path)


def execute_file_exist(test_file_name):

    success = fabric_execute(assert_file_exist, test_file_name=test_file_name)
    return success[FABRIC_RESULT_EXECUTE]
