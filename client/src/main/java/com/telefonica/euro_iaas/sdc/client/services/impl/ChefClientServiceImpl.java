/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ChefClientService;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

/**
 * @author jesus.movilla
 */
public class ChefClientServiceImpl extends AbstractBaseService implements ChefClientService {

    /**
     * @param client
     * @param baseHost
     * @param type
     */
    public ChefClientServiceImpl(Client client, String baseHost, String type) {
        setClient(client);
        setBaseHost(baseHost);
        setType(type);
    }

    public Task delete(String vdc, String chefClientName, String token) throws InvalidExecutionException {
        try {

            String url = getBaseHost() + MessageFormat.format(ClientConstants.CHEFCLIENT_PATH, vdc, chefClientName);
            Builder builder = createWebResource (url, token, vdc);
            // builder = addCallback(builder, callback);
            return builder.delete(Task.class);

        } catch (UniformInterfaceException e) {
            String errorMsg = " Error deleting a ChefClient  " + chefClientName + " in vdc " + vdc
                    + " from the Chef Server. Description: " + e.getMessage();
            throw new InvalidExecutionException(errorMsg, e);
        }
    }

    public ChefClient load(String vdc, String chefClientName, String token) throws ResourceNotFoundException {

        String url = null;
        try {
            url = getBaseHost() + MessageFormat.format(ClientConstants.CHEFCLIENT_PATH, vdc, chefClientName);

            Builder wr = createWebResource (url, token, vdc);

            return wr.accept(getType()).get(ChefClient.class);

        } catch (EntityNotFoundException enfe) {
            throw new ResourceNotFoundException(ChefClient.class, url);
        } catch (Exception e) {
            throw new ResourceNotFoundException(ChefClient.class, url);
        }
    }

    public ChefClient loadByHostname(String vdc, String hostname, String token) throws ResourceNotFoundException {

        String url = null;
        try {
            url = getBaseHost() + MessageFormat.format(ClientConstants.CHEFCLIENTHOSTNAME_PATH, vdc, hostname);

            Builder wr = createWebResource (url, token, vdc);

            return wr.accept(getType()).get(ChefClient.class);

        } catch (EntityNotFoundException enfe) {
            throw new ResourceNotFoundException(ChefClient.class, url);
        } catch (Exception e) {
            throw new ResourceNotFoundException(ChefClient.class, url);
        }
    }

}
