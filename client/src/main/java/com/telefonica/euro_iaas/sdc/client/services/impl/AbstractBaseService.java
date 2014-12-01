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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.MDC;

import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;

/**
 * Provides common functionallity and fields to every service.
 * 
 * @author serch
 */
public class AbstractBaseService {

    private SdcClientConfig clientConfig;
    private String baseHost;
    private String type;

    /**
     * @return the client
     */
    public Client getClient() {
        return clientConfig.getClient();
    }

    public void setSdcClientConfig(SdcClientConfig clientConfig) {
        this.clientConfig = clientConfig;
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

    protected Invocation.Builder createWebResource(String url, String token, String tenant) {

        WebTarget webTarget = getClient().target(url);
        Invocation.Builder builder = webTarget.request(getType()).accept(getType());
        builder.header("X-Auth-Token", token);
        builder.header("Tenant-Id", tenant);
        String txId = MDC.get("txId");
        if (txId != null) {
            builder.header("txId", txId);
        }
        return builder;
    }

}
