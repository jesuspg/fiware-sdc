__author__ = 'arobres'

from utils import delete_keys_when_value_is_none
from constants import PRODUCT_INSTANCE, PRODUCT_INSTANCE_VM, PRODUCT_INSTANCE_VM_HOSTNAME, PRODUCT_INSTANCE_VM_IP, \
    PRODUCT_INSTANCE_VM_FQN, PRODUCT_INSTANCE_VM_OSTYPE, PRODUCT, PRODUCT_NAME, VERSION, PRODUCT_INSTANCE_ATTRIBUTES


def installation_body_with_attributes(product_name=None, product_version=None, hostname=None, ip=None, fqn=None,
                                      ostype=None, attributes=None):
    if len(attributes) == 1:
        attributes = attributes[0]

    vm_dict = build_vm_body(hostname=hostname, ip=ip, fqn=fqn, ostype=ostype)
    return {PRODUCT_INSTANCE: {PRODUCT: {PRODUCT_NAME: product_name, VERSION: product_version},
                               PRODUCT_INSTANCE_VM: vm_dict, PRODUCT_INSTANCE_ATTRIBUTES: attributes}}


def simple_installation_body(product_name=None, product_version=None, hostname=None, ip=None, fqn=None,
                             ostype=None):

    vm_dict = build_vm_body(hostname=hostname, ip=ip, fqn=fqn, ostype=ostype)
    return {PRODUCT_INSTANCE: {PRODUCT: {PRODUCT_NAME: product_name, VERSION: product_version},
                               PRODUCT_INSTANCE_VM: vm_dict}}


def build_vm_body(hostname=None, ip=None, fqn=None, ostype=None):

    default_dict = {PRODUCT_INSTANCE_VM_HOSTNAME: hostname, PRODUCT_INSTANCE_VM_IP: ip, PRODUCT_INSTANCE_VM_FQN: fqn,
                    PRODUCT_INSTANCE_VM_OSTYPE: ostype}
    return delete_keys_when_value_is_none(default_dict)