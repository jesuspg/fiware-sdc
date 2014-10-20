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

"""
Imports all steps already defined and implemented in 'install_product' feature
"""
from component.install_product.features.install_product import *
from commons.utils import wait_for_software_installed, generate_content_installed_by_product
from commons.fabric_utils import execute_content_in_file


@step(u'a installed product with name "([^"]*)" and release "([^"]*)"')
def installed_product(step, product_name, product_version):

    a_created_product_with_name_group1(step, product_name, product_version)
    i_install_the_product_in_the_vm(step)
    the_task_is_performed(step)


@step(u'the task is performed')
def the_task_is_performed(step):
    the_task_has_finished_with_status_group1(step, TASK_STATUS_VALUE_SUCCESS)


@step(u'the product is installed')
def the_product_is_installed(step):
    world.file_name = PRODUCT_FILE_NAME_FORMAT.format(product_name=world.product_name,
                                                      product_version=world.product_version,
                                                      installator=world.cm_tool)

    assert_true(wait_for_software_installed(status_to_be_finished=True, file_name=world.file_name),
                "ERROR: SOFTWARE IS NOT INSTALLED")


@step(u'the product with attributes is installed')
def the_product_with_attributes_is_installed(step):
    the_product_is_installed(step)

    for attribute in world.instance_attributes:
        assert_true(execute_content_in_file(world.file_name,
                                            generate_content_installed_by_product(world.product_name,
                                                                                  world.product_version,
                                                                                  world.instance_attributes)),
                    "Attribute value not found in product installed [{}]".format(attribute[VALUE]))
