package com.telefonica.euro_iaas.sdc.client.services.impl;

import com.sun.jersey.api.client.WebResource;

public class SDCWebResourceFactory {

    public SDCWebResource getInstance(WebResource webResource) {
        return new SDCWebResource(webResource);

    }
}
