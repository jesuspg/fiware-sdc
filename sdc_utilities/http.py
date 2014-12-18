#!/usr/bin/env python
# -*- encoding: utf-8 -*-
# Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U
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


import httplib
from xml.dom.minidom import parse, parseString
from urlparse import urlparse
import sys
import json

import httplib
import mimetypes


def post_multipart(host, port, selector, fields, files):
    content_type, body = encode_multipart_formdata(fields, files)
    h = httplib.HTTP(host, port)
    h.putrequest('POST', selector)
    h.putheader('content-type', content_type)
    h.putheader('content-length', str(len(body)))
    h.endheaders()
    h.send(body)
    errcode, errmsg, headers = h.getreply()
    print errcode
    return h.file.read()


def encode_multipart_formdata(fields, files):
    LIMIT = '100'
    dd = '\r\n'
    L = []
    for (key, value) in fields:
        L.append('--' + LIMIT)
        L.append('Content-Disposition: form-data; name="%s"' % key)
        L.append('')
        L.append(value)
    print files
    for (filename, value) in files:
        L.append('--' + LIMIT)
        L.append('Content-Disposition: form-data; name="%s"; '
                 'filename="%s"' % (filename, filename))
        L.append('Content-Type: %s' % get_content_type(filename))
        L.append('')
        L.append(value)
    L.append('--' + LIMIT + '--')
    L.append('')
    print L
    body = dd.join(L)
    content_type = 'multipart/form-data; boundary=%s' % LIMIT
    return content_type, body


def get_content_type(filename):
    return mimetypes.guess_type(filename)[0] or 'application/octet-stream'


def __do_http_req(method, url, headers, payload):

    parsed_url = urlparse(url)
    con = httplib.HTTPConnection(parsed_url.netloc)
    con.request(method, parsed_url.path, payload, headers)
    return con.getresponse()


def __do_http_req_https(method, url, headers, payload):
    parsed_url = urlparse(url)
    con = httplib.HTTPSConnection(parsed_url.netloc)
    con.request(method, parsed_url.path, payload, headers)
    return con.getresponse()


def get(url, headers):
    if url.startswith('https'):
        return __do_http_req_https("GET", url, headers, None)
    else:
        return __do_http_req("GET", url, headers, None)


def delete(url, headers):
    if url.startswith('https'):
        return __do_http_req_https("DELETE", url, headers, None)
    else:
        return __do_http_req("DELETE", url, headers, None)


        ##
        ## Metod que hace el HTTP-PUT
        ##


def __put(url, headers):
    if url.startswith ('https'):
        return __do_http_req_https("PUT", url, headers, None)
    else:
        return __do_http_req("PUT", url, headers, None)


        ##
        ## Metod que hace el HTTP-POST
        ##


def post(url, headers, payload):
    if url.startswith ('https'):
        return __do_http_req_https("POST", url, headers, payload)
    else:
        return __do_http_req("POST", url, headers, payload)


def get_token(keystone_url, tenant, user, password):

# url="%s/%s" %(keystone_url,"v2.0/tokens")
    headers = {'Content-Type': 'application/json',
               'Accept': "application/xml"}
    payload = '{"auth":{"tenantName":"' + tenant +\
              '","passwordCredentials":{"username":"'\
              + user + '","password":"' + password + '"}}}'

    response = post(keystone_url, headers, payload)
    data = response.read()

    if response.status != 200:
        print 'error to obtain the token ' + str(response.status)
        sys.exit(1)
    else:
        dom = parseString(data)
        try:
            result = (dom.getElementsByTagName('token'))[0]
            var = result.attributes["id"].value

            return var
        except:
            print ("Error in the processing enviroment")
            sys.exit(1)


def processTask(headers, taskdom):
    try:
        href = taskdom["@href"]
        status = taskdom["@status"]
        while status == 'RUNNING':
            data1 = get_task(href, headers)
            data = json.loads(data1)
            status = data["@status"]

        if status == 'ERROR':
            error = data["error"]
            message = error["@message"]
            raise Exception('Error deploying the environt instnace' + str(message))
        return status
    except Exception as e:
        raise Exception('Unexpected error in parsing ' + e.message)


def get_task(url, headers):

# url="%s/%s" %(keystone_url,"v2.0/tokens")
    response = get(url, headers)

    ## Si la respuesta es la adecuada, creo el diccionario de los datos en JSON.
    if response.status != 200:
        print 'error to obtain the token ' + str(response.status)
        sys.exit(1)
    else:
        data = response.read()
        return data
