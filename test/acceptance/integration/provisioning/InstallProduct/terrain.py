# -*- coding: utf-8 -*-
from lettuce import world, after, before
from tools import catalogue_request
from tools.catalogue_request import CatalogueRequest
from tools.provisioning_request import ProvisioningRequest




@before.each_scenario
def before_each_scenario(scenario):
    world.env_requests = ProvisioningRequest(world.config['paas']['keystone_url'],
                                             world.config['paas']['paasmanager_url'],
                                             world.config['paas']['tenant'],
                                             world.config['paas']['user'],
                                             world.config['paas']['password'],
                                             world.config['paas']['vdc'],
                                             world.config['paas']['sdc_url'],
                                             world.config['paas']['VM_IP'],
                                             world.config['paas']['VM_hostname'])

    world.catalogue = CatalogueRequest(world.config['paas']['keystone_url'],
                                       world.config['paas']['paasmanager_url'],
                                       world.config['paas']['tenant'],
                                       world.config['paas']['user'],
                                       world.config['paas']['password'],
                                       world.config['paas']['vdc'],
                                       world.config['paas']['sdc_url'])
NOT_ERROR = ""
AcceptXML = "xml"

@after.each_scenario
def after_each_scenario(scenario):
    #Uninstall in a VM and delete the product created in the catalogue.
    world.env_requests.provisioning_uninstallProduct(world.product, world.version, "fqn", NOT_ERROR, AcceptXML)
    world.catalogue.catalogue_deleteProductRelease(world.product, world.version, NOT_ERROR)
    world.catalogue.catalogue_deleteProduct(world.product, AcceptXML, NOT_ERROR)
    pass
