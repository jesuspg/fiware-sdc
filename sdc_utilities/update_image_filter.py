__author__ = 'henar'
import sys
import http
import requests
import json





def get_images_filter ():
    token = get_token ('cloud.lab.fi-ware.org:4731',
                      '00000000000000000000000000000081',
                      'henar@tid.es',
                      'vallelado')
    endpoints = get_endpoints ('cloud.lab.fi-ware.org:4731',
                      '00000000000000000000000000000081',
                      'henar@tid.es',
                      'vallelado')

    products = get_product_with_image_filtered (endpoints['sdc']['Spain'], token, '00000000000000000000000000000081')


    for product in products:
        print product
        image_list = product [1].split(' ')
        image_ids = ''
        for image in image_list:
            try:
                try:
                    image_ids.index (image)
                    continue
                except:
                    image_name = check_image_in_spain (endpoints['image']['Spain'], image, token)
                    if image_name == None:
                        print 'image no valid in Spain region ' + image
                        continue
                    image_ids = image_ids + ' ' + image
                    print 'product ' + product [0] +  image_ids
                    for endpoint_glace in endpoints['image']:
                        if endpoint_glace == 'Spain':
                            continue
                        image_id = get_image_id_anotherregion(endpoints['image'][endpoint_glace], image_name, token)
                        if image_id == None:
                            continue
                        image_ids = image_ids + ' ' + image_id
                        print    'product ' + product [0] + ' ' + endpoint_glace + ' ' +  image_id

                    print 'product ' + product [0] +  image_ids
            except Exception, e:
                print 'error with image ' + image
        print image_ids





def get_token ( url_base, tenant_id, user, password):
    url = 'http://' + url_base + '/v2.0/tokens'
    headers = {'Accept': 'application/json'}
    payload = '{"auth":{"tenantName":"' + tenant_id + '","passwordCredentials":{"username":"' + user + '","password":"' + password + '"}}}'
    response = requests.post(url, headers=headers, data=payload)
    if response.status_code != 200:
        print 'error to obtain the token ' + str(response.status_code)
        exit(1)
    response_json = response.json()
    token = response_json['access']['token']['id']
    return token


def get_sdc_url (url_base, tenant_id, user, password):
    get_url(url_base, tenant_id, user, password, 'sdc', 'Spain')

def get_glance_url (url_base, tenant_id, user, password, region):
    get_url(url_base, tenant_id, user, password, 'image', region)

def get_url (url_base, tenant_id, user, password, type, region):
    url = 'http://' + url_base + '/v2.0/tokens'
    headers = {'Accept': 'application/json'}
    payload = {'auth': {'tenantName': '' + tenant_id + '',
                        'passwordCredentials': {'username': '' + user + '', 'password': '' + password + ''}}}
    try:
        response = requests.post(url, headers=headers, data=json.dumps(payload))
        response_json = response.json()
        services = response_json['access']['serviceCatalog']
    except Exception as e:
        raise Exception('Error to obtain a image ' + e.message)

    for service in services:
        if service['type'] == type and service ['region'] == region:
            for endpoint in service['endpoints']:
                return endpoint['publicURL']

def get_endpoints (url_base, tenant_id, user, password):
    url = 'http://' + url_base + '/v2.0/tokens'
    headers = {'Accept': 'application/json'}
    payload = {'auth': {'tenantName': '' + tenant_id + '',
                        'passwordCredentials': {'username': '' + user + '', 'password': '' + password + ''}}}
    try:
        response = requests.post(url, headers=headers, data=json.dumps(payload))
        response_json = response.json()
        services = response_json['access']['serviceCatalog']
    except Exception as e:
        raise Exception('Error to obtain a image ' + e.message)

    endpoints = {}
    for service in services:
        service_endpoints = {}
        for endpoint in service['endpoints']:
            service_endpoints.update({endpoint ['region'] : endpoint['publicURL'] } )
        endpoints.update({service['type'] : service_endpoints})
    return endpoints


def get_product_with_image_filtered (sdc_url, token, vdc):

    url = "%s/%s" % (sdc_url, "catalog/product")
    headers = {'X-Auth-Token': token, 'Tenant-Id': vdc,
                   'Accept': "application/json", 'Content-Type': 'application/json'}

    response = http.get(url, headers)

    ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
    if response.status != 200:
        print 'error to add the product sdc ' + str(response.status)
        sys.exit(1)
    else:
        data = json.loads(response.read())
        if data == None:
           return None
        products = []
        for product in data:
            if product['name'] == 'orion':
                print 'orion'
            try :
                for metadata in product ['metadatas']:
                    if metadata == 'value':
                        if product['metadatas']['key'] == 'image' and product['metadatas']['value'] != '' :
                           value = []
                           value.append(product['name'])
                           value.append(product['metadatas']['value'])
                           products.append (value);

                    if metadata['key'] == 'image' and metadata['value'] != '':
                        value = []
                        value.append(product['name'])
                        value.append(metadata['value'])
                        products.append (value);

            except:
                continue
        return products


def check_image_in_spain(glance_url, id, token):
    url = glance_url +'/images?limit=100'
    headers = {'Accept': 'application/json', 'X-Auth-Token': token}

    try:
        response = requests.get(url, headers=headers)
        response_json = response.json()
        for image in response_json['images']:
            if image ['id']==id:
                return image['name']
        return None
    except Exception as e:
        raise Exception('Error to obtain a image ' + e.message)

def get_image_id_anotherregion (glance_url, image_name, token):
    url = glance_url +'/images'
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



get_images_filter()