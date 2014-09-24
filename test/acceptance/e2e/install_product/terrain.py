__author__ = 'jfernandez'

from commons.terrain_steps import *
from commons.provisioning_steps import ProvisioningSteps
from commons.constants import TASK_STATUS_VALUE_SUCCESS

provisioning_steps = ProvisioningSteps()


@before.each_feature
def before_each_feature(feature):
    setup_feature(feature)


@before.each_scenario
def before_each_scenario(scenario):
    setup_scenario(scenario)


#@after.each_scenario
#def after_each_scenario(scenario):
    #print "TERRAIN: Uninstalling product"
    #provisioning_steps.i_uninstall_a_installed_product_and_release(None)
    #provisioning_steps.the_task_has_finished_with_status_group1(None, TASK_STATUS_VALUE_SUCCESS)


@before.outline
def before_outline(param1, param2, param3, param4):
    setup_outline(param1, param2, param3, param4)


@after.all
def after_all(scenario):
    tear_down(scenario)
    api_utils.uninstall_all_products(world.headers)