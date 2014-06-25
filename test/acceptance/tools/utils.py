# -*- coding: utf-8 -*-
__author__ = 'ivanl@tid.es'

from lettuce import world
from tools import body_message
import http

def errorLabel (value, error):
    if error == "wrong":
        return '1234567890'
    elif error == "empty":
        return ''
    else:
        return value

def errorUrl (value, error):
    """

    :param value:
    :param error:
    :param label:
    :return:
    """
    REST = "rest/"
    if error == "Not Found":
       pos = value.find(REST)+len(REST)          # add "error_" text after "rest/" resource
       value = value[:pos] + "error_" + value[pos:]    # ex: http://130.206.80.119:8082/sdc/rest/error_catalog/product/Product_test_0001
    return value


def getPosition(operation, content):
    """
     return the position where insert element
    :param operation:
    :param content:
    """
    for ID in body_message.position:
        if ID['operation'] == operation and ID['content'] == content:
            return -ID['position']


def body_oneElement (request, values, operation, content):
    """
     Add a element into a request
    :param request:
    :param value:
    :param operation:
    :param content:
    """
    body="" #str(values['label'])+ "   -- "+str(values['value'])
    positionToInsert = getPosition(operation, content)
    if content == 'xml':
        body=body+'<'+str(values['label'])+'>'+str(values['value'])+'</'+str(values['label'])+'>'
    elif content == 'json':
        body=body+'\"'+str(values['label']) +'\": \"'+str(values['value'])+'\"'
        if request != '{}': body = ','+body
    return insert_text(request,positionToInsert, body)

def body_elements(request, values, elementName, operation, content):
    """
    Add new multi elements into a request

    :param request:  Request before to insert new element
    :param values:   labels and values that will be inserted
    :param elementName: element name
    :param operation: operation in use, ex:
                    "installProduct"
    :param content: specify media types which are acceptable for the response, ex:
                 "xml", "json"
    :return: new request with new element added
    """
    body = ""
    positionToInsert = getPosition(operation, content)
    if content == 'xml':
        for ID in values:
            if ID['value'] is not None:
                body=body+"<"+ID['label']+">"+ID['value']+"</"+ID['label']+">"
        if body != "": body = '<'+elementName+'>'+body+'</'+elementName+'>'
    elif content == 'json':
        for ID in values:
            if ID['value'] is not None:
                body=body+'"'+ID['label']+'": "'+ID['value']+'",'
        if body != "": body = '"'+elementName+'":{'+body[:-1]+'}'
        if request != '{}': body = ','+body
    return insert_text(request,positionToInsert, body)

def request(method, url,  headers, body,  error):
    headers['X-Auth-Token'] = errorLabel (headers['X-Auth-Token'], error)
    url = errorUrl(url, error)

    if error == "GET" or error == "PUT" or error == "POST" or error == "DELETE":
        method = error

    if method == "GET":
        response = http.get(url, headers)
    elif method == "POST":
        response = http.post(url, headers, body)
    elif method == "PUT":
        response =  http.put(url, headers, body)
    elif method == "DELETE":
        response = http.delete(url, headers)

    printRequest(method,url,headers,body)
    #printResponse(response)
    return response

def insert_label (string, stringBeforeToInsert, newSubString):
    pos = string.find(stringBeforeToInsert)
    return string[:pos] + newSubString + string[pos:]

def insert_text (string, positionBeforeToInsert, newSubString):
    return string[:positionBeforeToInsert] + newSubString + string[positionBeforeToInsert:]

def printRequest(method, url, headers, body):
    print "------------------------------ Request ----------------------------------------------"
    print "url: "+ str(method) + "  "+str(url)
    print "\nHeader: "+ str (headers)+"\n"
    if body is not None:
        print "\nBody: init("+str (body)+")end\n\n"
    print "----------------------------------------------------------------------------------------\n\n\n\n"

def printResponse(response):
    print "---------------------------------- Response ----------------------------------------------"
    print "status code: "+str(response.status)
    print "\nHeader: "+ str(response.msg)
    print "\nBody: init("+str(response.read())+")end\n\n\n"
    print "----------------------------------------------------------------------------------------"

def get_body_expected(response_type, operation):
    for ID in body_message.Catalog_body:
        if ID["operation"] == operation and ID["code"] == response_type:
            return ID["body"]

def check_response_status(response, expected_status_code):
    """
    Checks that the response status is the expected one.
    :param response: Response to be checked.
    :param expected_status_code: Expected status code of the response.
    """

    assert response.status == expected_status_code, \
    "Wrong status code received: %d. Expected: %d. \n\nBody content: %s" \
    % (response.status, expected_status_code, response.read())

def check_response_body(response, expected_body):
    """
    Checks that the response body is the expected one.
    :param response: Response to be checked.
    :param expected_body: Expected body of the response.
    """

    resp = str(response.read())
    #print "\n\n\n respuesta: "+ resp+ "\n\n\n"
    #print "\n  esperado: "+ expected_body + "\n\n\n"
    #print "\n\n------------------------------------------------------------------------------------------------------------------------------------------------- "+str(resp.find(expected_body))+"\n\n"

    assert resp.find(expected_body) >= 0,  \
        "Wrong body received: %s \n\n Expected: %s" \
        % (resp, expected_body)


