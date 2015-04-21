#!/usr/bin/env python
# -*- encoding: utf-8 -*-
#
# Copyright 2014 Telefónica Investigación y Desarrollo, S.A.U
#
# This file is part of FI-WARE project.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
#
# You may obtain a copy of the License at:
#
#        http://www.apache.org/licenses/LICENSE-2.0
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
#
import sys
import http
import requests
import json
import os


def get_images_filter():
    """It prints all products with metatada image, and obtain all the
       images in all regions.
    """
    KEYSTONE = os.environ.get('OS_KEYSTONE')
    TENANT_ID = os.environ.get('OS_TENANT_NAME')
    USERNAME = os.environ.get('OS_USERNAME')
    PASSWORD = os.environ.get('OS_PASSWORD')

    token = get_token(KEYSTONE,
                      TENANT_ID,
                      USERNAME,
                      PASSWORD)
    endpoints = get_endpoints(KEYSTONE,
                              TENANT_ID,
                              USERNAME,
                              PASSWORD)

    products = get_product_with_image_filtered(endpoints['sdc']['Spain'],
                                               token, TENANT_ID)

    for product in products:
        image_list = product[1].split(' ')
        image_ids = ''
        for image in image_list:
            try:
                try:
                    image_ids.index(image)
                    continue
                except:
                    image_name = check_image_in_spain(
                        endpoints['image']['Spain'], image, token)
                    if image_name is None:
                        continue

                    image_ids = image_ids + ' ' + image
                    for endpoint_glace in endpoints['image']:
                        if endpoint_glace == 'Spain':
                            continue
                        image_id = get_image_id_another_region(
                            endpoints['image'][endpoint_glace],
                            image_name, token)
                        if image_id is None:
                            continue
                        image_ids = image_ids + ' ' + image_id
                    #update_metadata_image (endpoints['sdc']['Spain'],
                    #  token, TENANT_ID, product, image_ids)
            except Exception, e:
                print 'Error with image ' + image
        print 'product ' + product[0] + image_ids


def get_token(url_base, tenant_id, user, password):
    """It obtains a valid token.
    :param url_base: keystone url
    :param tenand_id: the id of the tenant
    :param user: the user
    :param paassword: the password
    """
    url = 'http://' + url_base + '/v2.0/tokens'
    headers = {'Accept': 'application/json'}
    payload = '{"auth":{"tenantName":"' + tenant_id +\
              '","passwordCredentials":{"username":"'\
              + user + '","password":"' + password + '"}}}'
    response = requests.post(url, headers=headers, data=payload)
    if response.status_code != 200:
        print 'error to obtain the token ' + str(response.status_code)
        exit(1)
    response_json = response.json()
    token = response_json['access']['token']['id']
    return token


def get_sdc_url(url_base, tenant_id, user, password):
    """It get the SDC url
    :param url_base: keystone url
    :param tenand_id: the id of the tenant
    :param user: the user
    :param paassword: the password
    """
    get_url(url_base, tenant_id, user, password, 'sdc', 'Spain')


def get_glance_url(url_base, tenant_id, user, password, region):
    """It get the glance url
    :param url_base: keystone url
    :param tenand_id: the id of the tenant
    :param user: the user
    :param paassword: the password
    """
    get_url(url_base, tenant_id, user, password, 'image', region)


def get_url(url_base, tenant_id, user, password, type, region):
    """It get the url for a concrete service
    :param url_base: keystone url
    :param tenand_id: the id of the tenant
    :param user: the user
    :param paassword: the password
    :param type: the type of service
    :param region: the region
    """
    url = 'http://' + url_base + '/v2.0/tokens'
    headers = {'Accept': 'application/json'}
    payload = {'auth': {'tenantName': '' + tenant_id + '',
                        'passwordCredentials':
                        {'username': '' + user + '',
                        'password': '' + password + ''}}}
    try:
        response = requests.post(url, headers=headers,
                                 data=json.dumps(payload))
        response_json = response.json()
        services = response_json['access']['serviceCatalog']
    except Exception as e:
        raise Exception('Error to obtain a image ' + e.message)

    for service in services:
        if service['type'] == type and service['region'] == region:
            for endpoint in service['endpoints']:
                return endpoint['publicURL']


