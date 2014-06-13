__author__ = 'arobres'

from utils import delete_keys_when_value_is_none
from constants import PRODUCT_INSTANCE, VM, HOSTNAME, IP, FQN, OSTYPE, PRODUCT, PRODUCT_NAME, VERSION


def simple_installation_body(product_name=None, product_version=None, hostname=None, ip=None, fqn=None, ostype=None):

    vm_dict = build_vm_body(hostname=hostname, ip=ip, fqn=fqn, ostype=ostype)
    return {PRODUCT_INSTANCE: {PRODUCT: {PRODUCT_NAME: product_name, VERSION: product_version},
                                   VM: vm_dict}}


def build_vm_body(hostname=None, ip=None, fqn=None, ostype=None):

    default_dict = {HOSTNAME: hostname, IP: ip, FQN: fqn, OSTYPE: ostype}
    return delete_keys_when_value_is_none(default_dict)
