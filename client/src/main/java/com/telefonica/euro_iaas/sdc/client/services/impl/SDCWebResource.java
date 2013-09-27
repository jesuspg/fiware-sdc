package com.telefonica.euro_iaas.sdc.client.services.impl;

import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.model.Task;
import org.apache.commons.lang.StringUtils;

public class SDCWebResource {

    private WebResource webResource;

    public SDCWebResource(WebResource webResource) {
        this.webResource = webResource;

    }

    public WebResource getWebResource() {
        return webResource;
    }

    public void setWebResource(WebResource webResource) {
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

}