def get_endpoints(url_base, tenant_id, user, password):
    """It get the endpoints
    :param url_base: keystone url
    :param tenand_id: the id of the tenant
    :param user: the user
    :param paassword: the password
    """
    url = 'http://' + url_base + '/v2.0/tokens'
    headers = {'Accept': 'application/json'}
    payload = {'auth': {'tenantName': '' + tenant_id + '',
                        'passwordCredentials': {'username':
                        '' + user + '', 'password': '' +
                        password + ''}}}
    try:
        response = requests.post(url, headers=headers,
                                 data=json.dumps(payload))
        response_json = response.json()
        services = response_json['access']['serviceCatalog']
    except Exception as e:
        raise Exception('Error to obtain a image ' + e.message)

    endpoints = {}
    for service in services:
        service_endpoints = {}
        for endpoint in service['endpoints']:
            service_endpoints.update({endpoint['region']:
                                      endpoint['publicURL']})
        endpoints.update({service['type']: service_endpoints})
    return endpoints


def get_product_with_image_filtered(sdc_url, token, vdc):
    """It get the product for an concrete image
    :param sdc_url: the sdc url
    :param token: the valid token
    :param vdc: the user
    """
    url = "%s/%s" % (sdc_url, "catalog/product")
    headers = {'X-Auth-Token': token, 'Tenant-Id': vdc,
               'Accept': "application/json"}

    response = http.get(url, headers)

    if response.status != 200:
        print 'error to get the product with image filtered ' + str(response.status)
        sys.exit(1)
    else:
        data = json.loads(response.read())
        if data is None:
            return None
        products = []
        for product in data:
            try:
                for metadata in product['metadatas']:
                    if metadata == 'value':
                        if product['metadatas']['key'] == 'image'\
                                and product['metadatas']['value'] != '':
                            value = []
                            value.append(product['name'])
                            value.append(product['metadatas']['value'])
                            products.append(value)

                    if metadata['key'] == 'image' and metadata['value'] != '':
                        value = []
                        value.append(product['name'])
                        value.append(metadata['value'])
                        products.append(value)

            except:
                continue
        return products


def check_image_in_spain(glance_url, id, token):
    """It obtain if the image is in Spain
    :param glance_url: the sdc url
    :param token: the valid token
    :param id: image id
    """
    url = glance_url + '/images?property-sdc_aware=true'
    headers = {'Accept': 'application/json', 'X-Auth-Token': token}

    try:
        response = requests.get(url, headers=headers)
        response_json = response.json()
        for image in response_json['images']:
            if image['id'] == id:
                return image['name']
        return None
    except Exception as e:
        raise Exception('Error to obtain a image ' + e.message)


def get_image_id_another_region(glance_url, image_name, token):
    """It obtains the image in another regions
    :param glance_url: the sdc url
    :param token: the valid token
    :param image_name: image name
    """
    url = glance_url + '/images?property-sdc_aware=true'
    headers = {'Accept': 'application/json', 'X-Auth-Token': token}

    try:
        response = requests.get(url, headers=headers)
        response_json = response.json()
        for image_info in response_json['images']:
            if image_info['name'] == image_name:
                return image_info['id']
        return None
    except Exception as e:
        return None


def update_metadata_image(sdc_url, token, vdc, product, metadata_image):
    """It updates the product metadada for image filtered
    :param glance_url: the sdc url
    :param token: the valid token
    :param metadata_image: image name
    :param product: image name

    """
    url = "%s/%s" % (sdc_url, "catalog/product/"+product)
    headers = {'X-Auth-Token': token, 'Tenant-Id': vdc,
               'Accept': "application/json",
               'Content-Type': 'application/json'}

    response = http.get(url, headers)

    if response.status != 200:
        print 'error to get the product ' + str(response.status)
        return
    else:
        payload = '{"metadata":{"key":"image","value"' + metadata_image + '"}}'
        response = http.put(url + "/metadatas/image", headers, data=payload)
        if response.status != 200:
            print 'error to update the product ' + product \
                  + ' ' + str(response.status)


if __name__ == "__main__":
    get_images_filter()
