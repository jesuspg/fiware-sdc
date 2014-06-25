/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
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
