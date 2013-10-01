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

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;

/**
 * Provides common functionallity and fields to every service.
 * 
 * @author serch
 */
public class AbstractBaseService {

    private Client client;
    private String baseHost;
    private String type;

    private SDCWebResourceFactory sdcWebResourceFactory = new SDCWebResourceFactory();

    public SDCWebResourceFactory getSdcWebResourceFactory() {
        return sdcWebResourceFactory;
    }

    public void setSdcWebResourceFactory(SDCWebResourceFactory sdcWebResourceFactory) {
        this.sdcWebResourceFactory = sdcWebResourceFactory;
    }

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
}
