__author__ = 'jfernandez'

from lettuce import step, world
from commons.rest_utils import RestUtils
from commons.utils import body_model_to_body_request, response_body_to_dict, generate_product_instance_id, \
    wait_for_task_finished
from commons.constants import *
from commons.configuration import CONFIG_VM_HOSTNAME, CONFIG_VM_IP, CONFIG_VM_FQN
from nose.tools import assert_equals, assert_true, assert_in
from commons.installation_body import simple_installation_body, installation_body_with_attributes
import re


class ProvisioningSteps():

    api_utils = RestUtils()

    def __init__(self):
        None

    @staticmethod
    def and_a_vm_with_this_parameters(step):
        """
        Given step. Configure a VM parameters. Load data from config file if value is "CONFIG_FILE"
        Set values from hash to world:
            world.vm_hostname
            world.vm_ip
            world.vm_fqn
            world.ostype
        :param step: Lettuce step data
        :return: Values in lettuce world.
        """

        if "hostname" in step.hashes[0]:
            hostname = step.hashes[0]['hostname']
            world.vm_hostname = CONFIG_VM_HOSTNAME if CONFIG_FILE == hostname else hostname
        if "ip" in step.hashes[0]:
            ip = step.hashes[0]['ip']
            world.vm_ip = CONFIG_VM_IP if CONFIG_FILE == ip else ip
        if "fqn" in step.hashes[0]:
            fqn = step.hashes[0]['fqn']
            world.vm_fqn = CONFIG_VM_FQN if CONFIG_FILE == fqn else fqn
        if "ostype" in step.hashes[0]:
            world.ostype = step.hashes[0]['ostype']

    def i_install_the_product_in_the_vm(self, step):
        """
        Install a product in a virtual machine.
        :param step: Lettuce step data
        :return: The response is set into world.response
        """

        if world.instance_attributes is None:
            body_model = simple_installation_body(product_name=world.product_name, product_version=world.product_version,
                                                hostname=world.vm_hostname, ip=world.vm_ip, fqn=world.vm_fqn,
                                                ostype=world.vm_ostype)
        else:
            body_model = installation_body_with_attributes(product_name=world.product_name,
                                                           product_version=world.product_version,
                                                           hostname=world.vm_hostname, ip=world.vm_ip,
                                                           fqn=world.vm_fqn, ostype=world.vm_ostype,
                                                           attributes=world.instance_attributes)

        body = body_model_to_body_request(body_model, world.headers[CONTENT_TYPE],
                                          body_model_root_element=PRODUCT_INSTANCE)

        world.response = self.api_utils.install_product(headers=world.headers, vdc_id=world.tenant_id, body=body)

    @staticmethod
    def task_is_created(step):
        """
        Checks if the task is created with the correct info
        :param step: Lettuce step
        :return: In world.task_id, the task id created
        """

        assert_true(world.response.ok, 'RESPONSE: {}'.format(world.response.content))

        response_headers = world.response.headers
        assert_in(response_headers[CONTENT_TYPE], world.headers[ACCEPT_HEADER],
                  'RESPONSE HEADERS: {}'.format(world.response.headers))

        response_body = response_body_to_dict(world.response, world.headers[ACCEPT_HEADER], with_attributes=True,
                                              xml_root_element_name=TASK)

        assert_equals(response_body[TASK_STATUS], TASK_STATUS_VALUE_RUNNING)
        assert_in(world.product_name, response_body[DESCRIPTION])
        assert_in(world.vm_hostname, response_body[DESCRIPTION])
        assert_equals(world.tenant_id, response_body[TASK_VDC])

        m = re.search('/task/(.*)$', response_body[TASK_HREF])
        world.task_id = m.group(1)

    def the_product_is_instantiated(self, step):
        """
        Checks if the product is instantiated with the correct data (values from world)
        :param step: Lettuce step data
        :return: world.instance_status with the installation status
        """

        world.instance_id = generate_product_instance_id(world.vm_fqn, world.product_name, world.product_version)
        response = self.api_utils.retrieve_product_instance(headers=world.headers, vdc_id=world.tenant_id,
                                                            product_instance_id=world.instance_id)

        assert_true(response.ok, 'RESPONSE: {}'.format(response))

        response_body = response_body_to_dict(response, world.headers[ACCEPT_HEADER],
                                              xml_root_element_name=PRODUCT_INSTANCE_RES)

        assert_equals(response_body[PRODUCT_INSTANCE_NAME], world.instance_id)
        assert_true(response_body[PRODUCT_INSTANCE_STATUS] != "")
        assert_equals(response_body[PRODUCT_INSTANCE_VM][PRODUCT_INSTANCE_VM_FQN], world.vm_fqn)
        assert_equals(response_body[PRODUCT_INSTANCE_VM][PRODUCT_INSTANCE_VM_HOSTNAME], world.vm_hostname)

        ip_aux = "" if world.vm_ip is None else world.vm_ip
        assert_equals(response_body[PRODUCT_INSTANCE_VM][PRODUCT_INSTANCE_VM_IP], ip_aux)

        assert_equals(response_body[PRODUCT_RELEASE][VERSION], world.product_version)
        assert_equals(response_body[PRODUCT_RELEASE][PRODUCT][PRODUCT_NAME], world.product_name)

        world.instance_status = response_body[PRODUCT_INSTANCE_STATUS]

    def the_product_installation_status_is(self, step, status):
        """
        Checks the product instalation status.
        :param step: Lettuce data
        :param status: Expected status to check
        :return:
        """

        if world.instance_status is None:
            world.instance_id = "{}_{}_{}".format(world.vm_fqn, world.product_name, world.product_version)
            response = self.api_utils.retrieve_product_instance(headers=world.headers, vdc_id=world.tenant_id,
                                                                product_instance_id=world.instance_id)
            assert_true(response.ok, 'RESPONSE: {}'.format(response))

            response_body = response_body_to_dict(response, world.headers[ACCEPT_HEADER],
                                                  xml_root_element_name=PRODUCT_INSTANCE_RES)
            world.instance_status = response_body[PRODUCT_INSTANCE_STATUS]

        assert_equals(world.instance_status, status)

    def i_uninstall_a_installed_product_and_release(self, step):
        """
        Uninstal a product. Use data from world
            world.vm_fqn, world.product_name, world.product_version, world.tenant_id
        :return: Response in world.response and instance_id in world.instance_id
        """
        world.instance_id = generate_product_instance_id(world.vm_fqn, world.product_name, world.product_version)
        world.response = self.api_utils.uninstall_product_by_product_instance_id(headers=world.headers,
                                                                                 vdc_id=world.tenant_id,
                                                                                 product_instance_id=world.instance_id)

    @staticmethod
    def the_task_has_finished_with_status_group1(step, status):
        """
        Wait for task's result and check that this one is the expected status
        :param step: Lettuce steps
        :param status: Expected status
        :return:
        """
        finished = wait_for_task_finished(vdc_id=world.tenant_id, task_id=world.task_id,
                                          status_to_be_finished=status, headers=world.headers)
        assert_true(finished, 'Task is not in the correct status. Expected: {}'.format(status))
