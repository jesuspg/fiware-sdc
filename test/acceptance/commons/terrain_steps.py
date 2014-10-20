__author__ = 'arobres, jfernandez'

from lettuce import world, before, after
from commons.constants import *
from commons.rest_utils import RestUtils
from commons.authentication import get_token
from commons.utils import set_default_headers

api_utils = RestUtils()


def init_vars():
    world.product_name = None
    world.product_version = None
    world.instance_status = None
    world.vm_ip = None
    world.vm_hostname = None
    world.vm_fqn = ''
    world.vm_ostype = None
    world.instance_status = None
    world.attributes = None
    world.metadatas = None
    world.cm_tool = 'puppet'
    world.file_name = None
    world.instance_status = None
    world.instance_attributes = None


def setup_feature(feature):
    world.token_id, world.tenant_id = get_token()


def setup_scenario(scenario):
    init_vars()
    world.headers = set_default_headers(world.token_id, world.tenant_id)
    api_utils.delete_all_testing_products(world.headers)


def setup_outline(param1, param2, param3, param4):
    setup_scenario(None)


def tear_down(scenario):
    world.token_id, world.tenant_id = get_token()
    world.headers = set_default_headers(world.token_id, world.tenant_id)
    api_utils.delete_all_testing_products(world.headers)
