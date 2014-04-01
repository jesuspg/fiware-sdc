/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.client.services.impl;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 * Provides common functionallity and fields to every service.
 * 
 * @author serch
 */
public class AbstractBaseService {

    private Client client;
    private String baseHost;
    private String type;

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * @return the baseHost
     */
    public String getBaseHost() {
        return baseHost;
    }

    /**
     * @param baseHost
     *            the baseHost to set
     */
    public void setBaseHost(String baseHost) {
        this.baseHost = baseHost;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public MultivaluedMap<String, String> addParam(MultivaluedMap<String, String> queryparams, String key, Object value) {
        if (value != null) {
            queryparams.add(key, value.toString());
        }
        return queryparams;
    }
    
    protected Builder createWebResource (String url, String token, String tenant) {        
    	WebResource webResource = getClient().resource(url);
    	Builder builder = webResource.accept(getType()).type(getType());
    	builder.header("X-Auth-Token", token);
    	builder.header("Tenant-Id", tenant);
        return builder;
    }
}
