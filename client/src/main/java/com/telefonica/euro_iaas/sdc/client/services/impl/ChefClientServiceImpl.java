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

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ChefClientService;
import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

/**
 * @author jesus.movilla
 */
public class ChefClientServiceImpl extends AbstractBaseService implements ChefClientService {

    /**
     * @param clientConfig
     * @param baseHost
     * @param type
     */
    public ChefClientServiceImpl(SdcClientConfig clientConfig, String baseHost, String type) {
        setSdcClientConfig(clientConfig);
        setBaseHost(baseHost);
        setType(type);
    }

    public Task delete(String vdc, String chefClientName, String token) throws InvalidExecutionException {

        String url = getBaseHost() + MessageFormat.format(ClientConstants.CHEFCLIENT_PATH, vdc, chefClientName);
        Response response = null;
        try {
            Invocation.Builder builder = createWebResource(url, token, vdc);
            // builder = addCallback(builder, callback);
            response = builder.delete();
            return response.readEntity(Task.class);
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }

    public ChefClient load(String vdc, String chefClientName, String token) throws ResourceNotFoundException {

        String url = null;
        Response response = null;
        try {
            url = getBaseHost() + MessageFormat.format(ClientConstants.CHEFCLIENT_PATH, vdc, chefClientName);

            Invocation.Builder wr = createWebResource(url, token, vdc);

            response = wr.get();
            return response.readEntity(ChefClient.class);

        } catch (EntityNotFoundException enfe) {
            throw new ResourceNotFoundException(ChefClient.class, url);
        } catch (Exception e) {
            throw new ResourceNotFoundException(ChefClient.class, url);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public ChefClient loadByHostname(String vdc, String hostname, String token) throws ResourceNotFoundException {

        String url = null;
        Response response = null;
        try {
            url = getBaseHost() + MessageFormat.format(ClientConstants.CHEFCLIENTHOSTNAME_PATH, vdc, hostname);

            Invocation.Builder wr = createWebResource(url, token, vdc);

            response = wr.accept(getType()).get();
            return response.readEntity(ChefClient.class);

        } catch (EntityNotFoundException enfe) {
            throw new ResourceNotFoundException(ChefClient.class, url);
        } catch (Exception e) {
            throw new ResourceNotFoundException(ChefClient.class, url);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

}
