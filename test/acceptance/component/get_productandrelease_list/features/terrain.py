__author__ = 'jfernandez'

from commons.terrain_steps import *


@before.each_feature
def before_each_feature(feature):
    setup_feature(feature)


@before.each_scenario
def before_each_scenario(scenario):
    setup_scenario(scenario)


@before.outline
def before_outline(param1, param2, param3, param4):
    setup_outline(param1, param2, param3, param4)


@after.all
def after_all(scenario):
    tear_down(scenario)
