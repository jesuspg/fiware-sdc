/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;

import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.client.model.ProductInstances;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;

public class SDCWebResource<T> {

    private WebResource webResource;

    public SDCWebResource(WebResource webResource) {
        this.webResource = webResource;

    }

    public Task post(String type, Object dto, String callback) {
        WebResource.Builder builder = webResource.accept(type).type(type).entity(dto);
        builder = addCallback(builder, callback);
        return builder.post(Task.class);
    }

    private WebResource.Builder addCallback(WebResource.Builder resource, String callback) {
        if (!StringUtils.isEmpty(callback)) {
            resource = resource.header("callback", callback);
        }
        return resource;
    }

    public Task delete(String type, String callback) {
        WebResource.Builder builder = webResource.accept(type).type(type);
        builder = addCallback(builder, callback);
        return builder.delete(Task.class);
    }

    public Task delete(String type) {
        WebResource.Builder builder = webResource.accept(type).type(type);
        return builder.delete(Task.class);
    }

    public List<ProductInstance> queryParams(MultivaluedMap<String, String> searchParams, String type) {

        return webResource.queryParams(searchParams).accept(type).get(ProductInstances.class);

    }

    public T get(String type, Class<T> returnType) {
        WebResource.Builder builder = webResource.accept(type);
        return builder.get(returnType);
    }
}
